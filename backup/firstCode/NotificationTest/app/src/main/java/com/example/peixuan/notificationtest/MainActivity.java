package com.example.peixuan.notificationtest;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button sendNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendNotice = (Button) findViewById(R.id.send_notice);
        sendNotice.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_notice:
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification.Builder mBuilder = new Notification.Builder(this);

                Intent intent = new Intent(this, NotificationActivity.class);

                TaskStackBuilder mTaskStackBuilder = TaskStackBuilder.create(this);
                mTaskStackBuilder.addParentStack(NotificationActivity.class);
                mTaskStackBuilder.addNextIntent(intent);

                PendingIntent pendingIntent = mTaskStackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                Uri uri = Uri.fromFile(new File("/system/media/audio/ringtones/Zeta.ogg"));
                mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("this is content title")
                        .setContentText("this is content text")
                        .setSound(uri)
                        .setContentIntent(pendingIntent);
                manager.notify(1, mBuilder.build());
                break;
            default:
                break;
        }
    }
}
