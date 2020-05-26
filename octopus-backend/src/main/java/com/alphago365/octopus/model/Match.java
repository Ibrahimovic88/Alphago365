package com.alphago365.octopus.model;

import com.alphago365.octopus.converter.WdlConverter;
import com.alphago365.octopus.model.audit.DateAudit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

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

    @Convert(converter = WdlConverter.class)
    private WDL actualWdl = WDL.UNKNOWN; // default

    private Integer halfHomeGoals;
    private Integer halfAwayGoals;

    private Integer finalHomeGoals;
    private Integer finalAwayGoals;

    public String toString() {
        return String.format("%d %s %s %svs%s", id, league, kickoffTime.toString(), home, away);
    }
}
