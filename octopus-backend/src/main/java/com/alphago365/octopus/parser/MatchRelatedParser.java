package com.alphago365.octopus.parser;

import com.alphago365.octopus.config.DownloadConfig;
import com.alphago365.octopus.model.Match;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

public abstract class MatchRelatedParser<T> extends ListParser<T> {

    @Autowired
    protected DownloadConfig downloadConfig;

    protected final Match match;

    public MatchRelatedParser(@NotNull Match match) {
        this.match = match;
    }

}
