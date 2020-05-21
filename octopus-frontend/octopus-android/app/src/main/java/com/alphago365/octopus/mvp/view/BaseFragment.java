package com.alphago365.octopus.mvp.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alphago365.octopus.mvp.presenter.MvpPresenter;

public abstract class BaseFragment<P extends MvpPresenter> extends Fragment implements MvpView {

    private P presenter;

    protected P getPresenter() {
        if (presenter == null) {
            presenter = createPresenter();
        }
        if (presenter == null) {
            throw new IllegalStateException("createPresenter method must be implemented");
        }
        return presenter;
    }

    protected abstract P createPresenter();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPresenter().onAttach(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPresenter().onDestroy();
    }
}
