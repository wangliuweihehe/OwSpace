package com.wlw.admin.owspace.player;

import android.media.MediaPlayer;

import com.wlw.admin.owspace.utils.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Player implements IPlayback, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private static volatile Player sInstance;
    private MediaPlayer mPlayer;
    private List<Callback> mCallbacks;
    private boolean isPaused;
    private String song;

    private Player() {
        mCallbacks = new ArrayList<>(2);
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnErrorListener(this);
    }

    public static Player getInstance() {
        if (sInstance == null) {
            synchronized (Player.class) {
                if (sInstance == null) {
                    sInstance = new Player();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mPlayer.reset();
        notifyComplete(PlayState.COMPLETE);
    }

    private void notifyComplete(PlayState state) {
        for (Callback callback : mCallbacks) {
            callback.onComplete(state);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        notifyPlayStatusChanged(PlayState.ERROR);
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mPlayer.start();
        notifyPlayStatusChanged(PlayState.PLAYING);
    }

    @Override
    public boolean play() {
        if (isPaused) {
            mPlayer.start();
            notifyPlayStatusChanged(PlayState.PLAYING);
            return true;
        }
        return false;
    }

    @Override
    public boolean play(String song) {
        try {
            mPlayer.reset();
            mPlayer.setDataSource(song);
            mPlayer.prepare();
            this.song = song;
            notifyPlayStatusChanged(PlayState.PLAYING);
            return true;
        } catch (IOException e) {
            notifyPlayStatusChanged(PlayState.ERROR);
            e.printStackTrace();
        }
        return false;
    }

    private void notifyPlayStatusChanged(PlayState state) {
        for (Callback callback : mCallbacks) {
            callback.onPlayStatusChanged(state);
        }
    }

    @Override
    public boolean pause() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            isPaused = true;
            notifyPlayStatusChanged(PlayState.PAUSE);
            return true;
        }
        return false;
    }

    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    @Override
    public int getProgress() {
        Log.e("getProgress", mPlayer.getCurrentPosition() + "");
        return mPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        Log.e("getDuration", mPlayer.getDuration() + "");
        return mPlayer.getDuration();
    }

    @Override
    public boolean seekTo(int progress) {
        mPlayer.seekTo(progress);
        return true;
    }

    @Override
    public void registerCallback(Callback callback) {
        mCallbacks.add(callback);
    }

    @Override
    public void unRegisterCallback(Callback callback) {
        mCallbacks.remove(callback);
    }

    @Override
    public void removeCallbacks() {
        mCallbacks.clear();
    }

    @Override
    public void releasePlayer() {
        mPlayer.reset();
        mPlayer.release();
        mPlayer = null;
        sInstance = null;
        song = null;
    }

    public String getSong() {
        return song;
    }
}
