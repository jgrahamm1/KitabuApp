package com.example.jgraham.kitabureg1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;

/**
 * Created by jgraham on 3/3/17.
 */

public class EMAAlarmReceiver extends BroadcastReceiver {

    // Receive broadcast
    @Override
    public void onReceive(final Context context, Intent intent) {
        //Log.d("ALARM", "BroadcastReceiver");

        startPSM(context);
    }

    // Start service
    private void startPSM(Context context) {
        Log.d("ALARM", "BroadcastReceiver firing");
        Intent emaIntent = new Intent(context, AlarmService.class);
        emaIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(emaIntent);
    }
}