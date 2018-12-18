package com.example.core.player;

/**
 * 视频播放器管理器.
 */
public class VideoViewManager {

    private VideoView mPlayer;

    private VideoViewManager() {
    }

    private static VideoViewManager sInstance;

    public static VideoViewManager instance() {
        if (sInstance == null) {
            synchronized (VideoViewManager.class) {
                if (sInstance == null) {
                    sInstance = new VideoViewManager();
                }
            }
        }
        return sInstance;
    }

    public void setCurrentVideoPlayer(VideoView player) {
        mPlayer = player;
    }

    public VideoView getCurrentVideoPlayer() {
        return mPlayer;
    }

    public void release() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void start() {
        if (mPlayer != null && !mPlayer.isPlaying()) {
            mPlayer.start();
        }
    }

    public boolean isPlaying() {
        if (mPlayer != null) {
            return mPlayer.isPlaying();
        }
        return false;
    }

    public void stopPlayback() {
        if (mPlayer != null) {
            mPlayer.stopPlayback();
        }
    }

    public void pause() {
        if (mPlayer != null) {
            mPlayer.pause();
        }
    }

    public void resume() {
        if (mPlayer != null) {
            mPlayer.resume();
        }
    }

    public boolean isFullScreen() {
        return mPlayer != null && mPlayer.isFullScreen();
    }
}
