package com.lucky.androidlearn.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class LearnBindService extends Service{


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new BindServiceBinder();
    }

    class BindServiceBinder extends Binder{

        public LearnBindService getService(){
            return LearnBindService.this;
        }
    }




}
