package com.alphago365.octopus.model;

import com.alphago365.octopus.model.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "overunders")
@Data
@NoArgsConstructor
public class Overunder extends DateAudit {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Provider provider;

    private int displayOrder;

    @OneToMany(mappedBy = "overunder", cascade = CascadeType.ALL)
    private List<OverunderChange> changeHistories;

    private double ratioOver;
    private double ratioUnder;

    private double kellyOver;
    private double kellyUnder;

    private double payoff;

    public Overunder(Match match) {
        this.match = match;
    }

    public String toString() {
        return match.toString() + " " + provider.toString();
    }
}
