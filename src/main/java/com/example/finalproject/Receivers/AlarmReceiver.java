package com.example.finalproject.Receivers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.example.finalproject.Activities.MainActivity;
import com.example.finalproject.Data.User;
import com.example.finalproject.R;

import java.util.Calendar;

/**
 * Alarm Receiver
 * @author Junqi.Sun
 */
public class AlarmReceiver extends BroadcastReceiver {
    /**
     * onReceive
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction() == "NotificationAlarm") {

            //Get data from intent
            Bundle intentData = intent.getExtras();
            String name = intentData.getString("username");
            String psw = intentData.getString("psw");
            int points = intentData.getInt("points");
            int checkin = intentData.getInt("checkin");
            String date = intentData.getString("date");


            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            //reopen intent
            Intent reOpenIntent = new Intent(context, MainActivity.class);
            reOpenIntent.putExtra("username", name);
            reOpenIntent.putExtra("psw", psw);
            reOpenIntent.putExtra("points", points);
            reOpenIntent.putExtra("receiverMode", true);
            reOpenIntent.putExtra("checkin", checkin);
            reOpenIntent.putExtra("date", date);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 101, reOpenIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(reOpenIntent);

            NotificationCompat.Builder builder1 = new NotificationCompat.Builder(context, "notification");
            String id_channel = "channel_alarm";
            String description = "set_alarm_birthday";

            builder1.setSmallIcon(R.drawable.icon_learning);
            builder1.setTicker("New Messages");

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(id_channel, "alarm", importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);


            builder1.setContentTitle("Time to Learn!");
            builder1.setContentText("Do not forget learning today");
            builder1.setAutoCancel(true);
            builder1.setDefaults(Notification.DEFAULT_ALL);
            builder1.setChannelId(id_channel);
            builder1.setContentIntent(pendingIntent);
            builder1.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_learning));
            builder1.setFullScreenIntent(pendingIntent, false);
            Notification notification1 = builder1.build();
            notificationManager.notify(101, notification1);

            Log.i("Test Receiver", "has Received!");
        }
    }


}
