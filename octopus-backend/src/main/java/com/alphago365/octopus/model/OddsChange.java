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
@Table(name = "odds_changes")
@Data
@NoArgsConstructor
public class OddsChange extends DateAudit {

    @EmbeddedId
    private CompositeChangeId compositeChangeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "odds_id", nullable = false)
    @JsonIgnore
    private Odds odds;

    private Instant kickoffTime; // redundant, easy to calculate interval time till match kickoff

    private double home;
    private double draw;
    private double away;

    public OddsChange(Odds odds) {
        this.odds = odds;
    }

    @JsonIgnore
    public Instant getUpdateTime() {
        return compositeChangeId.getUpdateTime();
    }
}
