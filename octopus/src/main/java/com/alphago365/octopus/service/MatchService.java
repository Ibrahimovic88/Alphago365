package com.alphago365.octopus.service;

import com.alphago365.octopus.exception.ResourceNotFoundException;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.repository.MatchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Transactional
    public List<Match> getMatchList(int latestDays) {
        Instant now = LocalDateTime.now().toInstant(ZoneOffset.UTC);
        Instant start = now.minus(latestDays, ChronoUnit.DAYS);
        Instant end = now.plus(latestDays, ChronoUnit.DAYS);
        return matchRepository.findByKickoffTimeBetween(start, end);
    }

    @Transactional
    public void save(Match match) {
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
