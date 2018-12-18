package com.example.core.player;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import java.util.Locale;

public class VideoView extends BaseVideoView {
    protected ResizeSurfaceView mSurfaceView;
    protected FrameLayout playerContainer;
    protected boolean isFullScreen;//是否处于全屏状态

    public static final int SCREEN_SCALE_DEFAULT = 0;
    public static final int SCREEN_SCALE_16_9 = 1;
    public static final int SCREEN_SCALE_4_3 = 2;
    public static final int SCREEN_SCALE_MATCH_PARENT = 3;
    public static final int SCREEN_SCALE_ORIGINAL = 4;
    public static final int SCREEN_SCALE_CENTER_CROP = 5;

    protected int mCurrentScreenScale = SCREEN_SCALE_DEFAULT;

    public VideoView(@NonNull Context context) {
        this(context, null);
    }

    public VideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化播放器视图
     */
    protected void initView() {
        playerContainer = new FrameLayout(getContext());
        playerContainer.setBackgroundColor(Color.BLACK);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(playerContainer, params);
    }

    /**
     * 创建播放器实例，设置播放地址及播放器参数
     */
    @Override
    protected void initPlayer() {
        super.initPlayer();
        addDisplay();
    }

    protected void addDisplay() {
        addSurfaceView();
    }

    /**
     * 向Controller设置播放状态，用于控制Controller的ui展示
     */
    @Override
    protected void setPlayState(int playState) {
        mCurrentPlayState = playState;
        if (mOnVideoViewStateChangeListeners != null) {
            if (mOnVideoViewStateChangeListeners.containsKey(mPlayerConfig.createTime)) {
                mOnVideoViewStateChangeListeners.get(mPlayerConfig.createTime).onPlayStateChanged(playState);
            }
        }
    }

    /**
     * 向Controller设置播放器状态，包含全屏状态和非全屏状态
     */
    @Override
    protected void setFullState(int fullState) {
        mCurrentFullState = fullState;
        if (mOnVideoViewStateChangeListeners != null) {
            if (mOnVideoViewStateChangeListeners != null) {
                if (mOnVideoViewStateChangeListeners.containsKey(mPlayerConfig.createTime)) {
                    mOnVideoViewStateChangeListeners.get(mPlayerConfig.createTime).onFullStateChanged(fullState);
                }
            }
        }
    }

    @Override
    protected void startPlay() {
        if (mPlayerConfig.addToPlayerManager) {
            VideoViewManager.instance().release();
            VideoViewManager.instance().setCurrentVideoPlayer(this);
        }
        super.startPlay();
    }

    /**
     * 添加SurfaceView
     */
    private void addSurfaceView() {
        playerContainer.removeView(mSurfaceView);
        mSurfaceView = new ResizeSurfaceView(getContext());
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (mMediaPlayer != null) {
                    mMediaPlayer.setDisplay(holder);
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });
        surfaceHolder.setFormat(PixelFormat.RGBA_8888);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        playerContainer.addView(mSurfaceView, 0, params);
    }


    @Override
    public void release() {
        super.release();
        playerContainer.removeView(mSurfaceView);
        mCurrentScreenScale = SCREEN_SCALE_DEFAULT;
    }

    /**
     * 进入全屏
     */
    @Override
    public void startFullScreen() {
        this.removeView(playerContainer);
        setFullState(PLAYER_FULL_SCREEN);
        Activity activity = scanForActivity(getContext());
        ViewGroup contentView = (ViewGroup) activity.getWindow().getDecorView();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.addView(playerContainer, params);
        isFullScreen = true;
    }

    /**
     * 退出全屏
     */
    @Override
    public void stopFullScreen() {
        setFullState(PLAYER_NORMAL);
        Activity activity = scanForActivity(getContext());
        ViewGroup contentView = (ViewGroup) activity.getWindow().getDecorView();
        contentView.removeView(playerContainer);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(playerContainer, params);
        this.requestFocus();
        isFullScreen = false;
    }

    /**
     * 判断是否处于全屏状态
     */
    @Override
    public boolean isFullScreen() {
        return isFullScreen;
    }

    /**
     * 重试
     */
    @Override
    public void retry() {
        addDisplay();
        startPrepare(true);
    }

    @Override
    public void onInfo(int what, int extra) {
        super.onInfo(what, extra);
    }

    @Override
    public void onVideoSizeChanged(int videoWidth, int videoHeight) {
        mSurfaceView.setScreenScale(mCurrentScreenScale);
        //mSurfaceView.setVideoSize(videoWidth, videoHeight);
    }

    /**
     * 设置视频比例
     */
    @Override
    public void setScreenScale(int screenScale) {
        this.mCurrentScreenScale = screenScale;
        if (mSurfaceView != null) {
            mSurfaceView.setScreenScale(screenScale);
        }
    }

    public String getStringTime(long position) {
        int totalSeconds = (int) (position / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        if (hours > 0) {
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds);
        }
    }
}
