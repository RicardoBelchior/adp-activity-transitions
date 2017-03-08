package com.rbelchior.list_detail_transition.support;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.rbelchior.list_detail_transition.DetailPagerTransitionPresenter;

/**
 * Shallow implementation of {@link DetailPagerTransitionPresenter} for SDK < Lollipop.
 */
public class DetailPagerTransitionPresenterCompat implements DetailPagerTransitionPresenter {
    @Override
    public void create(Bundle savedInstanceState) {
    }

    @Override
    public void saveInstanceState(@NonNull Bundle outState) {
    }

    @Override
    public void finishAfterTransition() {
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    }
}
