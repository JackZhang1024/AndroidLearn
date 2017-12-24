package com.lucky.androidlearn;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.jingewenku.abrahamcaijin.commonutil.AppLogMessageMgr;
import com.lucky.androidlearn.provider.DBManager;

public class AndroidApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }


    // 设置字体大小不受系统字体大小变化设置
    // 如果不设置 就会发生字体影响布局
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1) {
            getResources();
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        if (resources.getConfiguration().fontScale != 1) {
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();
            resources.updateConfiguration(newConfig, resources.getDisplayMetrics());
        }
        return resources;
    }
}
