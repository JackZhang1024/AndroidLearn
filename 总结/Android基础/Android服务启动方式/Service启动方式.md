## Android Service 启动方式

Android启动Service的方式有两种，一种是startService(), 一种是bindService()。startService这种启动方式只是单纯的启动一个后台服务，并不涉及和UI层的交互，适用于后台下载文件，播放音乐等操作场景，而bindService这种启动方式是和UI层有绑定的，在UI层有Service的IBinder回调接口，通过这个Binde接口转化成Service中的getBinder方法的返回对象Binder, 这样就暴露了服务端的接口方法，适合需要访问服务端接口的场景，这种启动方式一般和Activity的生命周期一致，在Activity结束的时候，也需要结束这个Service。

### startService

```java
public class LearnStartService extends Service {
    private static final String TAG = "StartServiceLearn";

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "StartServiceLearn");
        builder.setContentTitle("StartServiceTitle");
        builder.setContentText("StartServiceText");
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        // startForeground可以提高进程的优先级 不容易被系统杀死
        startForeground(1, notification);
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
                System.out.println("doSomethings...");
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
}

// 开启服务
Intent intent = new Intent(this, LearnStartService.class);
intent.putExtra(SERVICE_PARAM, "helloWorld");
startService(intent);

// 结束服务 
stopService(new Intent(this, LearnStartService.class));
```
### bindService

```java

// 服务端代码
public class LearnBindService extends Service{

    private static final String TAG = "LearnBindService";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind: ");
        return new BindServiceBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    class BindServiceBinder extends Binder{
        public LearnBindService getService(){
            return LearnBindService.this;
        }
    }
}

// 客户端代码

// 绑定服务
Intent intent = new Intent(this, LearnBindService.class);
bindService(intent, bindServiceConnection, Service.BIND_AUTO_CREATE);

private ServiceConnection bindServiceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.e(TAG, "onServiceConnected: ");
        LearnBindService.BindServiceBinder binder = (LearnBindService.BindServiceBinder) service;
        LearnBindService learnBindService = binder.getService();
        // 获取到Service的引用，那么就可以调用Service中的暴露出来的接口方法
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.e(TAG, "onServiceDisconnected: ");
    }

};

// 结束服务 一般是在Activity的onDestroy方法中调用
unbindService(bindServiceConnection);

```

总结：startService这种方式启动的service之后，再次调用的startService的时候，只会再次回调onStartCommand()方法，onCreate()只在创建的时候回调一次。


