package com.example.jgraham.kitabureg1;

/*

 .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.
| .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. |
| |  ___  ____   | || |     _____    | || |  _________   | || |      __      | || |   ______     | || | _____  _____ | |
| | |_  ||_  _|  | || |    |_   _|   | || | |  _   _  |  | || |     /  \     | || |  |_   _ \    | || ||_   _||_   _|| |
| |   | |_/ /    | || |      | |     | || | |_/ | | \_|  | || |    / /\ \    | || |    | |_) |   | || |  | |    | |  | |
| |   |  __'.    | || |      | |     | || |     | |      | || |   / ____ \   | || |    |  __'.   | || |  | '    ' |  | |
| |  _| |  \ \_  | || |     _| |_    | || |    _| |_     | || | _/ /    \ \_ | || |   _| |__) |  | || |   \ `--' /   | |
| | |____||____| | || |    |_____|   | || |   |_____|    | || ||____|  |____|| || |  |_______/   | || |    `.__.'    | |
| |              | || |              | || |              | || |              | || |              | || |              | |
| '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' |
 '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'

 */

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/*
 * Class to receive GCM broadcasts.
 * Sets a notification whenever received.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("Received a broadcast", "received");
        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}