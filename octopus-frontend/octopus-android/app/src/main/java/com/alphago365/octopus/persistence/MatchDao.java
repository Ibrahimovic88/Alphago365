package com.alphago365.octopus.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.alphago365.octopus.mvp.model.Match;

import java.time.Instant;
import java.util.List;

@Dao
public interface MatchDao {

    @Query("SELECT * FROM matches WHERE date BETWEEN :start AND :end")
    List<Match> findMatchesBetweenStartAndEnd(Instant start, Instant end);

    @Query("SELECT * FROM matches WHERE id =:id")
    Match findMatchById(Long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Match> matchList);

    @Delete
    void delete(Match match);
}