package com.alphago365.octopus.mvp.repository.match;

import com.alphago365.octopus.mvp.model.Match;

import java.util.List;

import io.reactivex.Observable;

public interface MatchRepository {

    Observable<List<Match>> getMatchList(final String date, final int latestDays);

}
