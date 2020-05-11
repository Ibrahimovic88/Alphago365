package com.alphago365.octopus;

import com.alphago365.octopus.config.JobConfig;
import com.alphago365.octopus.job.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class PriorityJobScheduler {

    private final JobConfig jobConfig;

    private final ExecutorService priorityJobPoolExecutor;
    private final ExecutorService priorityJobScheduler = Executors.newSingleThreadExecutor();
    private final DelayQueue<Job> jobDelayQueue;
    private final AtomicBoolean running = new AtomicBoolean(true);

    public PriorityJobScheduler(JobConfig jobConfig) {
        this.jobConfig = jobConfig;
        priorityJobPoolExecutor = Executors.newFixedThreadPool(jobConfig.getPoolSize());
        jobDelayQueue = new DelayQueue<>();
        final long minIntervalSeconds = jobConfig.getMinIntervalSeconds();
        priorityJobScheduler.execute(() -> {
            LocalDateTime lastTakeTime = null;
            while (running.weakCompareAndSet(true, true)) {
                if (lastTakeTime != null) {
                    long intervalSeconds = ChronoUnit.SECONDS.between(lastTakeTime, LocalDateTime.now());
                    if (intervalSeconds <= minIntervalSeconds) {
                        log.warn(String.format("Sleep %d seconds, due to interval from last job %d is less than %d", minIntervalSeconds, intervalSeconds, minIntervalSeconds));
                        try {
                            Thread.sleep(minIntervalSeconds * 1000);
                        } catch (InterruptedException e) {
                            log.error(e.getMessage());
                        }
                    }
                }
                try {
                    priorityJobPoolExecutor.execute(jobDelayQueue.take());
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                    break;
                }
                lastTakeTime = LocalDateTime.now();
            }
        });
    }

    public void scheduleJob(Job job) {
        jobDelayQueue.add(job);
    }

    public void shutdown() {
        running.compareAndSet(true, false);
        priorityJobScheduler.shutdown();
        priorityJobPoolExecutor.shutdown();
    }
}