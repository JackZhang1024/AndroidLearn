package com.lucky.androidlearn.plugin.hookplugin;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lucky.androidlearn.AndroidApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;

public class HookPluginUtil {

    private static final String TAG = "HookPluginUtil";


    private static List<String> mPluginActivity = new ArrayList<>();

    static {
        mPluginActivity.add(TestPluginActivity.class.getName());
        mPluginActivity.add("com.luckyboy.plugin_package.MainActivity");
    }

    private static HookPluginUtil mInstance;
    private Application mApplicationContext;

    private HookPluginUtil(Application application) {
        this.mApplicationContext = application;
    }


    public static HookPluginUtil getInstance(Application application) {
        if (mInstance == null) {
            mInstance = new HookPluginUtil(application);
        }
        return mInstance;
    }

    public void initHook() {
        try {
            hookAMS2();
            hookActivityThreadHandler();
            pluginToApplication();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TestPluginActivity  hook startActivity ProxyActivity AMS (通过检测)
    // public static IActivityManager getService()
    // IActivityManager start
    // public interface IActivityManager extends IInterface {
    //    public int startActivity() throws RemoteException;
    //}

    // 我们使用自己的IActivityManager 代替系统的IActivityManager对象
    // 在系统的IActivityManager执行startActivity的时候启动ProxyActivity（我们提前注册好的Activity）
    // 这样就可以用真的注册过的Activity来绕过AMS检查 这样就不会报错了
    // 同时我们可以将组建信息存在这个新的启动的代理Intent中，方便后面还原

    private void hookAMS() throws Exception {
        // 1. 创建代理IActivityManager
        Class iActivityManagerClass = Class.forName("android.app.IActivityManager");
        // 2. 获取系统的IActivityManager
        // 根据ActivityManager中的代码
        // public static IActivityManager getService()
        // private static final Singleton<IActivityManager> IActivityManagerSingleton = new Singleton<IActivityManager>()

        // 阅读以上代码可以知道IActivity其实就是Singleton的成员变量 mInstance
        // android.util.Singleton
        // 我们只需要将代理activityManagerProxy 赋值给mInstance就可以替换系统原有的IActivityManager实例

        // -1 获取mInstance成员字段
        // - 1.1 获取Singleton的Class对象
        Class singletonClass = Class.forName("android.util.Singleton");
        Field mInstanceField = singletonClass.getDeclaredField("mInstance");
        // -2 设置mInstance成员字段Value
        //mInstanceField.set(null, activityManagerProxy);
        // - 2.1 需要找到mInstanceField归属的对象 也就是Singleton的对象
        // ActivityManager中有对应的字段可以直接拿到 static IActivityManagerSingleton
        // - 2.2
        Class activityManagerClass = Class.forName("android.app.ActivityManager");
        // 判断版本
        Field activityManagerSingletonField = activityManagerClass.getDeclaredField("IActivityManagerSingleton");
        activityManagerSingletonField.setAccessible(true);
        Object singletonInstance = activityManagerSingletonField.get(null);
        // - 2.3 获取系统原有的IActivityManager对象
        // ActivityManager中的static getService()方法返回的就是原有的IActivityManager对象
        Method getServiceMethod = activityManagerClass.getDeclaredMethod("getService");
        Object originIActivityManger = getServiceMethod.invoke(null);
        Object activityManagerProxy = Proxy.newProxyInstance(AndroidApplication.class.getClassLoader(), new Class[]{iActivityManagerClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Log.e(TAG, "invoke: " + method.getName());
                if ("startActivity".equals(method.getName())) {
                    Log.e(TAG, "invoke: startActivity");
                }
                return method.invoke(originIActivityManger, args);
            }
        });
        // -2.4 设置代理对象替换系统的IActivityManager
        mInstanceField.set(singletonInstance, activityManagerProxy);
    }

    private void hookAMS2() throws Exception {
        // 1. 创建代理IActivityManager
        Class iActivityManagerClass = Class.forName("android.app.IActivityManager");
        // 2. 获取系统的IActivityManager
        // 根据ActivityManager中的代码
        // public static IActivityManager getService()
        // private static final Singleton<IActivityManager> IActivityManagerSingleton = new Singleton<IActivityManager>()

        // 阅读以上代码可以知道IActivity其实就是Singleton的成员变量 mInstance
        // android.util.Singleton
        // 我们只需要将代理activityManagerProxy 赋值给mInstance就可以替换系统原有的IActivityManager实例

        // -1 获取mInstance成员字段
        // - 1.1 获取Singleton的Class对象
        Class singletonClass = Class.forName("android.util.Singleton");
        Field mInstanceField = singletonClass.getDeclaredField("mInstance");
        mInstanceField.setAccessible(true);
        // -2 设置mInstance成员字段Value
        //mInstanceField.set(null, activityManagerProxy);
        // - 2.1 需要找到mInstanceField归属的对象 也就是Singleton的对象
        // ActivityManager中有对应的字段可以直接拿到 static IActivityManagerSingleton
        // - 2.2
        Class activityManagerClass = getActivityManager();
        // 判断版本
        Object singletonInstance = getSingleton(activityManagerClass);
        // - 2.3 获取系统原有的IActivityManager对象
        // ActivityManager中的static getService()方法返回的就是原有的IActivityManager对象
        Object originIActivityManger = getOriginIActivityManager(activityManagerClass);
        Object activityManagerProxy = Proxy.newProxyInstance(AndroidApplication.class.getClassLoader(), new Class[]{iActivityManagerClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Log.e(TAG, "invoke: " + method.getName());
                //public int startActivity(IApplicationThread caller, String callingPackage, Intent intent,
                //        String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags,
                //ProfilerInfo profilerInfo, Bundle options) throws RemoteException;
                if ("startActivity".equals(method.getName())) {
                    Intent rawIntent = (Intent) args[2];
                    try {
                        Log.e(TAG, "invoke: isNull " + (rawIntent.getComponent() == null));
                        if (rawIntent.getComponent() != null && mPluginActivity.contains(ComponentUtils.componentFullName(rawIntent.getComponent()))) {
                            Log.e(TAG, "invoke: startActivity");
                            // 启动ProxyActivity
                            Intent proxyIntent = new Intent(mApplicationContext, ProxyActivity.class);
                            proxyIntent.putExtra("actionIntent", rawIntent);
                            args[2] = proxyIntent;
                        }
                        Log.e(TAG, "invoke: 结束");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Log.e(TAG, "invoke: 拦截到了ActivityManager中的方法");
                // 继续执行原有的逻辑
                return method.invoke(originIActivityManger, args);
            }
        });
        // -2.4 设置代理对象替换系统的IActivityManager
        mInstanceField.set(singletonInstance, activityManagerProxy);
    }

    private Class getActivityManager() throws Exception {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
            return Class.forName("android.app.ActivityManager");
        } else {
            return Class.forName("android.app.ActivityManagerNative");
        }
    }

    private Object getSingleton(Class activityManagerClass) throws Exception {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
            Field activityManagerSingletonField = activityManagerClass.getDeclaredField("IActivityManagerSingleton");
            activityManagerSingletonField.setAccessible(true);
            return activityManagerSingletonField.get(null);
        } else {
            Field activityManagerSingletonField = activityManagerClass.getDeclaredField("gDefault");
            activityManagerSingletonField.setAccessible(true);
            return activityManagerSingletonField.get(null);
        }
    }

