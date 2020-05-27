package com.alphago365.octopus.job;


import com.alphago365.octopus.exception.ParseException;
import com.alphago365.octopus.model.Overunder;
import com.alphago365.octopus.model.OverunderChange;
import com.alphago365.octopus.parser.OverunderChangeParser;
import com.alphago365.octopus.service.OverunderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Scope("prototype")
@Component
@Slf4j
public class OverunderChangeJob extends OverunderRelatedJob<OverunderChange> {

    @Autowired
    private OverunderService overunderService;

    public OverunderChangeJob(long delay, Overunder overunder) {
        super(String.format("OVC-%d-%d", overunder.getMatch().getId(), overunder.getProvider().getId()), delay, overunder);
    }

    @Override
    public void runJob() {
        Long matchId = overunder.getMatch().getId();
        Integer providerId = overunder.getProvider().getId();
        String url = downloadConfig.getOverunderChangeUrl()
                .replaceFirst(MATCH_ID_PLACEHOLDER, String.valueOf(matchId)) // first
                .replaceFirst(PROVIDER_ID_PLACEHOLDER, String.valueOf(providerId)); // second
        save(parse(download(url)));
    }

    public List<OverunderChange> save(List<OverunderChange> overunderChangeList) {
        return overunderService.saveAllChanges(overunderChangeList);
    }

    public List<OverunderChange> parse(String json) {
        try {
            OverunderChangeParser overunderChangeParser = applicationContext.getBean(OverunderChangeParser.class, overunder);
            return overunderChangeParser.parseResponse(json);
        } catch (ParseException e) {
            log.error("Download overunder change error", e);
        }
        return Collections.emptyList();
    }
}
