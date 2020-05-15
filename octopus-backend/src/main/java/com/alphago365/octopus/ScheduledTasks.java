package com.alphago365.octopus;

import com.alphago365.octopus.config.DownloadConfig;
import com.alphago365.octopus.config.JobConfig;
import com.alphago365.octopus.job.*;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.service.HandicapService;
import com.alphago365.octopus.service.MatchService;
import com.alphago365.octopus.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class ScheduledTasks {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PriorityJobScheduler priorityJobScheduler;

    @Autowired
    private DownloadConfig downloadConfig;

    @Autowired
    private JobConfig jobConfig;

    @Autowired
    private MatchService matchService;

    @Autowired
    private HandicapService handicapService;

    @Scheduled(cron = "${scheduled.cron.downloadMatch}")
    private void downloadMatch() {
        MatchJob matchJob = applicationContext.getBean(MatchJob.class,
                jobConfig.getMatchJobDelay(), downloadConfig.getMatchDate());
        priorityJobScheduler.scheduleJob(matchJob);
    }

    @Scheduled(cron = "${scheduled.cron.updateMatch}")
    private void updateMatchRelated() {
        List<Match> latestDaysMatchList = getMatches();
        latestDaysMatchList.forEach(match -> {

            // odds
            OddsJob oddsJob = applicationContext.getBean(OddsJob.class,
                    jobConfig.getOddsJobDelay(), match);
            priorityJobScheduler.scheduleJob(oddsJob);

            // handicap
            HandicapJob handicapJob = applicationContext.getBean(HandicapJob.class,
                    jobConfig.getHandicapJobDelay(), match);
            priorityJobScheduler.scheduleJob(handicapJob);

            // overunder
            OverunderJob overunderJob = applicationContext.getBean(OverunderJob.class,
                    jobConfig.getOverunderJobDelay(), match);
            priorityJobScheduler.scheduleJob(overunderJob);
        });
    }

    @Scheduled(cron = "${scheduled.cron.analyzeHandicap}")
    private void analyzeHandicap() {
        List<Match> latestDaysMatchList = getMatches();
        latestDaysMatchList.forEach(match -> handicapService.findByMatchId(match.getId()).forEach(handicap -> {
            Instant analysisTime = DateUtils.asInstant(LocalDateTime.now());
            HandicapAnalysisJob analysisJob = applicationContext.getBean(HandicapAnalysisJob.class,
                    jobConfig.getHandicapAnalysisJobDelay(), handicap, analysisTime);
            priorityJobScheduler.scheduleJob(analysisJob);
        }));
    }

    private List<Match> getMatches() {
        int latestDays = downloadConfig.getLatestDays();
        Instant date = DateUtils.parseToInstant(downloadConfig.getMatchDate(), "yyyy-MM-dd");
        Instant start = date.minus(latestDays, ChronoUnit.DAYS);
        Instant end = date.plus(latestDays, ChronoUnit.DAYS);
        return matchService.findMatchesBetweenStartAndEnd(start, end);
    }
}