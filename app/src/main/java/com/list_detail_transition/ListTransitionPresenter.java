package com.list_detail_transition;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Presenter responsible for helping the shared element transition between a list activity and
 * a detail activity, whose items in the detail are not immediately available.
 * <br/>
 * This presenter is to be implemented in the "list" activity.
 * Make sure to call create/resume/reenter activity methods, and open the detail activity using our
 * #startDetail method.
 * <br/><br/>
 * Also important, is to add a transition name (according to #getTransitionName) and tag
 * to each view of the list.
 */
public interface ListTransitionPresenter {

    interface ListTransitionView {

        /** return current {@link Activity} instance */
        Activity getActivity();

        /** return {@link android.support.v7.widget.RecyclerView#findViewWithTag(Object)} */
        View getViewWithTag(String transitionName);

        /** {@link android.support.v7.widget.RecyclerView#scrollToPosition(int)} */
        void scrollToPosition(int position);

        /** {@link ViewTreeObserver#addOnPreDrawListener(ViewTreeObserver.OnPreDrawListener)} */
        void addOnPreDrawListener(ViewTreeObserver.OnPreDrawListener onPreDrawListener);

        /** {@link ViewTreeObserver#removeOnPreDrawListener(ViewTreeObserver.OnPreDrawListener)} */
        void removeOnPreDrawListener(ViewTreeObserver.OnPreDrawListener onPreDrawListener);

        /** {@link View#requestLayout()} on the recycler view */
        void requestListLayout();

        /** Return a transition name, unique per each data item */
        String getTransitionName(int currentPosition);
    }

    void create();

    void resume();

    void reenterActivity(int resultCode, Intent data);

    void startDetail(Intent intent, View imageView, int itemPosition);

}
