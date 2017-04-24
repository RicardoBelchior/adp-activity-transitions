package com.alexjlockwood.activity.transitions;

import android.app.Activity;
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

class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ListViewHolder> {

    private static final int ITEM_COUNT = 5;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_CONTENT = 1;
    private static final int TYPE_LOADING = 2;


    private ImageView mAlbumImage;


    private final Activity activity;
    private final int mStartingPosition;
    private final int mAlbumPosition;
    private final boolean mIsTransitioning;
    private final long mBackgroundImageFadeMillis;

    private boolean backgroundLoaded;
    private boolean fetchingData;

    DetailsAdapter(Activity activity, int mStartingPosition, int mAlbumPosition, boolean mIsTransitioning, long mBackgroundImageFadeMillis) {
        this.activity = activity;
        this.mStartingPosition = mStartingPosition;
        this.mAlbumPosition = mAlbumPosition;
        this.mIsTransitioning = mIsTransitioning;
        this.mBackgroundImageFadeMillis = mBackgroundImageFadeMillis;

        this.backgroundLoaded = !mIsTransitioning;
        this.fetchingData = true;

        setHasStableIds(true);
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_HEADER) {
            return new ViewHolderHeader(inflater.inflate(R.layout.list_item_header, parent, false));
        } else if (viewType == TYPE_CONTENT) {
            return new ViewHolderContent(inflater.inflate(R.layout.list_item_content, parent, false));
        } else if (viewType == TYPE_LOADING) {
            return new ViewHolderLoading(inflater.inflate(R.layout.list_item_loading, parent, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return fetchingData ? 2 : ITEM_COUNT;
    }

    @Override
    public int getItemViewType(int position) {

        //Log.i("TAG", String.format("pos: %s, size=%s", position, getItemCount()));
        if (position < 0 || position >= getItemCount()) {
            throw new IllegalArgumentException("given position does not exist !");
        }

        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == 1) {
            return fetchingData ? TYPE_LOADING : TYPE_CONTENT;
        } else {
            return TYPE_CONTENT;
        }
    }

    public void setFetchingData(boolean fetchingData) {
        this.fetchingData = fetchingData;
        //Log.d("TAG", "fetchingData = " + fetchingData);
    }

    private class ViewHolderHeader extends ListViewHolder {
        ImageView backgroundImage;

        ViewHolderHeader(View itemView) {
            super(itemView);
            mAlbumImage = (ImageView) itemView.findViewById(R.id.details_album_image);
            backgroundImage = (ImageView) itemView.findViewById(R.id.details_background_image);
        }

        void bind() {
            String albumName = ALBUM_NAMES[mAlbumPosition];
            mAlbumImage.setTransitionName(albumName);

            String albumImageUrl = ALBUM_IMAGE_URLS[mAlbumPosition];
            String backgroundImageUrl = BACKGROUND_IMAGE_URLS[mAlbumPosition];

            RequestCreator albumImageRequest = Picasso.with(activity).load(albumImageUrl);
            RequestCreator backgroundImageRequest = Picasso.with(activity).load(backgroundImageUrl).fit().centerCrop();

            if (mIsTransitioning && !backgroundLoaded) {
                albumImageRequest.noFade();
                backgroundImageRequest.noFade();
                backgroundImage.setAlpha(0f);
                activity.getWindow().getSharedElementEnterTransition().addListener(new TransitionListenerAdapter() {
                    @Override
                    public void onTransitionEnd(Transition transition) {
                        backgroundImage.animate().setDuration(mBackgroundImageFadeMillis).alpha(1f);
                        backgroundLoaded = true;
                    }
                });
            }

            albumImageRequest.into(mAlbumImage, mImageCallback);
            backgroundImageRequest.into(backgroundImage);
        }
    }

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

    private void startPostponedEnterTransition() {
        if (mAlbumPosition == mStartingPosition) {
            mAlbumImage.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mAlbumImage.getViewTreeObserver().removeOnPreDrawListener(this);
                    activity.startPostponedEnterTransition();
                    return true;
                }
            });
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
        }
    }

    private class ViewHolderLoading extends ListViewHolder {

        ViewHolderLoading(View itemView) {
            super(itemView);
        }

        @Override
        void bind() {
        }
    }

    abstract class ListViewHolder extends RecyclerView.ViewHolder {
        ListViewHolder(View itemView) {
            super(itemView);
        }

        abstract void bind();
    }

    public ImageView getAlbumImage() {
        return mAlbumImage;
    }

}