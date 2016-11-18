package com.example.peixuan.earthquakefeed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class EarthquakeAlarmReceiver extends BroadcastReceiver {
    public static final String ACTION_REFRESH_EARTHQUAKE_ALARM =
            "com.example.peixuan.earthquakefeed.ACTION_REFRESH_EARTHQUAKE_ALARM";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Intent startIntent  = new Intent(context, EarthquakeUpdateService.class);
        context.startService(startIntent);
    }
}
