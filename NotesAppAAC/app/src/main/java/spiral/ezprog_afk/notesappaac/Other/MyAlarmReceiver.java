package spiral.ezprog_afk.notesappaac.Other;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import spiral.ezprog_afk.notesappaac.BottomActivities.MainActivity;
import spiral.ezprog_afk.notesappaac.R;

public class MyAlarmReceiver extends BroadcastReceiver {

    // Идентификатор канала
    public static String CHANNEL_ID = "Work Note channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
    }

    private void showNotification(Context context) {

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        Intent notificationIntent = new Intent(context, MainActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Work Note")
                .setContentText("Time to complete your task!")
                .setVibrate(new long[]{1000, 1000, 1000, 1000})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .build();

        notificationManagerCompat.notify(1, notification);
    }
}
