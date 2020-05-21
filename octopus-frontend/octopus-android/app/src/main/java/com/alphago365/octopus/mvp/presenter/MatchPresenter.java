package com.alphago365.octopus.mvp.presenter;

import com.alphago365.octopus.mvp.view.MatchView;

public abstract class MatchPresenter extends BasePresenter<MatchView> {
    public abstract void showMatchList(String date, int latestDays);
}
