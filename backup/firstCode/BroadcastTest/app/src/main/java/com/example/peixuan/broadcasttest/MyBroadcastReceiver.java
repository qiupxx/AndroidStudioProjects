package com.example.peixuan.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by peixuan on 16/8/8.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    public static final String MY_BROADCAST = "com.example.peixuan.broadcasttest.MY_BROADCAST";

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "received in MyBroadcastReceiver",
                Toast.LENGTH_SHORT).show();
        //终止广播继续发送
        abortBroadcast();
    }
}
