package com.alexjlockwood.activity.transitions;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


public class TeadsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private DetailsAdapter originalAdapter;

    public TeadsAdapter(DetailsAdapter originalAdapter) {
        this.originalAdapter = originalAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return originalAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DetailsAdapter.ListViewHolder) {
            originalAdapter.onBindViewHolder((DetailsAdapter.ListViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return originalAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return originalAdapter.getItemViewType(position);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof DetailsAdapter.ListViewHolder) {
            originalAdapter.onViewAttachedToWindow((DetailsAdapter.ListViewHolder) holder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof DetailsAdapter.ListViewHolder) {
            originalAdapter.onViewDetachedFromWindow((DetailsAdapter.ListViewHolder) holder);
        }
    }
}