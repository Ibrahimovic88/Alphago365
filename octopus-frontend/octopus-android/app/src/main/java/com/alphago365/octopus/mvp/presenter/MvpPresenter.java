package com.alphago365.octopus.mvp.presenter;

import com.alphago365.octopus.mvp.view.MvpView;

public interface MvpPresenter<V extends MvpView> {

    void onAttach(V view);

    void onResume();

    void onDestroy();

}
