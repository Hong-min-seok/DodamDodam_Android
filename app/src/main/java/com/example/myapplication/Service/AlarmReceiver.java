package com.example.myapplication.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "onReceive: onReceive()");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Intent in = new Intent(context, RestartService.class);
            context.startForegroundService(in);

        } else {

            Intent in = new Intent(context, BackService.class);
            context.startService(in);

        }

    }

}
