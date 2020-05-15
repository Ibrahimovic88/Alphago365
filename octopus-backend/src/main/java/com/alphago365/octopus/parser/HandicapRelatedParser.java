package com.alphago365.octopus.parser;

import com.alphago365.octopus.config.DownloadConfig;
import com.alphago365.octopus.model.Handicap;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

public abstract class HandicapRelatedParser<T> extends ListParser<T> {

    @Autowired
    protected DownloadConfig downloadConfig;

    protected final Handicap handicap;

    public HandicapRelatedParser(@NotNull Handicap handicap) {
        this.handicap = handicap;
    }

}
