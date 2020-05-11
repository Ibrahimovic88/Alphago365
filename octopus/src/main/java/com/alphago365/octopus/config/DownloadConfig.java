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

    // match url
    private String matchUrl;

    // odds related url
    private String oddsUrl;
    private String oddsChangeUrl;

    // handicap related url
    private String handicapUrl;
    private String handicapChangeUrl;

    // overunder related url
    private String overunderUrl;
    private String overunderChangeUrl;

    private long matchDelay;

    private long oddsDelay;
    private long oddsChangeDelay;

    private long handicapDelay;
    private long handicapChangeDelay;

    private long overunderDelay;
    private long overunderChangeDelay;

    @NotNull
    private List<Integer> oddsProviderIds;
    @NotNull
    private List<Integer> handicapProviderIds;

}
