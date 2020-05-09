package com.alphago365.octopus;

import com.alphago365.octopus.exception.ResourceNotFoundException;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.repository.MatchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Transactional
    public List<Match> getMatchList(int latestDays) {
        Instant now = Instant.now();
        Instant start = now.minus(1, ChronoUnit.DAYS);
        Instant end = now.plus(1, ChronoUnit.DAYS);
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
}
