package com.example.jgraham.kitabureg1;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

/**
 * Created by jgraham on 3/3/17.
 */

public class AlarmService extends IntentService {

    // Notifications
    private NotificationManager notification_manager;
    public static final int NOTIFICATION_ID = 100;

    // Vib & Sound
    public Vibrator vibrator;
    public MediaPlayer media_player;


    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */

    public AlarmService() {
        super("AlarmService");
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("ALARM", "AlarmService --> OnHandleIntent called");

        // Start notification
        setupNotification();

        // Start alarm items
        media_player = media_player.create(getApplicationContext(), R.raw.ring);
        vibrator = (Vibrator) getApplicationContext().
                getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(750);

        // Stop this service
        stopService(intent);
    }

    private void setupNotification(){
        if (notification_manager == null) {
            Intent intent = new Intent(this, SendDataActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); // MR5 Added
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);//XD: see book p1019 why we do not use Notification.Builder
            notificationBuilder.setSmallIcon(R.drawable.kitabu);
            notificationBuilder.setContentTitle("Kitabu Notification");
            notificationBuilder.setContentText("Tap to view your link");
            notificationBuilder.setContentIntent(pendingIntent);
            notificationBuilder.setAutoCancel(true);
            Notification notification = notificationBuilder.build();
            notification.flags |= Notification.FLAG_ONGOING_EVENT;
            notification_manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notification_manager.notify(NOTIFICATION_ID, notification);
        }
    }
}
