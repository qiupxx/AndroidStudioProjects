package com.example.peixuan.smstest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView sender;
    private TextView content;

    private IntentFilter receiveFilter;
    private MessageReceiver messageReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sender = (TextView) findViewById(R.id.sender);
        content = (TextView) findViewById(R.id.content);
        receiveFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        receiveFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY-1);
        messageReceiver = new MessageReceiver();
        registerReceiver(messageReceiver, receiveFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(messageReceiver);
    }

    class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("MessageReceiver", intent.getAction());
            Bundle bundle = intent.getExtras();
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] message = new SmsMessage[pdus.length];
            for (int i = 0; i < message.length; i++)
                message[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String address = message[0].getOriginatingAddress();
            String fullMessage = "";
            for (SmsMessage message1 : message)
                fullMessage += message1.getMessageBody();
            sender.setText(address);
            content.setText(fullMessage);
            //拦截广播
            abortBroadcast();
        }
    }
}
