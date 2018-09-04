package com.lucky.androidlearn.service;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class MyJobService extends JobIntentService {

    private static final String TAG = "MyJobService";

    public static final int JOB_ID = 1;


    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, MyJobService.class, JOB_ID, work);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: ");
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        // your code
        Log.e(TAG, "onHandleWork: ");
        try {
            for (; ; ) {
                Thread.sleep(3000);
                Log.e(TAG, "当前线程 "+Thread.currentThread().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setInterruptIfStopped(true);
        Log.e(TAG, "onDestroy: ");
    }
}