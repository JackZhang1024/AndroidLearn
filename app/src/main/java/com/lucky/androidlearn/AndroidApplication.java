package com.lucky.androidlearn;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

import com.jingewenku.abrahamcaijin.commonutil.klog.KLog;
import com.kingja.loadsir.core.LoadSir;
import com.lucky.androidlearn.dagger2learn.AppModule;
import com.lucky.androidlearn.dagger2learn.learn04.AppComponent;
import com.lucky.androidlearn.dagger2learn.learn04.DaggerAppComponent;
import com.lucky.androidlearn.loadsir.loadcallback.EmptyCallback;
import com.lucky.androidlearn.loadsir.loadcallback.ErrorCallback;
import com.lucky.androidlearn.loadsir.loadcallback.LoadingCallback;
import com.lucky.androidlearn.plugin.hookplugin.HookPluginUtil;
import com.lukcyboy.simpleaar.SimpleAarLog;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.commonsdk.UMConfigure;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
        //setupRefWatcher();
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "p.apk");
        if (!file.exists()) {
            Log.e(TAG, "onCreate: ");
        }
        mAndroidApplication = this;
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        appComponent.inject(this);
        Logger.addLogAdapter(new AndroidLogAdapter());
        Log.e(TAG, "onCreate: 进程ID " + Process.myPid());
        HookPluginUtil.getInstance(this).initHook();
        try {
            // 测试某个类是否已经被加载过来了
            Class pluginMain = Class.forName("com.luckyboy.plugin_package.MainActivity");
            String name = pluginMain.getSimpleName();
            Log.e(TAG, "onCreate: 是否已经加载过来插件的类" + name);
            doPluginLayoutLoad();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SimpleAarLog.getInstance();
        //友盟统计
        //UMConfigure.init(this, "5bdbef32b465f5b32400001d", "AndroidLearn", UMConfigure.DEVICE_TYPE_PHONE, null);
        //设置LOG开关，默认为false
        UMConfigure.setLogEnabled(true);
        try {
            Class<?> aClass = Class.forName("com.umeng.commonsdk.UMConfigure");
            Field[] fs = aClass.getDeclaredFields();
            for (Field f : fs) {
                Log.e("xxxxxx", "ff=" + f.getName() + "   " + f.getType().getName());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口
        UMConfigure.init(this, "5bdbef32b465f5b32400001d", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
        KLog.init(true);
        initLoadSir();
    }


    private RefWatcher setupRefWatcher() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
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

    // 禁止屏幕字体大小随着系统的字体设置改变而改变
//    @Override
//    public Resources getResources() {
//        Resources resources = super.getResources();
//        if (resources.getConfiguration().fontScale != 1) {
//            Configuration newConfig = new Configuration();
//            newConfig.setToDefaults();
//            resources.updateConfiguration(newConfig, resources.getDisplayMetrics());
//        }
//        return resources;
//    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public static AndroidApplication getInstances() {
        return mAndroidApplication;
    }

    public void initLoadSir() {
        LoadSir.beginBuilder()
                .addCallback(new LoadingCallback())
                .addCallback(new ErrorCallback())
                .addCallback(new EmptyCallback())
                .setDefaultCallback(LoadingCallback.class)
                .commit();
    }

    public static RefWatcher getRefWatcher(Context context) {
        AndroidApplication application = (AndroidApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private Resources resources;
    private AssetManager assetManager;

    // 处理加载插件中的布局
    private void doPluginLayoutLoad() throws Exception {
        // 通过反射的方式获取assetManager
        assetManager = AssetManager.class.newInstance();

        // 把插件的路径 给assetManager
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "p.apk");
        if (!file.exists()) {
            throw new FileNotFoundException("没有找到插件包");
        }
        // 执行此方法 public int addAssetPath(String path) {} 把插件路径添加进去
        Method method = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
        method.setAccessible(true);
        method.invoke(assetManager, file.getAbsolutePath());
        // 获取宿主的配置信息 高分辨率屏 低分辨率屏等系统配置
        Resources r = getResources();
        // 特殊：专门加载插件资源
        resources = new Resources(assetManager, r.getDisplayMetrics(), r.getConfiguration());
    }

    /**
     * boolean getResourceValue(@AnyRes int resId, int densityDpi, @NonNull TypedValue outValue,
     * boolean resolveRefs) {
     * if (outValue.type == TypedValue.TYPE_STRING) {
     * outValue.string = mApkAssets[cookie - 1].getStringFromPool(outValue.data);
     * }
     * return true;
     * }
     * }
     */


    @Override
    public Resources getResources() {
        return resources == null ? super.getResources() : resources;
    }


}
