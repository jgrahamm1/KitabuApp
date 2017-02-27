package com.example.jgraham.kitabureg1;

/**
 * Created by prashant on 2/22/17.
 * Inspired from demo code
 *
 *
                                     88
                                    88
                                   88
 8b       d8  ,adPPYba,   ,adPPYb,88 ,adPPYYba,
 `8b     d8' a8"     "8a a8"    `Y88 ""     `Y8
 `8b   d8'  8b       d8 8b       88 ,adPPPPP88
 `8b,d8'   "8a,   ,a8" "8a,   ,d88 88,    ,88
 Y88'     `"YbbdP"'   `"8bbdP"Y8 `"8bbdP"Y8
 d8'
 d8'

 */
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.gcm.GoogleCloudMessaging;


public class GcmIntentService extends IntentService {

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);
        Log.d("LOG", extras.toString());

        if (extras != null && !extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
            {
                Log.d("LOG", extras.toString());
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
}