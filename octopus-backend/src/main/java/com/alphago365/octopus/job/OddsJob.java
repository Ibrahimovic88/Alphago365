package com.alphago365.octopus.job;


import com.alphago365.octopus.PriorityJobScheduler;
import com.alphago365.octopus.exception.ParseException;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.model.Odds;
import com.alphago365.octopus.parser.OddsParser;
import com.alphago365.octopus.service.OddsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Scope("prototype")
@Component
@Slf4j
public class OddsJob extends MatchRelatedJob<Odds> {

    @Autowired
    private OddsService oddsService;

    @Autowired
    PriorityJobScheduler priorityJobScheduler;

    public OddsJob(long delay, Match match) {
        super(String.format("O-%d", match.getId()), delay, match);
    }

    @Override
    public void runJob() {
        String url = downloadConfig.getOddsUrl()
                .replaceFirst(MATCH_ID_PLACEHOLDER, String.valueOf(match.getId()));
        AtomicLong sum = new AtomicLong(0);
        try {
            save(parse(download(url))).forEach(odds -> {
                sum.getAndAdd(jobConfig.getOddsChangeJobDelay());
                OddsChangeJob oddsChangeJob = applicationContext.getBean(OddsChangeJob.class, sum.get(), odds);
                priorityJobScheduler.scheduleJob(oddsChangeJob);
            });
        } catch (ParseException e) {
            log.error("Download odds error with url: {}", url, e);
        }
    }

    public List<Odds> save(List<Odds> oddsList) {
        return oddsService.saveAll(oddsList);
    }

    public List<Odds> parse(String json) throws ParseException {
        try {
            OddsParser oddsParser = applicationContext.getBean(OddsParser.class, match);
            return oddsParser.parseResponse(json);
        } catch (ParseException e) {
            log.error("Parse odds error with json: {}", json, e);
            throw e;
        }
    }
}
