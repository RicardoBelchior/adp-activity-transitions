package com.alexjlockwood.activity.transitions;

import android.app.Fragment;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import tv.teads.sdk.adContainer.adapter.TeadsNotify;
import tv.teads.sdk.publisher.TeadsAd;


public class DetailsListFragment extends Fragment {
    private static final String TAG = DetailsListFragment.class.getSimpleName();
    private static final boolean DEBUG = false;

    private static final String ARG_ALBUM_IMAGE_POSITION = "arg_album_image_position";
    private static final String ARG_STARTING_ALBUM_IMAGE_POSITION = "arg_starting_album_image_position";

    private static final long FAKE_LOADING_DELAY = 100;

    private RecyclerView recyclerView;
    private DetailsAdapter detailsAdapter;

    private int mStartingPosition;
    private int mAlbumPosition;
    private boolean mIsTransitioning;
    private long mBackgroundImageFadeMillis;

    private TeadsAd teadsAd;

    public static DetailsListFragment newInstance(int position, int startingPosition) {
        Bundle args = new Bundle();
        args.putInt(ARG_ALBUM_IMAGE_POSITION, position);
        args.putInt(ARG_STARTING_ALBUM_IMAGE_POSITION, startingPosition);
        DetailsListFragment fragment = new DetailsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStartingPosition = getArguments().getInt(ARG_STARTING_ALBUM_IMAGE_POSITION);
        mAlbumPosition = getArguments().getInt(ARG_ALBUM_IMAGE_POSITION);
        mIsTransitioning = savedInstanceState == null && mStartingPosition == mAlbumPosition;
        mBackgroundImageFadeMillis = getResources().getInteger(
                R.integer.fragment_details_background_image_fade_millis);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState) {

        recyclerView = (RecyclerView) rootView;
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        detailsAdapter = new DetailsAdapter(getActivity(), mStartingPosition, mAlbumPosition, mIsTransitioning, mBackgroundImageFadeMillis);
        RecyclerView.Adapter adapter = new TeadsAdapter(detailsAdapter);
        recyclerView.setAdapter(adapter);

        // This simulates loading the data from DB asynchronously.
        // If FAKE_LOADING_DELAY is higher, than the transition runs smoothly.
        // It's probably not a good idea to swap the RecyclerView adapter just before the transition occurs.
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(loadDataRunnable, FAKE_LOADING_DELAY);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (teadsAd != null) {
            teadsAd.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (teadsAd != null) {
            teadsAd.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (teadsAd != null) {
            teadsAd.clean();
        }
    }

    private Runnable loadDataRunnable = new Runnable() {
        @Override
        public void run() {

            // If we comment this, the transition runs smoothly, independent of how long loading
            // data from DB takes (simulated by #FAKE_LOADING_DELAY)
            loadAds();

            detailsAdapter.setFetchingData(false);
            notifyDataSetChanged();
        }
    };

    private void loadAds() {

        if (teadsAd == null) {

            final String publisherId = TeadsFactory.getPublisherId(recyclerView.getContext());
            if (TextUtils.isEmpty(publisherId)) {
                return;
            }

            // Create the Teads Advert, also adds their own RecyclerView.Adapter, when loadAds is called.
            teadsAd = TeadsFactory.createArticleDetailAdvert(
                    getActivity(), recyclerView, publisherId);
        }

        teadsAd.load();
    }

    private void notifyDataSetChanged() {
        final RecyclerView.Adapter adapter = recyclerView.getAdapter();
        try {
            ((TeadsNotify) adapter).teadsNotifyDataSetChanged();
        } catch (ClassCastException e) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Returns the shared element that should be transitioned back to the previous Activity,
     * or null if the view is not visible on the screen.
     */
    @Nullable
    ImageView getAlbumImage() {
        final ImageView imageView = detailsAdapter.getAlbumImage();
        if (isViewInBounds(getActivity().getWindow().getDecorView(), imageView)) {
            return imageView;
        }
        return null;
    }

    /**
     * Returns true if {@param view} is contained within {@param container}'s bounds.
     */
    private static boolean isViewInBounds(@NonNull View container, @NonNull View view) {
        Rect containerBounds = new Rect();
        container.getHitRect(containerBounds);
        return view.getLocalVisibleRect(containerBounds);
    }

}
