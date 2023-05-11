package com.example.fitfood.data;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.fitfood.R;
import com.example.fitfood.ui.MainActivity;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Отправляем уведомление
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyChannel")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Напоминание")
                .setContentText("Время поесть!")
                .setContentIntent(contentIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        System.out.println("NOTIFY");
        notificationManager.notify(200, builder.build());
    }

}
