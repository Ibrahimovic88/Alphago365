package com.alphago365.octopus;

import com.alphago365.octopus.config.JobConfig;
import com.alphago365.octopus.job.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class PriorityJobScheduler {

    private final JobConfig jobConfig;

    private final ExecutorService priorityJobPoolExecutor;
    private final ExecutorService priorityJobScheduler = Executors.newSingleThreadExecutor();
    private final DelayQueue<Job> jobDelayQueue;

    public PriorityJobScheduler(JobConfig jobConfig) {
        this.jobConfig = jobConfig;
        priorityJobPoolExecutor = Executors.newFixedThreadPool(jobConfig.getPoolSize());
        jobDelayQueue = new DelayQueue<>();
        priorityJobScheduler.execute(() -> {
            while (true) {
                try {
                    priorityJobPoolExecutor.execute(jobDelayQueue.take());
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                    break;
                }
            }
        });
    }

    public void scheduleJob(Job job) {
        jobDelayQueue.add(job);
    }

    public void shutdown() {
        priorityJobScheduler.shutdown();
        priorityJobPoolExecutor.shutdown();
    }
}