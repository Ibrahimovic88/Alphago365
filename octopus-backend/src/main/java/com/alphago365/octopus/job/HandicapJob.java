package com.alphago365.octopus.job;


import com.alphago365.octopus.PriorityJobScheduler;
import com.alphago365.octopus.exception.ParseException;
import com.alphago365.octopus.model.Handicap;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.parser.HandicapParser;
import com.alphago365.octopus.service.HandicapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Scope("prototype")
@Component
@Slf4j
public class HandicapJob extends MatchRelatedJob<Handicap> {

    @Autowired
    private HandicapService handicapService;

    @Autowired
    private PriorityJobScheduler priorityJobScheduler;

    public HandicapJob(long delay, Match match) {
        super(String.format("H-%d", match.getId()), delay, match);
    }

    @Override
    public void runJob() {
        AtomicLong sum = new AtomicLong(0);
        String url = downloadConfig.getHandicapUrl()
                .replaceFirst(MATCH_ID_PLACEHOLDER, String.valueOf(match.getId()));
        try {
            save(parse(download(url))).forEach(handicap -> {
                sum.getAndAdd(jobConfig.getHandicapChangeJobDelay());
                HandicapChangeJob handicapChangeJob = applicationContext.getBean(HandicapChangeJob.class, sum.get(), handicap);
                priorityJobScheduler.scheduleJob(handicapChangeJob);
            });
        } catch (ParseException e) {
            log.error("Download handicap error with url: {}", url, e);
        }
    }

    public List<Handicap> save(List<Handicap> handicapList) {
        return handicapService.saveAll(handicapList);
    }

    public List<Handicap> parse(String json) throws ParseException {
        try {
            HandicapParser oddsParser = applicationContext.getBean(HandicapParser.class, match);
            return oddsParser.parseResponse(json);
        } catch (ParseException e) {
            log.error("Parse handicap error with json: {}", json, e);
            throw e;
        }
    }
}
