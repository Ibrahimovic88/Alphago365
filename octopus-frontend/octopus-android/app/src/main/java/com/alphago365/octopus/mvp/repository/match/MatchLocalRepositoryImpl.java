package com.alphago365.octopus.mvp.repository.match;

import com.alphago365.octopus.persistence.Converters;
import com.alphago365.octopus.mvp.model.Match;
import com.alphago365.octopus.persistence.MatchDao;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import io.reactivex.Observable;

public class MatchLocalRepositoryImpl implements MatchLocalRepository {

    private final MatchDao matchDao;

    public MatchLocalRepositoryImpl(MatchDao matchDao) {
        this.matchDao = matchDao;
    }

    @Override
    public void saveMatchList(List<Match> matchList) {
        matchDao.insertAll(matchList);
    }

    @Override
    public Observable<List<Match>> getMatchList(final String date, final int latestDays) {
        return Observable.fromCallable(() -> {
            Instant instant = Converters.instantFromDateString(date);
            Instant start = instant.minus(latestDays, ChronoUnit.DAYS);
            Instant end = instant.plus(latestDays, ChronoUnit.DAYS);
            return matchDao.findMatchesBetweenStartAndEnd(start, end);
        });
    }
}
