package com.list_detail_transition.support;

import android.content.Intent;
import android.view.View;

import com.mirror.news.ui.view.list_detail_transition.ListTransitionPresenter;

/**
 * Shallow implementation of {@link ListTransitionPresenter} for SDK < Lollipop.
 */
public class ListTransitionPresenterCompat implements ListTransitionPresenter {

    private ListTransitionPresenter.ListTransitionView view;

    public ListTransitionPresenterCompat(ListTransitionPresenter.ListTransitionView view) {
        this.view = view;
    }

    @Override
    public void create() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void reenterActivity(int resultCode, Intent data) {
    }

    @Override
    public void startDetail(Intent intent, View imageView, int itemPosition) {
        view.getActivity().startActivity(intent);
    }
}
