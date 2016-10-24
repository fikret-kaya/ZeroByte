package com.zero.zerobyte;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by FKRT on 16.10.2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private SharedPreferences preferences;

    private int totalData, remainingData;

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "receiver", Toast.LENGTH_SHORT).show();

        Intent background = new Intent(context, BackgroundService.class);
        background.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startService(background);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        preferences = context.getSharedPreferences("MyPreferences", context.MODE_PRIVATE);
        totalData = preferences.getInt("totalInt", -1);
        remainingData = preferences.getInt("remainingInt", -1);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("Internet Usage");
        builder.setContentText("So far " + (totalData - remainingData) + "mb (%5) internet used!");
        builder.setSmallIcon(R.drawable.yuz);
        builder.setOngoing(true);

        notificationManager.notify(1, builder.build());

    }
}
