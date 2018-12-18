package com.example.core.player;

public class PlayerConfig {

    public boolean isLooping;//是否循环播放
    public boolean addToPlayerManager;//是否添加到播放管理器
    public AbstractPlayer mAbstractPlayer = null;//自定义播放核心
    public boolean disableAudioFocus;//关闭AudioFocus监听
    public boolean savingProgress;//保存进度
    public long createTime;

    private PlayerConfig(PlayerConfig origin) {
        this.isLooping = origin.isLooping;
        this.addToPlayerManager = origin.addToPlayerManager;
        this.mAbstractPlayer = origin.mAbstractPlayer;
        this.disableAudioFocus = origin.disableAudioFocus;
        this.savingProgress = origin.savingProgress;
        this.createTime = System.currentTimeMillis();
    }

    private PlayerConfig() {

    }

    public static class Builder {

        private PlayerConfig target;

        public Builder() {
            target = new PlayerConfig();
        }


        /**
         * 添加到{@link VideoViewManager},如需集成到RecyclerView或ListView请开启此选项
         */
        public Builder addToPlayerManager() {
            target.addToPlayerManager = true;
            return this;
        }

        /**
         * 开启循环播放
         */
        public Builder setLooping() {
            target.isLooping = true;
            return this;
        }
        /**
         * 设置自定义播放核心
         */
        public Builder setCustomMediaPlayer(AbstractPlayer abstractPlayer) {
            target.mAbstractPlayer = abstractPlayer;
            return this;
        }

        /**
         * 关闭AudioFocus监听
         */
        public Builder disableAudioFocus() {
            target.disableAudioFocus = true;
            return this;
        }
        public Builder savingProgress() {
            target.savingProgress = true;
            return this;
        }

        public PlayerConfig build() {
            return new PlayerConfig(target);
        }
    }
}
