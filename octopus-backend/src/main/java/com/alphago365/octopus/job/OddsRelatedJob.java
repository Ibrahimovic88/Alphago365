package com.alphago365.octopus.job;

import com.alphago365.octopus.model.Odds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

abstract class OddsRelatedJob<T> extends DownloadJob<T> {

    protected final Odds odds;

    @Autowired
    protected ApplicationContext applicationContext;

    public OddsRelatedJob(String name, long delay, Odds odds) {
        super(name, delay);
        this.odds = odds;
    }
}
