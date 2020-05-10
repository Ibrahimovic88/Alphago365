package com.alphago365.octopus.service;

import com.alphago365.octopus.exception.ResourceNotFoundException;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.repository.MatchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Transactional
    public List<Match> getMatchListOfLatestDays(int latestDays) {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(latestDays);
        LocalDate end = today.plusDays(latestDays);
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
