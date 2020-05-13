package com.alphago365.octopus.config;

import com.alphago365.octopus.util.DateUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@ConfigurationProperties(prefix = "download")
@Data
public class DownloadConfig {

    // default down yesterday
    private String matchDate = DateUtils.format(LocalDate.now().minusDays(1), "yyyy-MM-dd");

    // default update latest 1 day match
    private int latestDays = 1;

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

    @NotNull
    private List<Integer> oddsProviderIds;
    @NotNull
    private List<Integer> handicapProviderIds;

}
