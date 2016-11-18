package com.example.peixuan.message;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by peixuan on 16/6/7.
 */
public class MessengerService extends Service {
    static final int MSG_SAY_HELLO = 1;

    //handler of incoming messages from clients
    class IncomingHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAY_HELLO:
                    Log.d("qiupxx", "ok");
                    Toast.makeText(getApplicationContext(), "hello!",
                            Toast.LENGTH_SHORT);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    //target we publish for clients to send messages to InclomingHandler.
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
