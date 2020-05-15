package com.alphago365.octopus.repository;

import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.model.Odds;
import com.alphago365.octopus.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OddsRepository extends JpaRepository<Odds, Long> {

    List<Odds> findByMatch(Match match);

    Optional<Odds> findByMatchAndProvider(Match match, Provider provider);

    boolean existsByMatchAndProvider(Match match, Provider provider);

}