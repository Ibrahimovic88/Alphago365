package com.alphago365.octopus.mvp.presenter;

import com.alphago365.octopus.mvp.model.Match;
import com.alphago365.octopus.mvp.repository.match.MatchRepository;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class MatchPresenterImpl extends MatchPresenter {

    private final MatchRepository matchRepository;

    private final Scheduler scheduler;

    private Disposable disposable;

    public MatchPresenterImpl(MatchRepository matchRepository, Scheduler scheduler) {
        this.matchRepository = matchRepository;
        this.scheduler = scheduler;
    }


    @Override
    public void showMatchList(String date, int latestDays) {
        if (isViewNotAttached()) {
            return;
        }

        disposable = matchRepository.getMatchList(date, latestDays)
                .observeOn(scheduler)
                .subscribeWith(new DisposableObserver<List<Match>>() {

                    @Override
                    public void onNext(List<Match> matchList) {
                        if (isViewNotAttached()) {
                            return;
                        }

                        getView().showMatchList(matchList);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
