package com.alphago365.octopus.job;


import com.alphago365.octopus.exception.ParseException;
import com.alphago365.octopus.model.Handicap;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.parser.HandicapParser;
import com.alphago365.octopus.service.HandicapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Scope("prototype")
@Component
@Slf4j
public class HandicapJob extends MatchRelatedJob {

    @Autowired
    private HandicapService handicapService;

    public HandicapJob(long delay, Match match) {
        super("HJ", delay, match);
    }

    @Override
    public void runJob() {
        save(parse(download()));
    }

    private void save(List<Handicap> oddsList) {
        handicapService.saveAll(oddsList);
    }

    private List<Handicap> parse(String json) {
        try {
            HandicapParser oddsParser = applicationContext.getBean(HandicapParser.class, match);
            return oddsParser.parseResponse(json);
        } catch (ParseException e) {
            log.error("Download odds error", e);
        }
        return Collections.emptyList();
    }

    private String download() {
        String url = downloadConfig.getHandicapUrl() + match.getId();
        return restService.getJson(url);
    }
}
