package com.alphago365.octopus.job;


import com.alphago365.octopus.PriorityJobScheduler;
import com.alphago365.octopus.exception.ParseException;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.model.Overunder;
import com.alphago365.octopus.parser.OverunderParser;
import com.alphago365.octopus.service.OverunderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Scope("prototype")
@Component
@Slf4j
public class OverunderJob extends MatchRelatedJob<Overunder> {

    @Autowired
    private OverunderService overunderService;

    @Autowired
    private PriorityJobScheduler priorityJobScheduler;

    public OverunderJob(long delay, Match match) {
        super(String.format("OV-%d", match.getId()), delay, match);
    }

    @Override
    public void runJob() {
        String url = downloadConfig.getOverunderUrl()
                .replaceFirst(MATCH_ID_PLACEHOLDER, String.valueOf(match.getId()));
        AtomicLong sum = new AtomicLong(0);
        save(parse(download(url))).forEach(overunder -> {
            sum.getAndAdd(jobConfig.getOverunderChangeJobDelay());
            OverunderChangeJob overunderChangeJob = applicationContext.getBean(OverunderChangeJob.class, sum.get(), overunder);
            priorityJobScheduler.scheduleJob(overunderChangeJob);
        });
    }

    public List<Overunder> save(List<Overunder> overunderList) {
        return overunderService.saveAll(overunderList);
    }

    public List<Overunder> parse(String json) {
        try {
            OverunderParser overunderParser = applicationContext.getBean(OverunderParser.class, match);
            return overunderParser.parseResponse(json);
        } catch (ParseException e) {
            log.error("Download overunder error", e);
        }
        return Collections.emptyList();
    }
}
