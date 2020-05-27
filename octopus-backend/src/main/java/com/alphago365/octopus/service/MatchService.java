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
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    public List<Match> findMatchesOfLatestDaysWithSpecifiedDate(Instant date, int latestDays) {
        Instant start = date.minus(latestDays, ChronoUnit.DAYS);
        Instant end = date.plus(latestDays, ChronoUnit.DAYS);
        return findMatchesBetweenStartAndEnd(start, end);
    }

    public List<Match> findMatchesBetweenStartAndEnd(Instant start, Instant end) {
        return matchRepository.findByDateBetween(start, end)
                .parallelStream()
                .sorted(Comparator
                        .comparing(Match::getKickoffTime)
                        .thenComparing(Match::getSerialNumber))
                .collect(Collectors.toList());
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
    public List<Match> saveAll(List<Match> matchList) {
        return matchRepository.saveAll(matchList);
    }
}
