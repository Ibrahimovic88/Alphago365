package com.alphago365.octopus.service;

import com.alphago365.octopus.exception.ResourceNotFoundException;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.repository.MatchRepository;
import com.alphago365.octopus.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    public List<Match> getMatchListOfLatestDays(int latestDays) {
        Instant today = DateUtils.asInstant(LocalDate.now());
        Instant start = today.minus(latestDays, ChronoUnit.DAYS);
        Instant end = today.plus(latestDays, ChronoUnit.DAYS);
        return matchRepository.findByDateBetween(start, end);
    }

    @Transactional
    public void save(Match match) {
        if (matchRepository.existsById(match.getId())) {
            log.warn("Skip to save existing match " + match);
            return;
        }
        matchRepository.save(match);
    }

    public Match findById(long id) {
        return matchRepository.findById(id).<ResourceNotFoundException>orElseThrow(() -> {
            throw new ResourceNotFoundException("Match not found by id: " + id);
        });
    }

    @Transactional
    public void saveAll(List<Match> matchList) {
        matchRepository.saveAll(matchList);
    }
}
