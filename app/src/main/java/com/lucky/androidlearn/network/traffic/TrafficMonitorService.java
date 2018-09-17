package com.lucky.androidlearn.network.traffic;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.TrafficStats;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.lucky.androidlearn.R;
import com.lucky.androidlearn.presentation.ui.activities.MainActivity;
import java.util.Timer;
import java.util.TimerTask;

public class TrafficMonitorService extends Service {

    private static final String TAG = "TrafficMonitorService";
    private Timer mTimer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: ");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "");
        builder.setContentTitle("AppLearn");
        builder.setContentText("TrafficStatics");
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        startForeground(1, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        collectTrafficStatistics(this);
        return START_STICKY;
    }

    private void collectTrafficStatistics(Context context) {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimer.schedule(new CollectTask(context), 0, 1000*30);
    }


    private class CollectTask extends TimerTask {

        private Context mContext;

        public CollectTask(Context context) {
            this.mContext = context;
        }

        @Override
        public void run() {
            collectStatistics();
        }
    }

    // 统计一个页面所用的流量
    private void collectStatistics() {
        long downloadTraffic = TrafficStats.getUidRxBytes(getAppUID(this));
        long uploadTraffic = TrafficStats.getUidTxBytes(getAppUID(this));
        Log.e(TAG, "collectStatistics: 下载 " + getFormattedStatics(downloadTraffic));
        Log.e(TAG, "collectStatistics: 上传 " + getFormattedStatics(uploadTraffic));
    }


    private int getAppUID(Context context) {
        int appUID = -1;
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            appUID = applicationInfo.uid;
            return appUID;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appUID;
    }


    // 获取格式化的网络流量统计数据
    public String getFormattedStatics(long traffics) {
        String trafficInByte = String.format("%s Bytes", traffics);
        //Log.e(TAG, trafficInByte);
        long trafficInKB = traffics / 1024;
        return String.format("%s KB", trafficInKB);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer!=null){
            mTimer.cancel();
        }
    }
}
