package com.example.core.leanback;

import android.view.View;

public interface OnItemListener<T> {
    /**
     * 获得焦点
     * @param parent
     * @param itemView
     * @param position
     */
    void onItemSelected(T parent, View itemView, int position);
    /**
     * 失去焦点
     * @param parent
     * @param itemView
     * @param position
     */
    void onItemPreSelected(T parent, View itemView, int position);

}
