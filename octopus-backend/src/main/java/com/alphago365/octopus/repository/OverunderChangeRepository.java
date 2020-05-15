package com.alphago365.octopus.repository;

import com.alphago365.octopus.model.CompositeChangeId;
import com.alphago365.octopus.model.OverunderChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OverunderChangeRepository
        extends JpaRepository<OverunderChange, CompositeChangeId> {

}