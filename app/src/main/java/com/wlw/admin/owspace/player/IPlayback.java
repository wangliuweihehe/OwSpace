package com.wlw.admin.owspace.player;

import javax.security.auth.callback.Callback;

public interface IPlayback {
    /**
     * @return 是否播放
     */
    boolean play();

    /**
     * @param song 音频路径
     * @return 是否播放
     */
    boolean play(String song);

    /**
     * 暂停
     */
    boolean pause();

    /**
     * 是否播放中
     *
     * @return boolean
     */
    boolean isPlaying();

    /**
     * 获取进度
     *
     * @return int
     */
    int getProgress();

    /**
     * 持续时间
     *
     * @return int
     */
    int getDuration();

    /**
     * 跳转进度
     *
     * @param progress 目标进度
     * @return boolean
     */
    boolean seekTo(int progress);

    /**
     * 注册接口
     *
     * @param callback callback 接口
     */
    void registerCallback(Callback callback);

    /**
     * 注销接口
     *
     * @param callback 接口
     */
    void unRegisterCallback(Callback callback);

    /**
     *
     */
    void removeCallbacks();

    /**
     * 释放播放器
     */
    void releasePlayer();

    interface Callback {
        /**
         * 播放完毕
         */
    void onComplete(PlayState state);

    /**
     * 播放状态改变
     */
    void onPlayStatusChanged(PlayState status);

    /**
     * 播放进度位置
     *
     * @param position 进度
     */
    void onPosition(int position);
}
}
