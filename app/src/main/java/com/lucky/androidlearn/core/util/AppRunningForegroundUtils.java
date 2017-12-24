package com.lucky.androidlearn.core.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by zfz on 2017/11/30.
 */

public class AppRunningForegroundUtils {
    private static final String TAG = "AppRunningForegroundUti";

    // true  表示App 已经切换到到后台
    // false 表示App 仍然在前台
    public static boolean isAppBroughtBackGround(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName componentName = tasks.get(0).topActivity;
            if (!componentName.getPackageName().equals(context.getPackageName())) {
                Log.e(TAG, "切换到后台运行.......");
                return true;
            }
        }
        Log.e(TAG, "切换到前台运行.......");
        return false;
    }


    // true  表示App切入到后台
    // false 表示App切换到前台
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.e(TAG, appProcess.processName + "后台运行......");
                    return true;
                } else {
                    Log.e(TAG, appProcess.processName + "前台运行......");
                    return false;
                }
            }
        }
        return false;
    }

}
