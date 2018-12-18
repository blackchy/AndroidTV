package com.example.core.player;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import java.util.LinkedHashMap;
import java.util.Map;


public abstract class BaseVideoView extends FrameLayout implements MediaPlayerControl, PlayerEventListener {
    protected AbstractPlayer mMediaPlayer;//播放器
    protected String mCurrentUrl;//当前播放视频的地址
    protected Map<String, String> mHeaders;//当前视频地址的请求头
    protected AssetFileDescriptor mAssetFileDescriptor;//assets文件
    protected long mCurrentPosition;//当前正在播放视频的位置
    //播放器的各种状态
    public static final int STATE_ERROR = -1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_PREPARING = 1;
    public static final int STATE_PREPARED = 2;
    public static final int STATE_PLAYING = 3;
    public static final int STATE_PAUSED = 4;
    public static final int STATE_PLAYBACK_COMPLETED = 5;
    public static final int STATE_BUFFERING = 6;
    public static final int STATE_BUFFERED = 7;
    protected int mCurrentPlayState = STATE_IDLE;//当前播放器的状态

    public static final int PLAYER_NORMAL = 10;        // 普通播放器
    public static final int PLAYER_FULL_SCREEN = 11;   // 全屏播放器
    protected int mCurrentFullState = PLAYER_NORMAL;
    protected AudioManager mAudioManager;//系统音频管理器
    @Nullable
    protected AudioFocusHelper mAudioFocusHelper;
    protected PlayerConfig mPlayerConfig;//播放器配置
    public LinkedHashMap<Long, OnVideoViewStateChangeListener> mOnVideoViewStateChangeListeners;

    public BaseVideoView(@NonNull Context context) {
        this(context, null);
    }

