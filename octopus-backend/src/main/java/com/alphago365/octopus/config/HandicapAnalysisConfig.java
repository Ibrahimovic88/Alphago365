package com.alphago365.octopus.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "analysis")
@Data
public class HandicapAnalysisConfig {
    private double overflowThreshold = 0.1;
}
