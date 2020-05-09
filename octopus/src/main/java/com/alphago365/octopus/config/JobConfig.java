package com.alphago365.octopus.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "job")
@Data
public class JobConfig {
    private int poolSize = 1;
}
