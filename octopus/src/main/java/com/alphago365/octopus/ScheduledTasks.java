package com.alphago365.octopus;

import com.alphago365.octopus.config.DownloadConfig;
import com.alphago365.octopus.job.HandicapJob;
import com.alphago365.octopus.job.MatchJob;
import com.alphago365.octopus.job.OddsJob;
import com.alphago365.octopus.job.OverunderJob;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
    private MatchService matchService;

    @Scheduled(cron = "${scheduled.cron.downloadMatch}")
    private void downloadMatch() {
        MatchJob matchJob = applicationContext.getBean(MatchJob.class,
                downloadConfig.getDelay(), downloadConfig.getMatchDate());
        priorityJobScheduler.scheduleJob(matchJob);
    }

    @Scheduled(cron = "${scheduled.cron.updateMatch}")
    private void updateMatchRelated() {
        List<Match> latestDaysMatchList = matchService.getMatchListOfLatestDays(1);
        latestDaysMatchList.forEach(match -> {

            // odds
            OddsJob oddsJob = applicationContext.getBean(OddsJob.class,
                    downloadConfig.getDelay(), match);
            priorityJobScheduler.scheduleJob(oddsJob);

            // handicap
            HandicapJob handicapJob = applicationContext.getBean(HandicapJob.class,
                    downloadConfig.getDelay(), match);
            priorityJobScheduler.scheduleJob(handicapJob);

            // overunder
            OverunderJob overunderJob = applicationContext.getBean(OverunderJob.class,
                    downloadConfig.getDelay(), match);
            priorityJobScheduler.scheduleJob(overunderJob);
        });
    }
}