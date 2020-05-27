package com.alphago365.octopus.job;

import com.alphago365.octopus.model.Overunder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

abstract class OverunderRelatedJob<T> extends DownloadJob<T> {

    protected final Overunder overunder;

    @Autowired
    protected ApplicationContext applicationContext;

    public OverunderRelatedJob(String name, long delay, Overunder overunder) {
        super(name, delay);
        this.overunder = overunder;
    }
}
