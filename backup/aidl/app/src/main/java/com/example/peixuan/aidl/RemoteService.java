package com.example.peixuan.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;

public class RemoteService extends Service {
    public RemoteService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    private final IRemoteService.Stub mBinder = new IRemoteService.Stub() {
        public int getPid() {
            return Process.myPid();
        }

        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
                        double aDouble, String aString) {
            //Does nothing
        }
    };

}
