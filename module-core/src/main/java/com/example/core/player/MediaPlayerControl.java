package com.example.core.player;
public interface MediaPlayerControl {

    void start();

    void pause();

    long getDuration();

    long getCurrentPosition();

    void seekTo(long pos);

    boolean isPlaying();

    int getBufferPercentage();

    void startFullScreen();

    void stopFullScreen();

    boolean isFullScreen();

    void setScreenScale(int screenScale);

    void retry();

    void refresh();

}