    public BaseVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPlayerConfig = new PlayerConfig.Builder().build();
    }

    /**
     * 初始化播放器
     */
    protected void initPlayer() {
        if (mMediaPlayer == null) {
            if (mPlayerConfig.mAbstractPlayer != null) {
                mMediaPlayer = mPlayerConfig.mAbstractPlayer;
            } else {
                mMediaPlayer = new AndroidMediaPlayer(getContext());
            }
            mMediaPlayer.bindVideoView(this);
            mMediaPlayer.initPlayer();
            mMediaPlayer.setLooping(mPlayerConfig.isLooping);
        }
    }

    protected abstract void setPlayState(int playState);

    protected abstract void setFullState(int fullState);

    /**
     * 开始准备播放（直接播放）
     */
    protected void startPrepare(boolean needReset) {
        if (TextUtils.isEmpty(mCurrentUrl) && mAssetFileDescriptor == null) {
            return;
        }
        if (needReset) {
            mMediaPlayer.reset();
        }
        if (mAssetFileDescriptor != null) {
            mMediaPlayer.setDataSource(mAssetFileDescriptor);
        } else {
            mMediaPlayer.setDataSource(Uri.parse(mCurrentUrl), mHeaders);
        }
        mMediaPlayer.prepareAsync();
        setPlayState(STATE_PREPARING);
        setFullState(isFullScreen() ? PLAYER_FULL_SCREEN : PLAYER_NORMAL);
    }

    /**
     * 开始播放
     */
    @Override
    public void start() {
        if (mCurrentPlayState == STATE_IDLE) {
            Log.d("mCurrentPlayState", "mCurrentPlayState");
            startPlay();
        } else if (isInPlaybackState()) {
            startInPlaybackState();
        }
        setKeepScreenOn(true);
        if (mAudioFocusHelper != null) {
            mAudioFocusHelper.requestFocus();
        }
    }

    /**
     * 第一次播放
     */
    protected void startPlay() {
        if (!mPlayerConfig.disableAudioFocus) {
            mAudioManager = (AudioManager) getContext().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            mAudioFocusHelper = new AudioFocusHelper();
        }
        if (mPlayerConfig.savingProgress) {
            mCurrentPosition = ProgressUtil.getSavedProgress(mPlayerConfig.createTime);
        }
        initPlayer();
        startPrepare(false);
    }

    /**
     * 播放状态下开始播放
     */
    protected void startInPlaybackState() {
        mMediaPlayer.start();
        setPlayState(STATE_PLAYING);
    }

    /**
     * 暂停播放
     */
    @Override
    public void pause() {
        if (isPlaying()) {
            mMediaPlayer.pause();
            setPlayState(STATE_PAUSED);
            setKeepScreenOn(false);
            if (mAudioFocusHelper != null) {
                mAudioFocusHelper.abandonFocus();
            }
        }
    }

    /**
     * 继续播放
     */
    public void resume() {
        if (isInPlaybackState() && !mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
            setPlayState(STATE_PLAYING);
            if (mAudioFocusHelper != null) {
                mAudioFocusHelper.requestFocus();
            }
            setKeepScreenOn(true);
        }
    }

    /**
     * 停止播放
     */
    public void stopPlayback() {
        if (mPlayerConfig.savingProgress && isInPlaybackState()) {
            ProgressUtil.saveProgress(mPlayerConfig.createTime, getCurrentPosition());
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            setPlayState(STATE_IDLE);
            if (mAudioFocusHelper != null) {
                mAudioFocusHelper.abandonFocus();
            }
            setKeepScreenOn(false);
        }
        onPlayStopped();
    }

    /**
     * 释放播放器
     */
    public void release() {
        if (mPlayerConfig.savingProgress && isInPlaybackState()) {
            ProgressUtil.saveProgress(mPlayerConfig.createTime, getCurrentPosition());
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            setPlayState(STATE_IDLE);
            if (mAudioFocusHelper != null) {
                mAudioFocusHelper.abandonFocus();
            }
            setKeepScreenOn(false);
        }
        onPlayStopped();
    }

    private void onPlayStopped() {
        mCurrentPosition = 0;
    }

    /**
     * 监听播放状态变化
     */
    public void addOnVideoViewStateChangeListener(@NonNull OnVideoViewStateChangeListener listener) {
        if (mOnVideoViewStateChangeListeners == null) {
            mOnVideoViewStateChangeListeners = new LinkedHashMap<>();
        }
        mOnVideoViewStateChangeListeners.put(mPlayerConfig.createTime, listener);
    }

    /**
     * 移除播放状态监听
     */
    public void removeOnVideoViewStateChangeListener(@NonNull OnVideoViewStateChangeListener listener) {
        if (mOnVideoViewStateChangeListeners != null) {
            mOnVideoViewStateChangeListeners.remove(mPlayerConfig.createTime);
        }
    }

    /**
     * 移除所有播放状态监听
     */
    public void clearOnVideoViewStateChangeListeners() {
        if (mOnVideoViewStateChangeListeners != null) {
            mOnVideoViewStateChangeListeners.clear();
        }
    }

    /**
     * 是否处于播放状态
     */
    protected boolean isInPlaybackState() {
        return (mMediaPlayer != null
                && mCurrentPlayState != STATE_ERROR
                && mCurrentPlayState != STATE_IDLE
                && mCurrentPlayState != STATE_PREPARING
                && mCurrentPlayState != STATE_PLAYBACK_COMPLETED);
    }

    /**
     * 获取视频总时长
     */
    @Override
    public long getDuration() {
        if (isInPlaybackState()) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    /**
     * 获取当前播放的位置
     */
    @Override
    public long getCurrentPosition() {
        if (isInPlaybackState()) {
            mCurrentPosition = mMediaPlayer.getCurrentPosition();
            return mCurrentPosition;
        }
        return 0;
    }

    /**
     * 调整播放进度
     */
    @Override
    public void seekTo(long pos) {
        if (isInPlaybackState()) {
            mMediaPlayer.seekTo(pos);
        }
    }

    /**
     * 是否处于播放状态
     */
    @Override
    public boolean isPlaying() {
        return isInPlaybackState() && mMediaPlayer.isPlaying();
    }

    /**
     * 获取当前缓冲百分比
     */
    @Override
    public int getBufferPercentage() {
        return mMediaPlayer != null ? mMediaPlayer.getBufferedPercentage() : 0;
    }


    /**
     * 视频播放出错回调
     */
    @Override
    public void onError() {
        setPlayState(STATE_ERROR);
    }

    /**
     * 视频播放完成回调
     */
    @Override
    public void onCompletion() {
        setPlayState(STATE_PLAYBACK_COMPLETED);
//        setKeepScreenOn(false);
        mCurrentPosition = 0;
    }

    @Override
    public void onInfo(int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                setPlayState(STATE_BUFFERING);
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                setPlayState(STATE_BUFFERED);
                break;
            case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: // 视频开始渲染
                setPlayState(STATE_PLAYING);
                if (getWindowVisibility() != VISIBLE) pause();
                break;
        }
    }

    /**
     * 视频缓冲完毕，准备开始播放时回调
     */
    @Override
    public void onPrepared() {
        setPlayState(STATE_PREPARED);
        if (mCurrentPosition > 0) {
            seekTo(mCurrentPosition);
        }
    }

    public void setPlayerConfig(PlayerConfig config) {
        this.mPlayerConfig = config;
    }

    /**
     * 获取当前播放器的状态
     */
    public int getCurrentPlayerState() {
        return mCurrentFullState;
    }

    /**
     * 获取当前的播放状态
     */
    public int getCurrentPlayState() {
        return mCurrentPlayState;
    }

    /**
     * 重新播放
     */
    @Override
    public void refresh() {
        mCurrentPosition = 0;
        retry();
    }

    /**
     * 设置视频地址
     */
    public void setVideoPath(String url) {
        this.mCurrentUrl = url;
    }

    /**
     * 设置包含请求头信息的视频地址
     *
     * @param url     视频地址
     * @param headers 请求头
     */
    public void setUrl(String url, Map<String, String> headers) {
        mCurrentUrl = url;
        mHeaders = headers;
    }

    /**
     * 用于播放assets里面的视频文件
     */
    public void setAssetFileDescriptor(AssetFileDescriptor fd) {
        this.mAssetFileDescriptor = fd;
    }

    /**
     * 一开始播放就seek到预先设置好的位置
     */
    public void skipPositionWhenPlay(int position) {
        this.mCurrentPosition = position;
    }

    /**
     * 设置音量 0.0f-1.0f 之间
     *
     * @param v1 左声道音量
     * @param v2 右声道音量
     */
    public void setVolume(float v1, float v2) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setVolume(v1, v2);
        }
    }

    public int getVolume() {
        if (mAudioManager != null) {
            return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        } else {
            return -1;
        }
    }

    public int getMaxVolume() {
        if (mAudioManager != null) {
            return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        } else {
            return -1;
        }
    }

    public Activity scanForActivity(Context context) {
        return context == null ? null : (context instanceof Activity ? (Activity) context : (context instanceof ContextWrapper ? scanForActivity(((ContextWrapper) context).getBaseContext()) : null));
    }

    /**
     * 音频焦点改变监听
     */
    private class AudioFocusHelper implements AudioManager.OnAudioFocusChangeListener {
        boolean startRequested = false;
        boolean pausedForLoss = false;
        int currentFocus = 0;

        @Override
        public void onAudioFocusChange(int focusChange) {
            if (currentFocus == focusChange) {
                return;
            }
            currentFocus = focusChange;
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN://获得焦点
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT://暂时获得焦点
                    if (startRequested || pausedForLoss) {
                        start();
                        startRequested = false;
                        pausedForLoss = false;
                    }
                    if (mMediaPlayer != null)//恢复音量
                        mMediaPlayer.setVolume(1.0f, 1.0f);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS://焦点丢失
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT://焦点暂时丢失
                    if (isPlaying()) {
                        pausedForLoss = true;
                        pause();
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK://此时需降低音量
                    if (mMediaPlayer != null && isPlaying()) {
                        mMediaPlayer.setVolume(0.1f, 0.1f);
                    }
                    break;
            }
        }

        /**
         * Requests to obtain the audio focus
         *
         * @return True if the focus was granted
         */
        boolean requestFocus() {
            if (currentFocus == AudioManager.AUDIOFOCUS_GAIN) {
                return true;
            }

            if (mAudioManager == null) {
                return false;
            }

            int status = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == status) {
                currentFocus = AudioManager.AUDIOFOCUS_GAIN;
                return true;
            }

            startRequested = true;
            return false;
        }

        /**
         * Requests the system to drop the audio focus
         *
         * @return True if the focus was lost
         */
        boolean abandonFocus() {

            if (mAudioManager == null) {
                return false;
            }

            startRequested = false;
            int status = mAudioManager.abandonAudioFocus(this);
            return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == status;
        }
    }
}
