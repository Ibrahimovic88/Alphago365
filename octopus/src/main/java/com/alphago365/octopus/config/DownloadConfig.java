package com.alphago365.octopus.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@ConfigurationProperties(prefix = "download")
@Data
public class DownloadConfig {

    private String matchDate = LocalDate.now().minusDays(1).toString(); // default;

    private String matchUrl;

    private String oddsUrl;

    private long delay;

    @NotNull
    private List<Integer> oddsProviderIds;
    @NotNull
    private List<Integer> handicapProviderIds;

}
