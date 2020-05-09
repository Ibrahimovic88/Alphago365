package com.alphago365.octopus.model;

import com.alphago365.octopus.model.audit.DateAudit;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "matches")
@Data
public class Match extends DateAudit {

    @Id
    private Long id;

    private String league;

    private String home;

    private String away;

    private Instant kickoffTime;

    public String toString() {
        return String.format("%d %s %s %svs%s", id, league, kickoffTime.toString(), home, away);
    }

}
