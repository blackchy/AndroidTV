package com.example.core.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import com.example.core.R;
import java.util.ArrayList;
import java.util.List;

public class ShimmerLayout extends FrameLayout implements View.OnFocusChangeListener {
    protected long mAnimDuration = 300;
    protected long mShimmerDelay = mAnimDuration + 100;
    private int mShimmerColor = 0x66FFFFFF;
    private boolean mIsShimmerAnim = false;
    private boolean mIsBounceInterpolator = false;
    private boolean mBringToFront = false;
    private boolean mSelfFocus = false;
    private float mScale = 1.05f;
    private LinearGradient mShimmerLinearGradient;
    private Matrix mShimmerGradientMatrix;
    private Paint mShimmerPaint;
    protected RectF mFrameRectF;
    private float mShimmerTranslate = 0;
    private boolean mShimmerAnimating = false;
    private ViewTreeObserver.OnPreDrawListener startAnimationPreDrawListener;
    private AnimatorSet mAnimatorSet;

    public ShimmerLayout(Context context) {
        this(context, null);
    }

    public ShimmerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShimmerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setWillNotDraw(false);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ShimmerLayout, 0, 0);
        try {
            mAnimDuration = a.getInteger(R.styleable.ShimmerLayout_mAnimDuration, 300);
            mShimmerColor = a.getColor(R.styleable.ShimmerLayout_mShimmerColor, 0x66FFFFFF);
            mIsShimmerAnim = a.getBoolean(R.styleable.ShimmerLayout_mIsShimmerAnim, false);
            mBringToFront = a.getBoolean(R.styleable.ShimmerLayout_mBringToFront, false);
            mSelfFocus = a.getBoolean(R.styleable.ShimmerLayout_mSelfFocus, false);
            mIsBounceInterpolator = a.getBoolean(R.styleable.ShimmerLayout_mIsBounceInterpolator, true);
            mScale = a.getFloat(R.styleable.ShimmerLayout_mScale, 1.05f);
            mShimmerDelay = mAnimDuration + 100;
        } finally {
            a.recycle();
        }
        if(mSelfFocus){
            setOnFocusChangeListener(this);
        }
        //关闭硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mShimmerPaint = new Paint();
        mShimmerGradientMatrix = new Matrix();
        mFrameRectF = new RectF();
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        final int width = getWidth();
        final int height = getHeight();
        mFrameRectF.set(getPaddingLeft(), getPaddingTop(), width - getPaddingRight(), height - getPaddingBottom());
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        onDrawShimmer(canvas);
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimation();
        super.onDetachedFromWindow();
    }

    /**
     * 绘制闪光
     *
     * @param canvas
     */
    protected void onDrawShimmer(Canvas canvas) {
        if (mShimmerAnimating) {
            canvas.save();
            float shimmerTranslateX = mFrameRectF.width() * mShimmerTranslate;
            float shimmerTranslateY = mFrameRectF.height() * mShimmerTranslate;
            mShimmerGradientMatrix.setTranslate(shimmerTranslateX, shimmerTranslateY);
            mShimmerLinearGradient.setLocalMatrix(mShimmerGradientMatrix);
            canvas.drawRoundRect(mFrameRectF, 0, 0, mShimmerPaint);
            canvas.restore();
        }
    }

    private void setShimmerAnimating(boolean shimmerAnimating) {
        mShimmerAnimating = shimmerAnimating;
        if (mShimmerAnimating) {
            mShimmerLinearGradient = new LinearGradient(
                    0, 0, mFrameRectF.width(), mFrameRectF.height(),
                    new int[]{0x00FFFFFF, reduceColorAlphaValueToZero(mShimmerColor), mShimmerColor, reduceColorAlphaValueToZero(mShimmerColor), 0x00FFFFFF},
                    new float[]{0f, 0.2f, 0.5f, 0.8f, 1f}, Shader.TileMode.CLAMP);
            mShimmerPaint.setShader(mShimmerLinearGradient);
        }
    }

    private int reduceColorAlphaValueToZero(int actualColor) {
        return Color.argb(0x1A, Color.red(actualColor), Color.green(actualColor), Color.blue(actualColor));
    }

    public void startAnimation() {
        if (getWidth() == 0) {
            startAnimationPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    getViewTreeObserver().removeOnPreDrawListener(this);
                    startAnimation();
                    return true;
                }
            };
            getViewTreeObserver().addOnPreDrawListener(startAnimationPreDrawListener);
            return;
        }
        if (null != mAnimatorSet) {
            mAnimatorSet.cancel();
        }
        createAnimatorSet(true);
        mAnimatorSet.start();
        setSelected(true);
    }

    public void stopAnimation() {
        if (startAnimationPreDrawListener != null) {
            getViewTreeObserver().removeOnPreDrawListener(startAnimationPreDrawListener);
        }
        if (null != mAnimatorSet) {
            mAnimatorSet.cancel();
        }
        createAnimatorSet(false);
        mAnimatorSet.start();
        setSelected(false);
    }

    private void createAnimatorSet(boolean isStart) {
        final List<Animator> together = new ArrayList<>();
        if (isStart) {
            together.add(getScaleXAnimator(mScale));
            together.add(getScaleYAnimator(mScale));
        } else {
            together.add(getScaleXAnimator(1.0f));
            together.add(getScaleYAnimator(1.0f));
        }
        final List<Animator> sequentially = new ArrayList<>();
        if (mIsShimmerAnim && isStart) {
            sequentially.add(getShimmerAnimator());
        }
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(together);
        mAnimatorSet.playSequentially(sequentially);
    }

    private ObjectAnimator getScaleXAnimator(float scale) {
        ObjectAnimator scaleXObjectAnimator = ObjectAnimator.ofFloat(this, "scaleX", scale).setDuration(mAnimDuration);
        if (mIsBounceInterpolator) {
            scaleXObjectAnimator.setInterpolator(new BounceInterpolator());
        }
        return scaleXObjectAnimator;
    }

    private ObjectAnimator getScaleYAnimator(float scale) {
        ObjectAnimator scaleYObjectAnimator = ObjectAnimator.ofFloat(this, "scaleY", scale).setDuration(mAnimDuration);
        if (mIsBounceInterpolator) {
            scaleYObjectAnimator.setInterpolator(new BounceInterpolator());
        }
        return scaleYObjectAnimator;
    }

    private ObjectAnimator getShimmerAnimator() {
        ObjectAnimator mShimmerAnimator = ObjectAnimator.ofFloat(this, "shimmerTranslate", -1f, 1f);
        mShimmerAnimator.setInterpolator(new DecelerateInterpolator(1));
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int max = getWidth() >= getHeight() ? getWidth() : getHeight();
        int duration = max > screenWidth / 3 ? screenWidth / 3 : max;
        mShimmerAnimator.setDuration(duration * 3);
        mShimmerAnimator.setStartDelay(mShimmerDelay);
        mShimmerAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                setShimmerAnimating(true);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setShimmerAnimating(false);
            }
        });
        return mShimmerAnimator;
    }


    protected void setShimmerTranslate(float shimmerTranslate) {
        if (mIsShimmerAnim && mShimmerTranslate != shimmerTranslate) {
            mShimmerTranslate = shimmerTranslate;
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    protected float getShimmerTranslate() {
        return mShimmerTranslate;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (mBringToFront) {
                v.bringToFront();
            }
            v.setSelected(true);
            startAnimation();
        } else {
            v.setSelected(false);
            stopAnimation();
        }
    }
}
