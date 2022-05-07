package com.example.kepzomuveszet;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    private Context context;
    private NotificationManager notificationManager;
    private static final String CHANNEL_ID = "shop_notification_channel";

    NotificationHandler(Context context){
        this.context = context;
        this.notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createChannel();
    }

    private void createChannel(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        }
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"Shop notification" ,NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableVibration(true);
        channel.setDescription("Notification from the shop");


        this.notificationManager.createNotificationChannel(channel);
    }


    public void send(String message){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setContentTitle("Shop application")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_message_foreground);
        this.notificationManager.notify(0, builder.build());
    }
}
