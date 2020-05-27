package com.alphago365.octopus.job;


import com.alphago365.octopus.exception.ParseException;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.parser.MatchParser;
import com.alphago365.octopus.service.MatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

@Scope("prototype")
@Component
@Slf4j
public class MatchJob extends DownloadJob<Match> {

    @Autowired
    private MatchParser matchParser;

    @Autowired
    private MatchService matchService;

    @NotNull
    private final String matchDate;

    public MatchJob(long delay, String date) {
        super(String.format("M-%s", date), delay);
        this.matchDate = date;
    }

    @Override
    public void runJob() {
        String url = downloadConfig.getMatchUrl().replace("DATE_PLACEHOLDER", matchDate);
        save(parse(download(url)));
    }

    public List<Match> save(List<Match> matchList) {
        return matchService.saveAll(matchList);
    }

    public List<Match> parse(String json) {
        try {
            return matchParser.parseResponse(json);
        } catch (ParseException e) {
            log.error("Parse match error", e);
        }
        return Collections.emptyList();
    }
}
