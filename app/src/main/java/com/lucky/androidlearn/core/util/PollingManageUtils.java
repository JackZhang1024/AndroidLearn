package com.lucky.androidlearn.core.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 轮询管理工具
 * Created by zfz on 2017/11/14.
 */

public class PollingManageUtils {
    private static final String TAG = "PollingManageUtils";

    // 一般定时器
    public static void startPollingServiceNoRepeat(Context context, Class<?> cls, String action){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //触发服务开始的时间
        long triggerAtTime = System.currentTimeMillis();
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtTime, pendingIntent);
        Log.e(TAG, "startPollingServiceNoRepeat: ");
    }

    // 启动轮询操作
    public static void startPollingService(Context context, int seconds, Class<?> cls, String action){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //触发服务开始的时间
        Log.e(TAG, "startPollingService: ");
        long triggerAtTime = System.currentTimeMillis();
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtTime, seconds*1000, pendingIntent);
    }

    // 结束轮询操作
    public static void stopPollingService(Context context, Class<?> cls, String action){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        Log.e(TAG, "stopPollingService: ");
    }

    // 立即开始服务
    public static void startServiceImmediately(Context context, Class<?> cls, String action){
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        context.startService(intent);
    }

    // 立即结束服务
    public static void stopServiceImmediately(Context context, Class<?> cls, String action){
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        context.stopService(intent);
    }

    // 立即重启服务
    public static void restartPollingServiceImmediately(Context context, Class<?> cls, String action){
        stopServiceImmediately(context, cls, action);
        startServiceImmediately(context, cls, action);
    }

}
