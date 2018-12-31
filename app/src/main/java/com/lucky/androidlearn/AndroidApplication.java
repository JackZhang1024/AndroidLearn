package com.lucky.androidlearn;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.Process;
import android.util.Log;

import com.lucky.androidlearn.dagger2learn.AppModule;
import com.lucky.androidlearn.dagger2learn.lesson04.AppComponent;
import com.lucky.androidlearn.dagger2learn.lesson04.DaggerAppComponent;
import com.lukcyboy.simpleaar.SimpleAarLog;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.umeng.commonsdk.UMConfigure;

import java.lang.reflect.Field;

import javax.inject.Inject;


/**
 * @author zfz
 */
public class AndroidApplication extends Application {
    private static final String TAG = "AndroidApplication";
    @Inject
    LocationManager locationManager;
    AppComponent appComponent;

    private static AndroidApplication mAndroidApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mAndroidApplication = this;
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        appComponent.inject(this);
        Logger.addLogAdapter(new AndroidLogAdapter());
        Log.e(TAG, "onCreate: 进程ID " + Process.myPid());
        SimpleAarLog.getInstance();
        //友盟统计
        //UMConfigure.init(this, "5bdbef32b465f5b32400001d", "AndroidLearn", UMConfigure.DEVICE_TYPE_PHONE, null);
        //设置LOG开关，默认为false
        UMConfigure.setLogEnabled(true);
        try {
            Class<?> aClass = Class.forName("com.umeng.commonsdk.UMConfigure");
            Field[] fs = aClass.getDeclaredFields();
            for (Field f:fs){
                Log.e("xxxxxx","ff="+f.getName()+"   "+f.getType().getName());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口
        UMConfigure.init(this, "5bdbef32b465f5b32400001d", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
    }

    /**
     * 设置字体大小不受系统字体大小变化设置
     * 如果不设置 就会发生字体影响布局
     */
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

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public static AndroidApplication getInstances() {
        return mAndroidApplication;
    }

}
