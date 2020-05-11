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

    private final ExecutorService priorityJobPoolExecutor;
    private final ExecutorService priorityJobScheduler = Executors.newSingleThreadExecutor();
    private final DelayQueue<Job> jobDelayQueue;
    private final AtomicBoolean running = new AtomicBoolean(true);

    public PriorityJobScheduler(JobConfig jobConfig) {
        priorityJobPoolExecutor = Executors.newFixedThreadPool(jobConfig.getPoolSize());
        jobDelayQueue = new DelayQueue<>();
        final long minSleepMilliseconds = jobConfig.getMinSleepMilliseconds();
        priorityJobScheduler.execute(() -> {
            LocalDateTime lastExecuteTime = null;
            while (running.compareAndSet(true, true)) {
                Job job;
                try {
                    job = jobDelayQueue.take();
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                    break;
                }
                if (lastExecuteTime != null) {
                    long intervalMilliseconds = ChronoUnit.MILLIS.between(lastExecuteTime, LocalDateTime.now());
                    if (intervalMilliseconds <= minSleepMilliseconds) {
                        log.warn(String.format("Sleep %dms, due to interval from last job %dms is less than %dms",
                                minSleepMilliseconds, intervalMilliseconds, minSleepMilliseconds));
                        try {
                            Thread.sleep(minSleepMilliseconds);
                        } catch (InterruptedException e) {
                            log.error(e.getMessage());
                        }
                    }
                }
                lastExecuteTime = LocalDateTime.now();
                priorityJobPoolExecutor.execute(job);
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