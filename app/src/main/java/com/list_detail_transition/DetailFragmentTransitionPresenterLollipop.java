package com.list_detail_transition;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.RequestCreator;

/**
 * See {@link DetailFragmentTransitionPresenter}
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class DetailFragmentTransitionPresenterLollipop implements DetailFragmentTransitionPresenter {

    private static final String ARG_ITEM_POSITION = "arg_item_position";
    private static final String ARG_STARTING_ITEM_POSITION = "arg_starting_item_position";


    private ImageView sharedElementImage;
    private int startingPosition;
    private int currentPosition;

    /**
     * timeout mechanism when loading the image, otherwise we may get a deadlock for not calling
     * {@link Activity#startPostponedEnterTransition()}.
     */
    private ImageLoadingTimeout imageLoadingTimeout;

    private DetailFragmentTransitionView view;

    DetailFragmentTransitionPresenterLollipop(DetailFragmentTransitionView view) {
        this.view = view;
    }

    @Override
    public void create(Bundle savedInstanceState) {
        final Bundle args = view.getFragment().getArguments();
        startingPosition = args.getInt(ARG_STARTING_ITEM_POSITION);
        currentPosition = args.getInt(ARG_ITEM_POSITION);

        imageLoadingTimeout = new ImageLoadingTimeout();
    }

    @Override
    public void loadImage(ImageView imageView, RequestCreator requestCreator) {
        this.sharedElementImage = imageView;

        imageLoadingTimeout.startTimeout();
        requestCreator.into(sharedElementImage, mImageCallback);
    }

    /**
     * Returns the shared element that should be transitioned back to the previous Activity,
     * or null if the view is not visible on the screen.
     */
    @Nullable
    @Override
    public ImageView getSharedElementImageView() {
        if (isViewInBounds(view.getFragment().getActivity().getWindow().getDecorView(), sharedElementImage)) {
            return sharedElementImage;
        }
        return null;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////


    private final Callback mImageCallback = new Callback() {
        @Override
        public void onSuccess() {
            imageLoadingTimeout.removeTimeout();
            if (imageLoadingTimeout.isTimeoutTriggered()) {
                return;
            }

            startPostponedEnterTransition();
        }

        @Override
        public void onError() {
            imageLoadingTimeout.removeTimeout();
            if (imageLoadingTimeout.isTimeoutTriggered()) {
                return;
            }

            startPostponedEnterTransition();
        }
    };

    private void startPostponedEnterTransition() {
        if (currentPosition == startingPosition) {
            sharedElementImage.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    sharedElementImage.getViewTreeObserver().removeOnPreDrawListener(this);
                    final Activity activity = view.getFragment().getActivity();
                    if (activity != null) {
                        activity.startPostponedEnterTransition();
                    }
                    return true;
                }
            });
        }
    }

    /**
     * Returns true if {@param view} is contained within {@param container}'s bounds.
     */
    private static boolean isViewInBounds(@NonNull View container, @NonNull View view) {
        Rect containerBounds = new Rect();
        container.getHitRect(containerBounds);
        return view.getLocalVisibleRect(containerBounds);
    }


    private class ImageLoadingTimeout {
        static final long TIMEOUT_IMAGE_LOADING_MILLI = 2000;


        Handler handler;
        boolean eventOccurred;

        ImageLoadingTimeout() {
            handler = new Handler(Looper.getMainLooper());
            eventOccurred = false;
        }

        void startTimeout() {
            removeTimeout();
            handler.postDelayed(imageLoadingRunnable, TIMEOUT_IMAGE_LOADING_MILLI);
        }

        void removeTimeout() {
            handler.removeCallbacks(imageLoadingRunnable);
        }

        boolean isTimeoutTriggered() {
            return eventOccurred;
        }

        private Runnable imageLoadingRunnable = new Runnable() {
            @Override
            public void run() {
                eventOccurred = true;
                startPostponedEnterTransition();
            }
        };
    }

}
