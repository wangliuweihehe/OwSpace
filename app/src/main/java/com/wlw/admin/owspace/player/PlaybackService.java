package com.wlw.admin.owspace.player;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.wlw.admin.owspace.R;
import com.wlw.admin.owspace.utils.Log;
import com.wlw.admin.owspace.view.activity.MainActivity;

/**
 * @author admin
 */
@SuppressLint("Registered")
public class PlaybackService extends Service implements IPlayback.Callback, IPlayback {
    public static final String TAG = PlaybackService.class.getSimpleName();
    public static final String ACTION_PLAY_TOGGLE = "com.wlw.admin.owspace.ACTION_PLAY_TOGGLE";
    public static final String ACTION_PLAY_LAST = "com.wlw.admin.owspace.ACTION_PLAY_LAST";
    public static final String ACTION_PLAY_NEXT = "com.wlw.admin.owspace.ACTION_PLAY_NEXT";
    public static final String ACTION_STOP_SERVICE = "com.wlw.admin.owspace.ACTION_STOP_SERVICE";
    private static final int NOTIFICATION_ID = 1;
    private RemoteViews mContentViewBig, mContentViewSmall;
    private Player mPlayer;
    private final Binder mBinder = new LocalBinder();
    private NotificationManager mNotifyManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        mPlayer = Player.getInstance();
        mPlayer.registerCallback(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_PLAY_TOGGLE.equals(action)) {
                if (isPlaying()) {
                    pause();
                } else {
                    play();
                }
            } else if (ACTION_STOP_SERVICE.equals(action)) {
                if (isPlaying()) {
                    pause();
                }
                stopForeground(true);
                unRegisterCallback(this);
            }
        }
        return START_STICKY;
    }

    @Override
    public boolean stopService(Intent name) {
        Log.e(TAG, "stopService");
        stopForeground(true);
        mNotifyManager.cancel(NOTIFICATION_ID);
        mNotifyManager.cancelAll();
        unRegisterCallback(this);
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "ondestory");
        releasePlayer();

        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onComplete(PlayState state) {
        showNotification(state);
    }

    @Override
    public void onPlayStatusChanged(PlayState status) {
        showNotification(status);
    }

    private void showNotification(PlayState state) {
         mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mNotifyManager.createNotificationChannelGroup(new NotificationChannelGroup("groupID", "groupName"));
            NotificationChannel channel = new NotificationChannel("channel", "channel", NotificationManager.IMPORTANCE_HIGH);
            channel.setGroup("groupID");
            channel.setShowBadge(true);
            channel.setLightColor(Color.RED);
            channel.enableLights(true);
            mNotifyManager.createNotificationChannel(channel);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(this, "channel")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setCustomContentView(getSmallContentView())
                .setOngoing(false);
        mNotifyManager.notify(NOTIFICATION_ID, builder.build());

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            mNotifyManager.deleteNotificationChannel("channel");
//
//        }
    }


    private RemoteViews getSmallContentView() {
        if (mContentViewSmall == null) {
            mContentViewSmall = new RemoteViews(getPackageName(), R.layout.remote_view_music_player_small);
            setUpRemoteView(mContentViewSmall);
        }
        updateRemoteViews(mContentViewSmall);
        return mContentViewSmall;
    }

    private RemoteViews getBigContentView() {
        if (mContentViewBig == null) {
            mContentViewBig = new RemoteViews(getPackageName(), R.layout.remote_view_music_player_small);
            setUpRemoteView(mContentViewBig);
        }
        updateRemoteViews(mContentViewBig);
        return mContentViewBig;
    }

    private void updateRemoteViews(RemoteViews remoteViews) {
        remoteViews.setImageViewResource(R.id.image_view_play_toggle, isPlaying() ? R.drawable.ic_remote_view_pause : R.drawable.ic_remote_view_play);
    }

    private void setUpRemoteView(RemoteViews remoteViews) {
        remoteViews.setImageViewResource(R.id.image_view_close, R.drawable.ic_remote_view_close);
        remoteViews.setImageViewResource(R.id.image_view_play_last, R.drawable.ic_remote_view_play_last);
        remoteViews.setImageViewResource(R.id.image_view_play_next, R.drawable.ic_remote_view_play_next);

        remoteViews.setOnClickPendingIntent(R.id.button_close, getPendingIntent(ACTION_STOP_SERVICE));
        remoteViews.setOnClickPendingIntent(R.id.image_view_play_last, getPendingIntent(ACTION_PLAY_LAST));
        remoteViews.setOnClickPendingIntent(R.id.button_play_toggle, getPendingIntent(ACTION_PLAY_TOGGLE));
        remoteViews.setOnClickPendingIntent(R.id.button_play_next, getPendingIntent(ACTION_PLAY_NEXT));

    }

    private PendingIntent getPendingIntent(String action) {
        return PendingIntent.getService(this, 0, new Intent(action), 0);

    }

    @Override
    public void onPosition(int position) {

    }

    @Override
    public boolean play() {
        return mPlayer.play();
    }

    @Override
    public boolean play(String song) {
        return mPlayer.play(song);
    }

    @Override
    public boolean pause() {
        return mPlayer.pause();
    }

    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    @Override
    public int getProgress() {
        return mPlayer.getProgress();
    }

    @Override
    public int getDuration() {
        return mPlayer.getDuration();
    }

    @Override
    public boolean seekTo(int progress) {
        return mPlayer.seekTo(progress);
    }

    @Override
    public void registerCallback(Callback callback) {
        mPlayer.registerCallback(callback);
    }

    @Override
    public void unRegisterCallback(Callback callback) {
        mPlayer.unRegisterCallback(callback);
    }

    @Override
    public void removeCallbacks() {
        mPlayer.removeCallbacks();
    }

    @Override
    public void releasePlayer() {
        mPlayer.releasePlayer();
    }

    public class LocalBinder extends Binder {
        public PlaybackService getService() {
            return PlaybackService.this;
        }
    }

    public String getSong() {
        return mPlayer.getSong();
    }
}
