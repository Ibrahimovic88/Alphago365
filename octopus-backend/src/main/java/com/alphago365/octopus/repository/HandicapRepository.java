package com.alphago365.octopus.repository;

import com.alphago365.octopus.model.Handicap;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HandicapRepository extends JpaRepository<Handicap, Long> {

    List<Handicap> findByMatch(Match match);

    Optional<Handicap> findByMatchAndProvider(Match match, Provider provider);

    boolean existsByMatchAndProvider(Match match, Provider provider);

}