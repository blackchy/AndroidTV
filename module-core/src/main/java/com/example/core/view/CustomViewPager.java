package com.example.core.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class CustomViewPager extends ViewPager {

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 禁止viewpager里面内容导致页面切换
     * @param event
     * @return
     */
    @Override
    public boolean executeKeyEvent(KeyEvent event) {
        return false;
    }
}
