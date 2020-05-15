package com.alphago365.octopus.repository;

import com.alphago365.octopus.model.OddsChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OddsChangeRepository extends JpaRepository<OddsChange, Long> {

}