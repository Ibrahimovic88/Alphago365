package com.alphago365.octopus;

import com.alphago365.octopus.config.JobConfig;
import com.alphago365.octopus.exception.RunJobException;
import com.alphago365.octopus.job.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class PriorityJobScheduler {

    private final ExecutorService priorityJobPoolExecutor;
    private final ExecutorService priorityJobScheduler = Executors.newSingleThreadExecutor();
    private final DelayQueue<Job> jobDelayQueue;
    private final AtomicBoolean running = new AtomicBoolean(true);

    private final Map<String, Job> jobCache = new ConcurrentHashMap<>();

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
                if (lastExecuteTime == null) { // for the first job
                    lastExecuteTime = LocalDateTime.now();
                }
                long intervalMilliseconds = ChronoUnit.MILLIS.between(lastExecuteTime, LocalDateTime.now());
                if (intervalMilliseconds <= minSleepMilliseconds) {
                    log.debug(String.format("Sleep %dms, due to interval from last job %dms is less than %dms",
                            minSleepMilliseconds, intervalMilliseconds, minSleepMilliseconds));
                    try {
                        Thread.sleep(minSleepMilliseconds);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage());
                    }
                }
                lastExecuteTime = LocalDateTime.now();
                priorityJobPoolExecutor.execute(job);
                dumpJobCache(job);
                jobCache.remove(job.getJobName());
            }
        });
    }

    private void dumpJobCache(Job job) {
        try {
            new DumpJobCacheJob(job).runJob();
        } catch (RunJobException e) {
            log.error("Dump job error", e);
        }
    }

    public void scheduleJob(Job job) {
        String jobName = job.getJobName();
        if (jobCache.containsKey(jobName)) {
            log.debug("Skip to schedule existing job " + jobName);
            return;
        }
        jobDelayQueue.add(job);
        jobCache.put(jobName, job);
    }

    public void shutdown() {
        running.compareAndSet(true, false);
        priorityJobScheduler.shutdown();
        priorityJobPoolExecutor.shutdown();
    }

    private class DumpJobCacheJob extends Job {

        private final Job current;

        public DumpJobCacheJob(Job current) {
            super("DumpJobCache", 500);
            this.current = current;
        }

        @Override
        protected void runJob() throws RunJobException {
            List<Job> jobCacheCopy = Collections.unmodifiableList(new ArrayList<>(jobCache.values()));
            StringBuilder builder = new StringBuilder("\n");
            jobCacheCopy.stream().sorted(Comparator.naturalOrder()).forEach(job -> {
                String jobName = job.getJobName();
                builder.append(String.format("%-13s",jobName))
                        .append(", ")
                        .append(String.format("%5d", job.getDelay(TimeUnit.MILLISECONDS)));
                if (jobName.equals(current.getJobName())) {
                    builder.append(" <----current job");
                }
                builder.append("\n");
            });
            log.debug(builder.toString());
        }
    }
}