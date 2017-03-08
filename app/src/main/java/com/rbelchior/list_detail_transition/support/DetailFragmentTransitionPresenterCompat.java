package com.rbelchior.list_detail_transition.support;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.rbelchior.list_detail_transition.DetailFragmentTransitionPresenter;
import com.squareup.picasso.RequestCreator;

/**
 * Shallow implementation of {@link DetailFragmentTransitionPresenter} for SDK < Lollipop.
 */
public class DetailFragmentTransitionPresenterCompat implements DetailFragmentTransitionPresenter {

    @Override
    public void create(Bundle savedInstanceState) {
    }

    @Override
    public void loadImage(ImageView imageView, RequestCreator requestCreator) {
        requestCreator.into(imageView);
    }

    @Nullable
    @Override
    public ImageView getSharedElementImageView() {
        return null;
    }
}
