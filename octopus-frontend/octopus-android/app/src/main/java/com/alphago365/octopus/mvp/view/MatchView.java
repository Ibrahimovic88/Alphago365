package com.alphago365.octopus.mvp.view;

import com.alphago365.octopus.mvp.model.Match;

import java.util.List;

public interface MatchView extends MvpView {

    void showMatchList(List<Match> matchList);
}
