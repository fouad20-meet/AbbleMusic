package com.example.abblemusic;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class IntentService extends android.app.IntentService{
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
  .
     */
    public IntentService() {
        super("IntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //phase 1
        int icon = R.drawable.musicicon;
        long when = System.currentTimeMillis();
        String title = "Welcome to Abble Music";
        String text="Thank you for signing up for Abble Music. Hope you enjoy making playlist and listening to music!";




        //phase 2
        Intent intent1 = new Intent(this, MainActivity.class);
        intent1.putExtra("key", "Uzi oranim");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "M_CH_ID");

        //required from android 8 and above
        setChannel(notificationManager,builder);

        //phase 3
        Notification notification = builder.setContentIntent(pendingIntent)
                .setSmallIcon(icon).setWhen(when)
                .setAutoCancel(true).setContentTitle(title)
                .setContentText(text).build();
        notificationManager.notify(1, notification);


    }
    public void setChannel(NotificationManager notificationManager,NotificationCompat.Builder builder){
        String channelId = "YOUR_CHANNEL_ID";
        NotificationChannel channel = new NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);
        builder.setChannelId(channelId);
    }
}
