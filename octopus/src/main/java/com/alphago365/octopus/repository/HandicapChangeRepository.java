package com.alphago365.octopus.repository;

import com.alphago365.octopus.model.HandicapChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HandicapChangeRepository extends JpaRepository<HandicapChange, Long> {

}