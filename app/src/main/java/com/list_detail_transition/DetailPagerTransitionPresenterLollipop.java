package com.list_detail_transition;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.List;
import java.util.Map;

/**
 * @see {@link DetailPagerTransitionPresenter}
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class DetailPagerTransitionPresenterLollipop implements DetailPagerTransitionPresenter {

    private static final String STATE_CURRENT_PAGE_POSITION = "state_current_page_position";


    private int currentPosition;
    private int startingPosition;
    private boolean isReturning;
    private boolean configurationChanged;

    private DetailPagerTransitionView view;

    DetailPagerTransitionPresenterLollipop(DetailPagerTransitionView view) {
        this.view = view;
    }

    @Override
    public void create(Bundle savedInstanceState) {
        final Activity activity = view.getActivity();

        activity.postponeEnterTransition();
        activity.setEnterSharedElementCallback(mCallback);

        startingPosition = activity.getIntent().getIntExtra(ListTransitionPresenterLollipop.EXTRA_STARTING_ITEM_POSITION, 0);
        if (savedInstanceState == null) {
            currentPosition = startingPosition;
            configurationChanged = false;
        } else {
            currentPosition = savedInstanceState.getInt(STATE_CURRENT_PAGE_POSITION);
        }

        view.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }
        });
    }

    @Override
    public void saveInstanceState(@NonNull Bundle outState) {
        outState.putInt(STATE_CURRENT_PAGE_POSITION, currentPosition);
    }

    @Override
    public void finishAfterTransition() {
        isReturning = true;
        Intent data = new Intent();
        data.putExtra(ListTransitionPresenterLollipop.EXTRA_STARTING_ITEM_POSITION, startingPosition);
        data.putExtra(ListTransitionPresenterLollipop.EXTRA_CURRENT_ITEM_POSITION, currentPosition);

        int resultCode = configurationChanged ?
                Activity.RESULT_CANCELED :
                Activity.RESULT_OK;

        view.getActivity().setResult(resultCode, data);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        configurationChanged = true;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////


    private final SharedElementCallback mCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (isReturning) {

                if (configurationChanged) {
                    names.clear();
                    sharedElements.clear();
                    return;
                }

                ImageView sharedElement = view.getCurrentFragment().getSharedElementImageView();
                if (sharedElement == null) {
                    // If shared element is null, then it has been scrolled off screen and
                    // no longer visible. In this case we cancel the shared element transition by
                    // removing the shared element from the shared elements map.
                    names.clear();
                    sharedElements.clear();
                } else if (startingPosition != currentPosition) {
                    // If the user has swiped to a different ViewPager page, then we need to
                    // remove the old shared element and replace it with the new shared element
                    // that should be transitioned instead.
                    names.clear();
                    names.add(sharedElement.getTransitionName());
                    sharedElements.clear();
                    sharedElements.put(sharedElement.getTransitionName(), sharedElement);
                }
            }
        }
    };
}
