package com.lucky.androidlearn.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.presentation.ui.activities.MainActivity;


// 前台服务
public class LearnStartService extends Service {
    private static final String TAG = "StartServiceLearn";

    @Override
    public void onCreate() {
        super.onCreate();
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "StartServiceLearn");
//        builder.setContentTitle("StartServiceTitle");
//        builder.setContentText("StartServiceText");
//        builder.setWhen(System.currentTimeMillis());
//        builder.setSmallIcon(R.drawable.ic_launcher);
//        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
//        Intent intent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//        builder.setContentIntent(pendingIntent);
//        Notification notification = builder.build();
        //startForeground(1, notification);

        createNotification();
        Log.e(TAG, "onCreate: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: ");
        if (intent!=null){
            String params = intent.getStringExtra(ServiceActivity.SERVICE_PARAM);
            Log.e(TAG, params);
        }
        doSomethings();
        return START_STICKY;
    }

    private void doSomethings() {
        Log.e(TAG, "doSomethings: ");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println("++++++++++++++++++++++");
            }
        }, 1000*6);
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


    @TargetApi(26)
    private void createNotification(){
        NotificationChannel channel = new NotificationChannel("fore_service", "前台服务", NotificationManager.IMPORTANCE_HIGH);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
        Intent intentForeSerive = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentForeSerive, 0);
        Notification notification = new NotificationCompat.Builder(this, "fore_service")
                .setContentTitle("This is content title")
                .setContentText("This is content text")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
    }
}
