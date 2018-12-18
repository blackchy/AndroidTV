package com.example.core.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

public class V7StaggeredGridLayoutManager extends StaggeredGridLayoutManager {
    public V7StaggeredGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
    }

    public V7StaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    @Override
    public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate, boolean focusedChildVisible) {
        if(parent instanceof RecyclerViewTV) {
            return parent.requestChildRectangleOnScreen(child, rect, immediate);
        }
        return super.requestChildRectangleOnScreen(parent, child, rect, immediate, focusedChildVisible);
    }
}
