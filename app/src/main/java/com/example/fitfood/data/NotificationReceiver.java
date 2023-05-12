package com.example.fitfood.data;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.fitfood.R;
import com.example.fitfood.ui.MainActivity;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id", 0);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notifyChannel", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        // Отправляем уведомление
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyChannel")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Напоминание")
                .setContentText("Время " + intent.getStringExtra("time"))
                .setContentIntent(contentIntent)
                .setAutoCancel(true);
        System.out.println("NOTIFY");
        notificationManager.notify(id, builder.build());
    }

}
