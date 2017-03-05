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

    protected int m_id;

    // Receive broadcast
    @Override
    public void onReceive(final Context context, Intent intent) {
        //Log.d("ALARM", "BroadcastReceiver");
        m_id = intent.getIntExtra("id", -1);
        if (m_id == -1) {
            Log.d("ALARM", "Warning: EMAAlarmReceiver got -1 for id");
        }

        startPSM(context);
    }

    // Start service
    private void startPSM(Context context) {
        Log.d("ALARM", "BroadcastReceiver firing");
        Intent emaIntent = new Intent(context, AlarmService.class);
        emaIntent.putExtra("id", m_id);
        emaIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(emaIntent);
    }
}