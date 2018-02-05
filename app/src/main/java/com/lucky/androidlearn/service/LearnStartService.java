package com.lucky.androidlearn.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.presentation.ui.activities.MainActivity;

public class LearnStartService extends Service {
    private static final String TAG = "StartServiceLearn";

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "");
        builder.setContentTitle("StartServiceTitle");
        builder.setContentText("StartServiceText");
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        startForeground(1, notification);
        Log.e(TAG, "onCreate: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: ");
        doSomethings();
        return START_STICKY;
    }

    private void doSomethings() {
        Log.e(TAG, "doSomethings: ");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
        stopForeground(true);
    }
}
