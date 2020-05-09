package com.alphago365.octopus.job;

import com.alphago365.octopus.exception.RunJobException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Slf4j
@Data
public abstract class Job implements Delayed, Runnable {
    protected final String jobName;
    private long startTime;

    public Job(String jobName, long delay) {
        this.jobName = jobName;
        this.startTime = System.currentTimeMillis() + delay;
    }

    protected abstract void runJob() throws RunJobException;

    @Override
    public void run() {
        String oldName = Thread.currentThread().getName();
        Thread.currentThread().setName(getJobName());
        try {
            runJob();
        } catch (Exception e) {
            log.error(String.format("Run job `%s` with error", jobName), e);
        } finally {
            Thread.currentThread().setName(oldName);
        }
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long diff = startTime - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (this.startTime < ((Job) o).startTime) {
            return -1;
        }
        if (this.startTime > ((Job) o).startTime) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return String.format("%s:%d", jobName, startTime);
    }
}