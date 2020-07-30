## LoadedApk式插件化
### LoadedApk插件化原理
占位式插件化使用的是宿主的Activity,Context，注入到插件中, Hook式插件化是将宿主和插件的elements进行融合，在插件中可以随意使用
this, 既然融合在一起，插件可以使用到宿主的环境，插件越多，内存中的newDexElements就会越来越大。LoadedApk式插件化框架是我们自己手写ClassLoader, PathClassLoader用于加载宿主的class，自定义的ClassLoader用于加载插件的class, 这样就解决了内存中的newDexElements越来越大的问题。

ActivityThread源码分析
startActivity->Activity->Instrumentation->AMS检查->ActivityThread->mH->LAUNCH_ACTIVITY 自己处理LoadedApk中的ClassLoader
```java
public void handleMessage(Message msg) {
    if (DEBUG_MESSAGES) Slog.v(TAG, ">>> handling: " + codeToString(msg.what));
    switch (msg.what) {
        case LAUNCH_ACTIVITY: {
            Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityStart");
            final ActivityClientRecord r = (ActivityClientRecord) msg.obj;
            //如果缓存mPackages中有LoadedApk 就直接返回，如果没有LoadedApk就创建出LoadedApk
            //如果是加载插件，从mPackages取出，插件专用的LoaedApk.自定义ClassLoader
            r.packageInfo = getPackageInfoNoCheck(
                    r.activityInfo.applicationInfo, r.compatInfo);
            //实例化真正的Activity 然后启动        
            handleLaunchActivity(r, null);
            Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
        } break;
  ...
}
```
1.public final LoadedApk getPackageInfoNoCheck == 宿主的

2.缓存中的 final ArrayMap<String, WeakReference> mPackages 默认保存的是宿主的LoadedApk

LoadedApk — 宿主的 ----> LoadedApk.ClassLoader —> 宿主中的class
java.lang.ClassLoader cl = r.packageInfo.getClassLoader(); // LoadedApk里面的ClassLoader
(Activity)cl.loadClass(className).newInstance(); 实例化的Activity --> 宿主的 LoadedApk里面的ClassLoader 去实例化的

以上代码结论：宿主的LoadedApk.ClassLoader 去加载 宿主中的class，然后实例化的

— > 自定义一个LoadedApk 自定义一个ClassLoader 用于专门加载插件里面的class，然后实例化

自定义一个 LoadedApk 然后保存 --> mPackages

LoadedApk — 插件的 ----> LoadedApk.ClassLoader —> 插件中的class

3.梳理流程：
宿主： startActivity —> Activity -->Instrumentation —> AMS检查 -->ActivityThread
mPackages.value取出 LoadedApk.ClassLoader —> 实例化Activity （只能加载宿主的）

我们在取出之前，需要自定义一个 （插件专用的 LoadedApk 自定义ClassLoader） 添加到 --> mPackages

startActivity ---> Activity -->Instrumentation ---> AMS检查 -->ActivityThread
mPackages.value取出 插件专用的LoadedApk.ClassLoader --> 实例化插件的Activity
### LoadedApk插件化实践

