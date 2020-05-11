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
@Table(name = "overunder_changes")
@Data
@NoArgsConstructor
public class OverunderChange extends DateAudit {

    @EmbeddedId
    private CompositeChangeId compositeChangeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "overunder_id", nullable = false)
    @JsonIgnore
    private Overunder overunder;

    private Instant kickoffTime; // redundant, easy to calculate interval time till match kickoff

    private double over;
    private double boundary;
    private double under;

    @JsonIgnore
    public Instant getUpdateTime() {
        return compositeChangeId.getUpdateTime();
    }

    public OverunderChange(Overunder handicap) {
        this.overunder = handicap;
    }
}
