package com.alphago365.octopus;

import com.alphago365.octopus.config.DownloadConfig;
import com.alphago365.octopus.job.MatchJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PriorityJobScheduler priorityJobScheduler;

    @Autowired
    private DownloadConfig downloadConfig;

    @Scheduled(cron = "${scheduled.cron.downloadMatch}")
    private void downloadMatch() {
        MatchJob matchJob = applicationContext.getBean(MatchJob.class, downloadConfig.getDelay());
        priorityJobScheduler.scheduleJob(matchJob);
    }

}