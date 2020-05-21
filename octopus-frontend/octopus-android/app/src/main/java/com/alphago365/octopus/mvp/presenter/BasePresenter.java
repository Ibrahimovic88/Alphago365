package com.alphago365.octopus.mvp.presenter;

import com.alphago365.octopus.mvp.view.MvpView;

import java.lang.ref.WeakReference;

public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private WeakReference<V> viewRef;

    @Override
    public void onAttach(V view) {
        this.viewRef = new WeakReference<>(view);
    }

    @Override
    public void onResume() {

    }

    protected boolean isViewNotAttached() {
        return viewRef == null || viewRef.get() == null;
    }

    @Override
    public void onDestroy() {
        if (viewRef == null) {
            return;
        }
        viewRef.clear();
        ;
        viewRef = null;
    }

    protected V getView() {
        return viewRef.get();
    }
}
