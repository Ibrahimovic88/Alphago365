package com.alphago365.octopus.mvp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.Instant;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(tableName = "matches")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Match implements MvpModel {

    @PrimaryKey
    private Long id;

    private Instant date;

    @ColumnInfo(name = "serial_number")
    private Integer serialNumber;

    private String league;

    private String home;

    private String away;

    @ColumnInfo(name = "kickoff_time")
    private Instant kickoffTime;

    @ColumnInfo(name = "actual_wdl")
    private WDL actualWdl = WDL.UNKNOWN; // default

    @ColumnInfo(name = "half_home_goals")
    private Integer halfHomeGoals;
    @ColumnInfo(name = "half_away_goals")
    private Integer halfAwayGoals;

    @ColumnInfo(name = "final_home_goals")
    private Integer finalHomeGoals;
    @ColumnInfo(name = "final_away_goals")
    private Integer finalAwayGoals;
}