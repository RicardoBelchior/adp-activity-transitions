package com.rbelchior.list_detail_transition;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.RequestCreator;

/**
 * Presenter responsible for helping the shared element transition between a list activity and
 * a detail activity, whose items in the detail are not immediately available.
 * <br/>
 * This presenter is to be implemented in the "detail" fragment -- manager by a view pager.
 * Make sure to call create method in the fragment, and load the shared element image view using
 * our method {@link #loadImage(ImageView, RequestCreator)}.
 * Also required is to set the transition name on the shared element image view, according to
 * {@link ListTransitionPresenter.ListTransitionView#getTransitionName(int)}.
 */
public interface DetailFragmentTransitionPresenter {

    interface DetailFragmentTransitionView {

        Fragment getFragment();

        @Nullable
        ImageView getSharedElementImageView();
    }

    /** Call at {@link Fragment#onViewCreated(View, Bundle)} */
    void create(Bundle savedInstanceState);

    /**
     * Use this method to load the shared image view in the 'detail' fragment.
     * Provide the image view and a Picasso request and we will take care of loading the actual image.
     *
     * @param imageView Shared image view
     * @param requestCreator Picasso {@link RequestCreator}
     */
    void loadImage(ImageView imageView, RequestCreator requestCreator);

    /**
     * @return The shared image view.
     */
    @Nullable
    ImageView getSharedElementImageView();

}
