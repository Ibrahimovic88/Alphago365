package com.alphago365.octopus.repository;

import com.alphago365.octopus.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByKickoffTimeBetween(Instant start, Instant end);
}