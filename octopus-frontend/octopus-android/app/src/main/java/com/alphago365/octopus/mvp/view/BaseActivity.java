package com.alphago365.octopus.mvp.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alphago365.octopus.mvp.presenter.MvpPresenter;

public abstract class BaseActivity<P extends MvpPresenter<MvpView>> extends AppCompatActivity implements MvpView {

    private P presenter;

    public P getPresenter() {
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
    protected void onResume() {
        super.onResume();
        getPresenter().onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().onDestroy();
    }
}
