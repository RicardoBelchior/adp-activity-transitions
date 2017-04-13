package com.rbelchior.list_detail_transition;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Pair;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * See {@link ListTransitionPresenter}
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class ListTransitionPresenterLollipop implements ListTransitionPresenter {

    static final String EXTRA_STARTING_ITEM_POSITION = "extra_starting_item_position";
    static final String EXTRA_CURRENT_ITEM_POSITION = "extra_current_item_position";

    private Bundle tmpReenterState;
    private boolean isDetailsActivityStarted;

    private ListTransitionView view;

    ListTransitionPresenterLollipop(ListTransitionView view) {
        this.view = view;
    }

    @Override
    public void create() {

        view.getActivity()
                .setExitSharedElementCallback(mCallback);
    }

    @Override
    public void resume() {
        isDetailsActivityStarted = false;
    }

    @Override
    public void reenterActivity(int resultCode, Intent data) {
        tmpReenterState = new Bundle(data.getExtras());
        int startingPosition = tmpReenterState.getInt(EXTRA_STARTING_ITEM_POSITION);
        int currentPosition = tmpReenterState.getInt(EXTRA_CURRENT_ITEM_POSITION);
        if (startingPosition != currentPosition) {
            view.scrollToPosition(currentPosition);
        }

        view.getActivity()
                .postponeEnterTransition();

        addOnPreDrawListener();
    }

    @Override
    public void startDetail(Intent intent, int itemPosition, View... sharedViews) {

        intent.putExtra(EXTRA_STARTING_ITEM_POSITION, itemPosition);

        if (!isDetailsActivityStarted) {
            isDetailsActivityStarted = true;
            final Activity activity = view.getActivity();
            final List<Pair> sharedElements = new ArrayList<>();

            if (sharedViews != null && sharedViews.length > 0) {
                for (View view : sharedViews) {
                    if (view != null && view.getTransitionName() != null && view.getVisibility() == View.VISIBLE) {
                        sharedElements.add(
                                new Pair<>(view, view.getTransitionName()));
                    }
                }
            }

            activity.startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(
                            activity, sharedElements.toArray(new Pair[sharedElements.size()])).toBundle());
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private void addOnPreDrawListener() {
        view.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.removeOnPreDrawListener(this);
                // TODO: figure out why it is necessary to request layout here in order to get a smooth transition.
                view.requestListLayout();
                view.getActivity()
                        .startPostponedEnterTransition();
                return true;
            }
        });
    }

    private final SharedElementCallback mCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (tmpReenterState != null) {
                int startingPosition = tmpReenterState.getInt(EXTRA_STARTING_ITEM_POSITION);
                int currentPosition = tmpReenterState.getInt(EXTRA_CURRENT_ITEM_POSITION);

                if (startingPosition != currentPosition) {
                    // If startingPosition != currentPosition the user must have swiped to a
                    // different page in the DetailsActivity. We must update the shared element
                    // so that the correct one falls into place.
                    String newTransitionName = view.getTransitionName(currentPosition);
                    View newSharedElement = view.getViewWithTag(newTransitionName);
                    if (newSharedElement != null) {
                        names.clear();
                        names.add(newTransitionName);
                        sharedElements.clear();
                        sharedElements.put(newTransitionName, newSharedElement);
                    }
                }

                tmpReenterState = null;
            } else {
                // If tmpReenterState is null, then the activity is exiting.
                View decorView = view.getActivity().getWindow().getDecorView();
                View navigationBar = decorView.findViewById(android.R.id.navigationBarBackground);
                View statusBar = decorView.findViewById(android.R.id.statusBarBackground);

                if (navigationBar != null) {
                    names.add(navigationBar.getTransitionName());
                    sharedElements.put(navigationBar.getTransitionName(), navigationBar);
                }
                if (statusBar != null) {
                    names.add(statusBar.getTransitionName());
                    sharedElements.put(statusBar.getTransitionName(), statusBar);
                }
            }
        }
    };
}
