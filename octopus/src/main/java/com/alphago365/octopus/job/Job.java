package com.alphago365.octopus.job;

import lombok.Data;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Data
public abstract class Job implements Delayed, Runnable {
    protected final String name;
    private long startTime;
 
    public Job(String name, long delay) {
        this.name = name;
        this.startTime = System.currentTimeMillis() + delay;
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
        return String.format("%s:%d", name, startTime);
    }
}