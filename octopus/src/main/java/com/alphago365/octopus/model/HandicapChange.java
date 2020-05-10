package com.alphago365.octopus.model;

import com.alphago365.octopus.model.audit.DateAudit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.Instant;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "handicap_changes")
@Data
public class HandicapChange extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handicap_id", nullable = false)
    private Handicap handicap;

    private Instant updateTime;
    private Instant kickoffTime; // redundant, easy to calculate interval time till match kickoff

    private double home;
    private double handicapNumber;
    private double away;
}
