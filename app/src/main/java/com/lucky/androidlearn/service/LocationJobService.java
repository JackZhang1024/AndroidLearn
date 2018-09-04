package com.lucky.androidlearn.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class LocationJobService extends JobService {

    private static final String TAG = "LocationJobService";
    private Timer mTimer = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: locationJobService  ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: ");
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e(TAG, "onStartJob: ");
        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e(TAG, "run: 定位中...."+Thread.currentThread().getName());
            }
        }, 0, 3000);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e(TAG, "onStopJob: ");
        return false;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy: ");
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    // 强制结束
    public void forceKillService(JobParameters params) {
        Log.e(TAG, "forceKillService: ");
        jobFinished(params, true);
    }
}
