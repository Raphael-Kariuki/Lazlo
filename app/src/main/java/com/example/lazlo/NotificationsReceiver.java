package com.example.lazlo;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.lazlo.Sql.DBHelper;

public class NotificationsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //add intent to redirect on notification click
        Intent actOnTask = new Intent(context,tasks.class);

        actOnTask.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,actOnTask,PendingIntent.FLAG_IMMUTABLE);

        //build notification

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"lazlo")
                .setSmallIcon(R.drawable.ic_baseline_access_time_24)
                .setContentTitle(intent.getStringExtra("taskTitle"))
                .setContentText(intent.getStringExtra("taskDescription"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(7702, builder.build());


    }
}