    private Object getOriginIActivityManager(Class activityManagerClass) throws Exception {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
            Method getServiceMethod = activityManagerClass.getDeclaredMethod("getService");
            return getServiceMethod.invoke(null);
        } else {
            Method getServiceMethod = activityManagerClass.getDeclaredMethod("getDefault");
            return getServiceMethod.invoke(null);
        }
    }

    ///**
    //     * Handle system messages here.
    //     */
    //    public void dispatchMessage(Message msg) {
    //        if (msg.callback != null) {
    //            handleCallback(msg);
    //        } else {
    //            if (mCallback != null) {
    //                if (mCallback.handleMessage(msg)) {
    //                    return;
    //                }
    //            }
    //            handleMessage(msg);
    //        }
    //    }
    private void hookActivityThreadHandler() throws Exception {
        // android.app.ActivityThread中的Handler类型的变量mH 负责处理启动Activity
        // 具体的执行是由handleMessage方法来处理
        // 所以我们可以想办法Hook主handleMessage或者在执行handleMessage之前进行一些处理
        // 让handleMessage正常执行

        // 由上面的注释的代码我们可以hookHandler对象的mCallback字段，然后让handleMessage正常执行

        // 将HookCallBack设置为mCallback对象
        Class mHandlerClass = Class.forName("android.os.Handler");
        Field mCallbackField = mHandlerClass.getDeclaredField("mCallback");
        // 需要获取到handler对象
        // mCallbackField.set(, new HookCallBack());
        // handler对象可以通过ActivityThread的mH变量获取到
        Class activityThreadClass = Class.forName("android.app.ActivityThread");
        Field mHandlerField = activityThreadClass.getDeclaredField("mH");
        mHandlerField.setAccessible(true);
        // 需要获取ActivityThread对象
        // mHandlerField.get();

        //   public static ActivityThread currentActivityThread() {
        //        return sCurrentActivityThread;
        //    }
        Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
        currentActivityThreadMethod.setAccessible(true);
        // 静态方法不用传对象
        Object currentActivityThread = currentActivityThreadMethod.invoke(null);
        // 获取到mH对象
        Object mHandler = mHandlerField.get(currentActivityThread);
        // 将自定义的Callback设置到mHandler的mCallback字段中
        mCallbackField.setAccessible(true);
        mCallbackField.set(mHandler, new HookCallBack((Handler) mHandler));
    }

    public static final int RELAUNCH_ACTIVITY = 160;
    public static final int LAUNCH_ACTIVITY = 100;

    class HookCallBack implements Handler.Callback {

        private Handler mHandler;

        public HookCallBack(Handler handler) {
            this.mHandler = handler;
        }

        @Override
        public boolean handleMessage(Message msg) {
            // 我们在这里进行恢复TestPluginActivity
            // 继续处理handleMessage
            try {
                return handleMessageInner(msg);
                //mHandler.handleMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    private boolean handleMessageInner(Message msg) throws Exception {
        Log.e(TAG, "handleMessageInner: " + Build.VERSION.SDK_INT + " msg.what " + msg.what);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
            if (msg.what == 159) {
                Object mClientTransaction = msg.obj;
//                Log.e(TAG, "handleMessageInner: 159 " + object);
                // 获取到ClientTransaction中的列表 mActivityCallbacks
                Class<?> mClientTransactionClass = Class.forName("android.app.servertransaction.ClientTransaction");
                Field mActivityCallbacksField = mClientTransactionClass.getDeclaredField("mActivityCallbacks");
                mActivityCallbacksField.setAccessible(true);
                // List<ClientTransactionItem> mActivityCallbacks
                List<Object> mActivityCallbacks = (List<Object>) mActivityCallbacksField.get(mClientTransaction);
                // 获取LaunchActivityItem的实例对象
                // 需要判断
                if (mActivityCallbacks.size() == 0) {
                    return false;
                }
                Object mLaunchActivityItem = mActivityCallbacks.get(0);
                Class mLaunchActivityItemClass = Class.forName("android.app.servertransaction.LaunchActivityItem");
                if (!mLaunchActivityItemClass.isInstance(mLaunchActivityItem)) {
                    return false;
                }
                String itemName = "android.app.servertransaction.LaunchActivityItem";
                Field mIntentField = mLaunchActivityItemClass.getDeclaredField("mIntent");
                mIntentField.setAccessible(true);
                Intent proxyIntent = (Intent) mIntentField.get(mLaunchActivityItem);
                Log.e(TAG, "handleMessageInner: "+proxyIntent);
                Intent actionIntent = proxyIntent.getParcelableExtra("actionIntent");
                if (actionIntent != null && actionIntent.getComponent() != null && mPluginActivity.contains(ComponentUtils.componentFullName(actionIntent.getComponent()))) {
                    // 把真实的Activity信息歇会去
                    //proxyIntent.setComponent(actionIntent.getComponent());
                    //mIntentField.set();
                    Log.e(TAG, "handleMessageInner: targetIntent "+actionIntent.getComponent().getClassName());
                    mIntentField.set(mLaunchActivityItem, actionIntent);
                }
            }
            return false;
        } else {
            if (msg.what == LAUNCH_ACTIVITY) {
                // 我们需要获取之前Hook携带过来的TestPluginActivity
                // msg 这块其实是ActivityClientRecord
                Object obj = msg.obj;
                Field intentField = obj.getClass().getDeclaredField("intent");
                intentField.setAccessible(true);
                // 获取intent对象 才能取出携带过来的actionIntent
                Intent intent = (Intent) intentField.get(obj);
                // actionIntent 就是 TestPluginActivity的intent
                Intent actionIntent = intent.getParcelableExtra("actionIntent");
                if (actionIntent != null && actionIntent.getComponent() != null && mPluginActivity.contains(ComponentUtils.componentFullName(actionIntent.getComponent()))) {
                    intentField.set(obj, actionIntent);
                }
            }
            return false;
        }
    }


    //把插件的dexElements 和宿主的dexElements融为一体
    private void pluginToApplication() throws Exception {
        // 1. 找到宿主的 dexElements 得到此对象
        //PathClassLoader pathClassLoader = new PathClassLoader();
        // mApplicationContext.getClassLoader获取得到的本身就是PathClassLoader
        PathClassLoader pathClassLoader = (PathClassLoader) mApplicationContext.getClassLoader();
        Class mBaseDexClassLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader");
        // private final DexPathList pathList;
        Field pathListField = mBaseDexClassLoaderClass.getDeclaredField("pathList");
        pathListField.setAccessible(true);

        Object mDexPathList = pathListField.get(pathClassLoader);
        Field dexElementsField = mDexPathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);

        // 本质就是Element[] dexElements
        Object dexElements = dexElementsField.get(mDexPathList);

        //------------------------------------

        // 2. 找到插件的 dexElements 得到此对象
        // 代表插件 DexClassLoader-- 代表插件
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "p.apk");
        if (!file.exists()) {
            throw new FileNotFoundException("没有找到插件包");
        }
        String pluginPath = file.getAbsolutePath();
        File fileDir = mApplicationContext.getDir("pluginDir", Context.MODE_PRIVATE); // data/data/报名/pluginDir/
        DexClassLoader dexClassLoader = new DexClassLoader(pluginPath, fileDir.getAbsolutePath(), null, mApplicationContext.getClassLoader());
        // 这里是插件的ClassLoader 不能时候用pathClassLoader
        //Object mDexPathList2 = pathListField2.get(pathClassLoader);
        // 创建插件的类加载器 DexClassLoader


        Class mBaseDexClassLoaderClassPlugin = Class.forName("dalvik.system.BaseDexClassLoader");
        // private final DexPathList pathList;
        Field pathListFieldPlugin = mBaseDexClassLoaderClassPlugin.getDeclaredField("pathList");
        pathListFieldPlugin.setAccessible(true);

        Object mDexPathListPlugin = pathListFieldPlugin.get(dexClassLoader);

        Field dexElementsFieldPlugin = mDexPathList.getClass().getDeclaredField("dexElements");
        dexElementsFieldPlugin.setAccessible(true);

        // 本质就是Element[] dexElements
        //Object dexPluginElements = dexElementsFieldPlugin.get(mDexPathList);
        Object dexPluginElements = dexElementsFieldPlugin.get(mDexPathListPlugin);
        // 3. 创建出新的 dexElements[]
        int mainDexLength = Array.getLength(dexElements);
        int pluginDexLength = Array.getLength(dexPluginElements);
        Log.e(TAG, "pluginToApplication: " + pluginDexLength);
        int sumDexLength = mainDexLength + pluginDexLength;
        // 参数一: int[] long[] String[] ... 需要Element[]
        // 参数二：数组的长度
        Object newElements = Array.newInstance(dexElements.getClass().getComponentType(), sumDexLength); // 创建数组对象
        // 4. 宿主Elements+插件Elements -->融合 新的 Elements
        for (int i = 0; i < sumDexLength; i++) {
            // 先融合宿主
            if (i < mainDexLength) {
                // 参数一：新融合的容器 --newDexElements
                Array.set(newElements, i, Array.get(dexElements, i));
            } else {
                // 再融合插件
                Object object = Array.get(dexPluginElements, i - mainDexLength);
                Field dexFileField = object.getClass().getDeclaredField("dexFile");
                dexFileField.setAccessible(true);
                Object dexFileObj = dexFileField.get(object);
                if (dexFileObj instanceof DexFile) {
                    DexFile dexFile = (DexFile) dexFileObj;
                    Enumeration<String> enumeration = dexFile.entries();
                    while (enumeration.hasMoreElements()) {
                        Log.e(TAG, "pluginToApplication: className " + enumeration.nextElement());
                    }
                }
                Array.set(newElements, i, Array.get(dexPluginElements, i - mainDexLength));
            }
        }
        // 5. 把新的Elements设置到宿主中
        dexElementsField.set(mDexPathList, newElements);
        Log.e(TAG, "pluginToApplication: 设置成功");
        // 处理加载插件中的布局
        //doPluginLayoutLoad();
    }


}
