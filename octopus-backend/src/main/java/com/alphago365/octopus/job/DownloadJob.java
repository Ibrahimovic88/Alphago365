package com.alphago365.octopus.job;

import com.alphago365.octopus.config.DownloadConfig;
import com.alphago365.octopus.exception.ParseException;
import com.alphago365.octopus.service.RestService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class DownloadJob<T> extends Job {

    protected final String MATCH_ID_PLACEHOLDER = "MATCH_ID_PLACEHOLDER";
    protected final String PROVIDER_ID_PLACEHOLDER = "PROVIDER_ID_PLACEHOLDER";

    @Autowired
    protected RestService restService;

    @Autowired
    protected DownloadConfig downloadConfig;

    public DownloadJob(String name, long delay) {
        super(name, delay);
    }

    protected String download(String url) {
        return restService.getJson(url);
    }

    protected abstract List<T> parse(String json) throws ParseException;

    protected abstract List<T> save(List<T> list);
}
