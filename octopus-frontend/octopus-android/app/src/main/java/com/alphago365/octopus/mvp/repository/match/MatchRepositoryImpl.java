package com.alphago365.octopus.mvp.repository.match;

import com.alphago365.octopus.mvp.model.Match;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class MatchRepositoryImpl implements MatchRepository {

    private final MatchRemoteRepository matchRemoteRepository;
    private final MatchLocalRepository matchLocalRepository;

    public MatchRepositoryImpl(MatchRemoteRepository matchRemoteRepository,
                               MatchLocalRepository matchLocalRepository) {
        this.matchRemoteRepository = matchRemoteRepository;
        this.matchLocalRepository = matchLocalRepository;
    }

    @Override
    public Observable<List<Match>> getMatchList(final String date, final int latestDays) {
        Observable<List<Match>> remoteObservable = matchRemoteRepository.getMatchList(date, latestDays)
                .doOnNext(matchLocalRepository::saveMatchList).subscribeOn(Schedulers.io());
        Observable<List<Match>> localObservable = matchLocalRepository.getMatchList(date, latestDays)
                .subscribeOn(Schedulers.io());
        return Observable.mergeDelayError(remoteObservable, localObservable);
    }
}
