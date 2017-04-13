package com.alexjlockwood.activity.transitions;

import android.app.Fragment;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import static com.alexjlockwood.activity.transitions.Constants.ALBUM_IMAGE_URLS;
import static com.alexjlockwood.activity.transitions.Constants.ALBUM_NAMES;
import static com.alexjlockwood.activity.transitions.Constants.BACKGROUND_IMAGE_URLS;


public class DetailsListFragment extends Fragment {
    private static final String TAG = DetailsListFragment.class.getSimpleName();
    private static final boolean DEBUG = false;

    private static final String ARG_ALBUM_IMAGE_POSITION = "arg_album_image_position";
    private static final String ARG_STARTING_ALBUM_IMAGE_POSITION = "arg_starting_album_image_position";

    private final Callback mImageCallback = new Callback() {
        @Override
        public void onSuccess() {
            startPostponedEnterTransition();
        }

        @Override
        public void onError() {
            startPostponedEnterTransition();
        }
    };

    private ImageView mAlbumImage;
    private int mStartingPosition;
    private int mAlbumPosition;
    private boolean mIsTransitioning;
    private long mBackgroundImageFadeMillis;

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

        RecyclerView recyclerView = (RecyclerView) rootView;

        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setAdapter(new DetailsAdapter());


    }

    private void startPostponedEnterTransition() {
        if (mAlbumPosition == mStartingPosition) {
            mAlbumImage.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mAlbumImage.getViewTreeObserver().removeOnPreDrawListener(this);
                    getActivity().startPostponedEnterTransition();
                    return true;
                }
            });
        }
    }

    /**
     * Returns the shared element that should be transitioned back to the previous Activity,
     * or null if the view is not visible on the screen.
     */
    @Nullable
    ImageView getAlbumImage() {
        if (isViewInBounds(getActivity().getWindow().getDecorView(), mAlbumImage)) {
            return mAlbumImage;
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


    private class DetailsAdapter extends RecyclerView.Adapter<ListViewHolder> {

        private static final int ITEM_COUNT = 2;
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_CONTENT = 1;


        @Override
        public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if (viewType == TYPE_HEADER) {
                return new ViewHolderHeader(inflater.inflate(R.layout.list_item_header, parent, false));
            } else if (viewType == TYPE_CONTENT) {
                return new ViewHolderContent(inflater.inflate(R.layout.list_item_content, parent, false));
            }

            return null;
        }

        @Override
        public void onBindViewHolder(ListViewHolder holder, int position) {
            holder.bind();
        }

        @Override
        public int getItemCount() {
            return ITEM_COUNT;
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ?
                    TYPE_HEADER :
                    TYPE_CONTENT;
        }
    }

    private class ViewHolderHeader extends ListViewHolder {
        ImageView backgroundImage;

        ViewHolderHeader(View itemView) {
            super(itemView);
            mAlbumImage = (ImageView) itemView.findViewById(R.id.details_album_image);
            backgroundImage = (ImageView) itemView.findViewById(R.id.details_background_image);
        }

        void bind() {

            String albumImageUrl = ALBUM_IMAGE_URLS[mAlbumPosition];
            String backgroundImageUrl = BACKGROUND_IMAGE_URLS[mAlbumPosition];

            RequestCreator albumImageRequest = Picasso.with(getActivity()).load(albumImageUrl);
            RequestCreator backgroundImageRequest = Picasso.with(getActivity()).load(backgroundImageUrl).fit().centerCrop();

            if (mIsTransitioning) {
                albumImageRequest.noFade();
                backgroundImageRequest.noFade();
                backgroundImage.setAlpha(0f);
                getActivity().getWindow().getSharedElementEnterTransition().addListener(new TransitionListenerAdapter() {
                    @Override
                    public void onTransitionEnd(Transition transition) {
                        backgroundImage.animate().setDuration(mBackgroundImageFadeMillis).alpha(1f);
                    }
                });
            }

            albumImageRequest.into(mAlbumImage, mImageCallback);
            backgroundImageRequest.into(backgroundImage);
        }
    }

    private class ViewHolderContent extends ListViewHolder {
        TextView albumTitleText;

        ViewHolderContent(View itemView) {
            super(itemView);
            View textContainer = itemView.findViewById(R.id.details_text_container);
            albumTitleText = (TextView) textContainer.findViewById(R.id.details_album_title);
        }

        void bind() {
            String albumName = ALBUM_NAMES[mAlbumPosition];

            albumTitleText.setText(albumName);
            mAlbumImage.setTransitionName(albumName);

        }
    }

    private abstract class ListViewHolder extends RecyclerView.ViewHolder {
        ListViewHolder(View itemView) {
            super(itemView);
        }

        abstract void bind();
    }
}
