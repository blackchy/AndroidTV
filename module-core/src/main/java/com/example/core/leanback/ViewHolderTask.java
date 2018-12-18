package com.example.core.leanback;

import android.support.v7.widget.RecyclerView;

/**
 * Interface for schedule task on a ViewHolder.
 */
public interface ViewHolderTask {
    public void run(RecyclerView.ViewHolder viewHolder);
}