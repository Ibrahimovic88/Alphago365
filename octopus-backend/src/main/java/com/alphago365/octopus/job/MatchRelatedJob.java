package com.alphago365.octopus.job;

import com.alphago365.octopus.model.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

abstract class MatchRelatedJob extends DownloadJob {

    protected final Match match;

    @Autowired
    protected ApplicationContext applicationContext;

    public MatchRelatedJob(String name, long delay, Match match) {
        super(name, delay);
        this.match = match;
    }
}
