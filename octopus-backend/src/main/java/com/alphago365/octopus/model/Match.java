package com.alphago365.octopus.model;

import com.alphago365.octopus.model.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
public class Match extends DateAudit {

    @Id
    private Long id;

    private Instant date;

    private Integer serialNumber;

    private String league;

    private String home;

    private String away;

    private Instant kickoffTime;

    public String toString() {
        return String.format("%d %s %s %svs%s", id, league, kickoffTime.toString(), home, away);
    }

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Odds> oddsList;
}
