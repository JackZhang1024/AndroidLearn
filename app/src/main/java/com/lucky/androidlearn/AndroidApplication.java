package com.lucky.androidlearn;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.Process;
import android.util.Log;

import com.jingewenku.abrahamcaijin.commonutil.klog.KLog;
import com.lucky.androidlearn.dagger2learn.AppModule;
import com.lucky.androidlearn.dagger2learn.lesson04.AppComponent;
import com.lucky.androidlearn.dagger2learn.lesson04.DaggerAppComponent;
import com.lukcyboy.simpleaar.SimpleAarLog;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
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
    private RefWatcher refWatcher;

    private static AndroidApplication mAndroidApplication;

    @Override
    public void onCreate() {
        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)){
//            // 如果是LeakCanary进程 就不要走后面的流程 直接return
//            return;
//        }
//        LeakCanary.install(this);

        // 如果想检测出除了Activity的内存泄漏之外的内存泄漏 需要以下设置
        setupRefWatcher();

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
        KLog.init(true);
    }


    private RefWatcher setupRefWatcher(){
        if (LeakCanary.isInAnalyzerProcess(this)){
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
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



    // https://www.cnblogs.com/baiqiantao/p/10125084.html
    public static RefWatcher getRefWatcher(Context context){
        AndroidApplication application = (AndroidApplication) context.getApplicationContext();
        return application.refWatcher;
    }


}
