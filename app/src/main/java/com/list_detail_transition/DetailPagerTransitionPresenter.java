package com.list_detail_transition;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;

/**
 * Presenter responsible for helping the shared element transition between a list activity and
 * a detail activity, whose items in the detail are not immediately available.
 * <br/>
 * This presenter is to be implemented in the "detail" activity which contains the view pager.
 * Make sure to call create/saveInstanceState/finishAfterTransition method in the activity.
 * Also each fragment of the view pager must implement {@link DetailFragmentTransitionPresenter.DetailFragmentTransitionView}.
 */
public interface DetailPagerTransitionPresenter {

    interface DetailPagerTransitionView {

        /** return current {@link Activity} instance */
        Activity getActivity();

        /** return currently selected fragment in the viewpager */
        DetailFragmentTransitionPresenter.DetailFragmentTransitionView getCurrentFragment();

        /** {@link ViewPager#addOnPageChangeListener(ViewPager.OnPageChangeListener)} */
        void addOnPageChangeListener(ViewPager.OnPageChangeListener listener);
    }

    /** Call at {@link Activity#onCreate(Bundle)} */
    void create(Bundle savedInstanceState);

    /**
     * Call at {@link Activity#onSaveInstanceState(Bundle)}.
     */
    void saveInstanceState(@NonNull Bundle outState);

    /**
     * Call at {@link Activity#finishAfterTransition()}. Make sure to call this one before super.
     */
    void finishAfterTransition();

    /**
     * Call at {@link Activity#onConfigurationChanged(Configuration)}}.
     */
    void onConfigurationChanged(Configuration newConfig);
}