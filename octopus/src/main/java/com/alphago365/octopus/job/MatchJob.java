package com.alphago365.octopus.job;


import com.alphago365.octopus.exception.ParseException;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.parser.MatchParser;
import com.alphago365.octopus.service.MatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Scope("prototype")
@Component
@Slf4j
public class MatchJob extends DownloadJob {

    @Autowired
    private MatchParser matchParser;

    @Autowired
    private MatchService matchService;

    public MatchJob(long delay) {
        super("MJ", delay);
    }

    @Override
    public void runJob() {
        String url = downloadConfig.getMatchUrl() + LocalDate.now().minusDays(1).toString();
        String json = restService.getJson(url);
        try {
            List<Match> matchList = matchParser.parseResponse(json);
            matchService.saveAll(matchList);
        } catch (ParseException e) {
            log.error("Download match error", e);
        }
    }
}
