package com.example.core.player;

import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.view.SurfaceHolder;
import java.util.Map;

public abstract class AbstractPlayer {

    protected PlayerEventListener mPlayerEventListener;

    /**
     * 初始化播放器实例
     */
    public abstract void initPlayer();

    public abstract void setVideoPath(String path);

    public abstract void setVideoURI(Uri uri);

    /**
     * 设置播放地址
     *
     * @param uri     播放地址
     * @param headers 播放地址请求头
     */
    public abstract void setDataSource(Uri uri, Map<String, String> headers);

    /**
     * 用于播放raw和asset里面的视频文件
     */
    public abstract void setDataSource(AssetFileDescriptor fd);

    /**
     * 播放
     */
    public abstract void start();

    /**
     * 暂停
     */
    public abstract void pause();

    /**
     * 停止
     */
    public abstract void stop();

    /**
     * 准备开始播放（异步）
     */
    public abstract void prepareAsync();

    /**
     * 重置播放器
     */
    public abstract void reset();

    /**
     * 是否正在播放
     */
    public abstract boolean isPlaying();

    /**
     * 调整进度
     */
    public abstract void seekTo(long time);

    /**
     * 释放播放器
     */
    public abstract void release();

    /**
     * 获取当前播放的位置
     */
    public abstract long getCurrentPosition();

    /**
     * 获取视频总时长
     */
    public abstract long getDuration();

    /**
     * 获取缓冲百分比
     */
    public abstract int getBufferedPercentage();

    /**
     * 设置渲染视频的View,主要用于SurfaceView
     */
    public abstract void setDisplay(SurfaceHolder holder);

    /**
     * 设置音量
     */
    public abstract void setVolume(float v1, float v2);

    /**
     * 设置是否循环播放
     */
    public abstract void setLooping(boolean isLooping);


    /**
     * 绑定VideoView
     */
    public void bindVideoView(PlayerEventListener playerEventListener) {
        this.mPlayerEventListener = playerEventListener;
    }

}
