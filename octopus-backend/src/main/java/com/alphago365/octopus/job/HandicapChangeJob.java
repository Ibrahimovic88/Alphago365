package com.alphago365.octopus.job;


import com.alphago365.octopus.PriorityJobScheduler;
import com.alphago365.octopus.exception.ParseException;
import com.alphago365.octopus.model.Handicap;
import com.alphago365.octopus.model.HandicapChange;
import com.alphago365.octopus.parser.HandicapChangeParser;
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
public class HandicapChangeJob extends HandicapRelatedJob<HandicapChange> {

    @Autowired
    private HandicapService handicapService;

    @Autowired
    private PriorityJobScheduler priorityJobScheduler;

    public HandicapChangeJob(long delay, Handicap handicap) {
        super(String.format("HC-%d-%d", handicap.getMatch().getId(), handicap.getProvider().getId()), delay, handicap);
    }

    @Override
    public void runJob() {
        Long matchId = handicap.getMatch().getId();
        Integer providerId = handicap.getProvider().getId();
        String url = downloadConfig.getHandicapChangeUrl()
                .replaceFirst(MATCH_ID_PLACEHOLDER, String.valueOf(matchId)) // first
                .replaceFirst(PROVIDER_ID_PLACEHOLDER, String.valueOf(providerId)); // second
        save(parse(download(url)));
    }

    public List<HandicapChange> save(List<HandicapChange> handicapChangeList) {
        return handicapService.saveAllChanges(handicapChangeList);
    }

    public List<HandicapChange> parse(String json) {
        try {
            HandicapChangeParser oddsParser = applicationContext.getBean(HandicapChangeParser.class, handicap);
            return oddsParser.parseResponse(json);
        } catch (ParseException e) {
            log.error("Download handicap change error", e);
        }
        return Collections.emptyList();
    }
}
