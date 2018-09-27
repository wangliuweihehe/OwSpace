package com.wlw.admin.owspace.view.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wlw.admin.owspace.R;

import static android.provider.Contacts.GroupMembership.GROUP_ID;

public class NotificationActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "channel_id";
    public static final String CHANNEL_ID2 = "channel_id2";
    public static final String CHANNEL_NAME = "CHANNEL_NAME";
    public static final int NOTIFYID = 0x11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        findViewById(R.id.notification).setOnClickListener(v -> notification());
    }

    private void notification() {
         NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mNotifyManager.createNotificationChannelGroup(new NotificationChannelGroup("groupID","groupName"));

            NotificationChannel  channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setGroup("groupID");
            channel.setShowBadge(true);
            channel.setLightColor(Color.RED);
            channel.enableLights(true);
            mNotifyManager.createNotificationChannel(channel);

            NotificationChannel channel2 = new NotificationChannel(CHANNEL_ID2, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel2.setGroup("groupID");
            channel2.setShowBadge(true);
            channel2.setLightColor(Color.RED);
            channel2.enableLights(true);
            mNotifyManager.createNotificationChannel(channel2);
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_notify_chat)
                .setContentTitle("你有一条新的消息")
                .setContentText("this is normal notification style")
                .setTicker("notification ticker")
                .setPriority(1000)
                .setAutoCancel(true)
                .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                .setNumber(3)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setOngoing(true);

        Notification notification = mBuilder.build();
        mNotifyManager.notify(NOTIFYID,notification);
    }
}
