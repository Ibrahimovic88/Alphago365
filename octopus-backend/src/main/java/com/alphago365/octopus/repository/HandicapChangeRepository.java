package com.alphago365.octopus.repository;

import com.alphago365.octopus.model.CompositeChangeId;
import com.alphago365.octopus.model.Handicap;
import com.alphago365.octopus.model.HandicapChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HandicapChangeRepository
        extends JpaRepository<HandicapChange, CompositeChangeId> {

    List<HandicapChange> findByHandicap(Handicap handicap);

}