package com.alphago365.octopus.mvp.repository.match;

import com.alphago365.octopus.mvp.model.Match;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface MatchRemoteService {
    @GET("/api/v2/matches/specified-date")
    Observable<List<Match>> getMatchList(@Query("date") final String date
            , @Query("latest-days") final int latestDays);
}
