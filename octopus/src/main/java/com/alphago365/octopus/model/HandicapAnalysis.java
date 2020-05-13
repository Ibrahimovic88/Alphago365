package com.alphago365.octopus.model;

import com.alphago365.octopus.model.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "handicap_analyses")
@Data
@NoArgsConstructor
public class HandicapAnalysis extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handicap_id", nullable = false)
    @JsonIgnore
    private Handicap handicap;

    @NotNull
    private Instant kickoffTime; // redundant, easy to calculate interval time till match kickoff

    @NotNull
    private Instant updateTime;

    @NotNull
    private Instant analysisTime;

    private double home;
    private double boundary;
    private double away;

    @Enumerated(EnumType.STRING)
    private BoundaryChange boundaryChange = BoundaryChange.KEEP;

    @Enumerated(EnumType.STRING)
    private WaterChange waterChange = WaterChange.KEEP;

    private boolean first;

}
