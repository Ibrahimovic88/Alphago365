package com.alphago365.octopus.job;

import com.alphago365.octopus.model.Handicap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

abstract class HandicapRelatedJob extends DownloadJob {

    protected final Handicap handicap;

    @Autowired
    protected ApplicationContext applicationContext;

    public HandicapRelatedJob(String name, long delay, Handicap handicap) {
        super(name, delay);
        this.handicap = handicap;
    }
}
