package com.example.core.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;
import java.util.ArrayList;
import java.util.List;

public class VerticalBanner extends ViewFlipper {
    private List<View> viewList = new ArrayList<>();
    private OnChildViewSelectedListener selectedListener;

    public VerticalBanner(Context context) {
        this(context, null);
    }

    public VerticalBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(List<View> viewList) {
        if (viewList == null || viewList.size() <= 0) {
            return;
        }
        removeAllViews();
        this.viewList.clear();
        this.viewList.addAll(viewList);
        for (View view : viewList) {
            addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }
        if (viewList.size() > 1) {
            startFlipping();
        }
    }

    @Override
    public void showNext() {
        super.showNext();
        if (selectedListener != null) {
            selectedListener.onSelected(viewList.get(getDisplayedChild()), getDisplayedChild());
        }
    }

    public void setOnChildViewSelectedListener(OnChildViewSelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }

    public interface OnChildViewSelectedListener {
        /**
         * 当前显示页面
         *
         * @param view
         * @param position
         */
        void onSelected(View view, int position);
    }
}
