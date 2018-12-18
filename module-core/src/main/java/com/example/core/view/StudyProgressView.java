package com.example.core.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class StudyProgressView extends ProgressBar {
    private String text = "";
    private Paint mPaint;
    private Rect rect;

    public StudyProgressView(Context context) {
        super(context);
    }

    public StudyProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StudyProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.BLACK);
        this.mPaint.setTextSize(24);
        mPaint.setAntiAlias(true);
        rect = new Rect();
    }
    public void setText(String text) {
        if (TextUtils.isEmpty(text)) {
            text = "";
        }
        this.text = text;
        invalidate();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaint != null && this.text != null && this.rect != null) {
            this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
            int x = (getWidth() / 2) - rect.centerX();
            int y = (getHeight() / 2) - rect.centerY();
            canvas.drawText(this.text, x, y, this.mPaint);
        }
    }
}
