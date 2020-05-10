package com.alphago365.octopus.model;

import com.alphago365.octopus.model.audit.DateAudit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.Instant;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "odds_changes")
@Data
public class OddsChange extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "odds_id", nullable = false)
    private Odds odds;

    private Instant updateTime;
    private Instant kickoffTime; // redundant, easy to calculate interval time till match kickoff

    private double home;
    private double draw;
    private double away;
}
