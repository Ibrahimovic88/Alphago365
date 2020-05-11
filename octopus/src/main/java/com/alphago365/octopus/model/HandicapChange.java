package com.alphago365.octopus.model;

import com.alphago365.octopus.model.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "handicap_changes")
@Data
@NoArgsConstructor
public class HandicapChange extends DateAudit {

    @EmbeddedId
    private CompositeChangeId compositeChangeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handicap_id", nullable = false)
    @JsonIgnore
    private Handicap handicap;

    private Instant kickoffTime; // redundant, easy to calculate interval time till match kickoff

    private double home;
    private double boundary;
    private double away;

    @JsonIgnore
    public Instant getUpdateTime() {
        return compositeChangeId.getUpdateTime();
    }

    public HandicapChange(Handicap handicap) {
        this.handicap = handicap;
    }
}
