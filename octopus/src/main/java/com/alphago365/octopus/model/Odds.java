package com.alphago365.octopus.model;

import com.alphago365.octopus.model.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "odds")
@Data
@NoArgsConstructor
public class Odds extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(mappedBy = "odds", cascade = CascadeType.ALL)
    private List<OddsChange> changeHistories;

    private double ratioHome;
    private double ratioDraw;
    private double ratioAway;

    private double kellyHome;
    private double kellyDraw;
    private double kellyAway;

    private double payoff;

    public Odds(Match match) {
        this.match = match;
    }

    public String toString() {
        return match.toString() + " " + provider.toString();
    }
}
