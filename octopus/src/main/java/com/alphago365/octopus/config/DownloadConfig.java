package com.alphago365.octopus.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "download")
@Data
public class DownloadConfig {
    private String matchUrl;
    private long delay;
}
