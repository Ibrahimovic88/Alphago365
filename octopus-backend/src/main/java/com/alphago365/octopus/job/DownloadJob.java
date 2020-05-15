package com.alphago365.octopus.job;

import com.alphago365.octopus.config.DownloadConfig;
import com.alphago365.octopus.service.RestService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class DownloadJob extends Job {

    @Autowired
    protected RestService restService;

    @Autowired
    protected DownloadConfig downloadConfig;

    public DownloadJob(String name, long delay) {
        super(name, delay);
    }
}
