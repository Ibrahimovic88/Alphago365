package com.alphago365.octopus.job;


import com.alphago365.octopus.exception.ParseException;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.model.Odds;
import com.alphago365.octopus.parser.OddsParser;
import com.alphago365.octopus.service.OddsService;
import com.alphago365.octopus.service.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Scope("prototype")
@Component
@Slf4j
public class OddsJob extends MatchRelatedJob {

    @Autowired
    private OddsService oddsService;

    public OddsJob(long delay, Match match) {
        super("OJ", delay, match);
    }

    @Override
    public void runJob() {
        save(parse(download()));
    }

    private void save(List<Odds> oddsList) {
        oddsService.saveAll(oddsList);
    }

    private List<Odds> parse(String json) {
        try {
            OddsParser oddsParser = applicationContext.getBean(OddsParser.class, match);
            return oddsParser.parseResponse(json);
        } catch (ParseException e) {
            log.error("Download odds error", e);
        }
        return Collections.emptyList();
    }

    private String download() {
        String url = downloadConfig.getOddsUrl() + match.getId();
        return restService.getJson(url);
    }
}
