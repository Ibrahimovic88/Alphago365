package com.alphago365.octopus;

import com.alphago365.octopus.config.DownloadConfig;
import com.alphago365.octopus.job.MatchJob;
import com.alphago365.octopus.job.OddsJob;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.model.Odds;
import com.alphago365.octopus.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
    private void updateMatch() {
        List<Match> latestDaysMatchList = matchService.getMatchListOfLatestDays(1);
        latestDaysMatchList.forEach(match -> {
            OddsJob oddsJob = applicationContext.getBean(OddsJob.class,
                    downloadConfig.getDelay(), match);
            priorityJobScheduler.scheduleJob(oddsJob);
        });
    }
}