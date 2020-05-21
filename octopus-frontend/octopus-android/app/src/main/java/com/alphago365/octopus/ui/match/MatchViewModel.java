package com.alphago365.octopus.ui.match;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alphago365.octopus.mvp.model.Match;

import java.util.List;

public class MatchViewModel extends ViewModel {

    private MutableLiveData<List<Match>> liveData;

    public MatchViewModel() {
        liveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Match>> getLiveData() {
        return liveData;
    }

    public void setLiveData(List<Match> matchList) {
        liveData.setValue(matchList);
    }
}