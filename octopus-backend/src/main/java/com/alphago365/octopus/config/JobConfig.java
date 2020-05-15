package com.alphago365.octopus.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "job")
@Data
public class JobConfig {

    private int poolSize = 1;

    private long minSleepMilliseconds;

    private long matchJobDelay;

    private long oddsJobDelay;
    private long oddsChangeJobDelay;

    private long handicapJobDelay;
    private long handicapChangeJobDelay;
    private long handicapAnalysisJobDelay;

    private long overunderJobDelay;
    private long overunderChangeJobDelay;
}
