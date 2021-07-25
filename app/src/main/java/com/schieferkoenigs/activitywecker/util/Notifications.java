package com.schieferkoenigs.activitywecker.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.schieferkoenigs.activitywecker.R;
import com.schieferkoenigs.activitywecker.activities.StepDetectorActivity;

public class Notifications {

    private Context context;

    public Notifications(Context mContext) {
        this.context = mContext;
    }

    /**
     * Der Notification Channel wird hier erzeugt
     */
    public void createNotificationChannel() {
        //Notification Channel wird erzeugt(da API >=26.0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
            channel.setDescription(Constants.CHANNEL_DESCRIPTION);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Benachrichtigung wird erstellt
     *
     * @param text Der ausgelesene Weckzeit text aus der File
     */
    public void setNotification(String text) {
        Log.i("setNotification:", text);
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(context, StepDetectorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.CHANNEL_ID)
                .setSmallIcon(R.drawable.wecker)
                .setContentTitle(Constants.NOTIFICATION_TITLE)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(42, builder.build());
    }
}
