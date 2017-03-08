package com.example.jgraham.kitabureg1;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.jgraham.kitabureg1.database.KitabuEntry;
import com.example.jgraham.kitabureg1.database.MySQLiteDbHelper;

public class AlarmService extends IntentService {

    public static final int NOTIFICATION_ID = 100;
    // Vib & Sound
    public Vibrator vibrator;
    public MediaPlayer media_player;
    // Url id
    protected int m_id;
    // Notifications
    private NotificationManager notification_manager;


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

        // Get url id from intent
        m_id = intent.getIntExtra("id", -1);
        if (m_id == -1) {
            Log.d("ALARM", "Warning: AlarmService url id is -1");
        }
        // Start notification
        setupNotification();
        // Start alarm items
        media_player = media_player.create(getApplicationContext(), R.raw.ring);
        vibrator = (Vibrator) getApplicationContext().
                getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 200};
        vibrator.vibrate(1200);
        // Stop this alarm service
        stopService(intent);
    }

    //Method to setup notifications
    private void setupNotification() {
        if (notification_manager == null) {
            // Set intent for launching URL
            MySQLiteDbHelper mySQLiteDbHelper = MySQLiteDbHelper.getInstance(getApplicationContext());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            // This handles the case when the id is -1 for some (bad) reason
            if (m_id == -1) {
                KitabuEntry kitabuEntry = mySQLiteDbHelper.fetchEntryByIndex(m_id);
                String url = "https://www.google.com";
                intent.setData(Uri.parse(url));
            } else {
                KitabuEntry kitabuEntry = mySQLiteDbHelper.fetchEntryByIndex(m_id);
                String url = kitabuEntry.getmLink();
                intent.setData(Uri.parse(url));
            }
            //Intent intent = new Intent(this, SendDataActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
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
