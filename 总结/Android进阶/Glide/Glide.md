- [Glide](#glide)
  - [Glide常见使用API](#glide常见使用api)
  - [Glide的原理分析](#glide的原理分析)
    - [Glide加载和Activity的生命周期一致](#glide加载和activity的生命周期一致)
## Glide
### Glide常见使用API
Glide是Google推荐的图片加载库，比Picasso加载快，但是需要更大的内存来缓存。Glide不只接收Context, 还接受Activity,Fragemnt，图片会和Activity,Fragment的生命周期一致，onResume恢复加载图片，onPause暂停载图片。同时支持Gif图片。
```java
with(Context context). // 使用Application上下文，Glide请求将不受Activity/Fragment生命周期控制。
with(Activity activity).// 使用Activity作为上下文，Glide的请求会受到Activity生命周期控制。
with(FragmentActivity activity). //Glide的请求会受到FragmentActivity生命周期控制。
with(android.app.Fragment fragment). //Glide的请求会受到Fragment 生命周期控制。
with(android.support.v4.app.Fragment fragment). //Glide的请求会受到Fragment生命周期控制。
```
```java
// 资源加载
// Glide基本可以load任何可以拿到的媒体资源，如：
load("file://"+ Environment.getExternalStorageDirectory().getPath()+"/test.jpg") // load SD卡资源：
load("file:///android_asset/f003.gif") // load assets资源
load("android.resource://com.frank.glide/raw/raw_1") // load raw资源
load("android.resource://com.frank.glide/raw/"+R.raw.raw_1)  // load raw资源
load("android.resource://com.frank.glide/drawable/news") // load drawable资源
load("android.resource://com.frank.glide/drawable/"+R.drawable.news) // load drawable资源
load("content://media/external/images/media/139469") // load ContentProvider资源
load("http://img.my.csdn.net/uploads/201508/05/1438760757_3588.jpg") // load http资源：
load("https://img.alicdn.com/tps/TB1uyhoMpXXXXcLXVXXXXXXXXXX-476-538.jpg_240x5000q50.jpg")
// 当然，load不限于String类型，还可以：
load(Uri uri)，load(File file)，load(Integer resourceId)，
load(URL url)，load(byte[] model, final String id)，
load(byte[] model)，load(T model)。
// 而且可以使用自己的ModelLoader进行资源加载：
using(ModelLoader<A, T> modelLoader, Class<T> dataClass)，using(final StreamModelLoader<T> modelLoader)，using(StreamByteArrayLoader modelLoader)，using(final FileDescriptorModelLoader<T> modelLoader)。
// 返回RequestBuilder实例
```
```java
//请求给定系数的缩略图。如果缩略图比全尺寸图先加载完，就显示缩略图，否则就不显示。系数sizeMultiplier必须在(0,1)之间，
//可以递归调用该方法。
thumbnail(float sizeMultiplier) 
//在加载资源之前给Target大小设置系数
sizeMultiplier(float sizeMultiplier)
//设置是否跳过内存缓存，但不保证一定不被缓存 比如请求已经在加载资源且没设置跳过内存缓存，这个资源就会被缓存在内存中
skipMemoryCache(boolean skip)
//设置缓存策略
diskCacheStrategy(DiskCacheStrategy strategy)
//缓存原始数据
DiskCacheStrategy.SOURCE
//缓存变换修改后的资源数据
DiskCacheStrategy.RESULT
//什么都不缓存
DiskCacheStrategy.NONE：
//缓存所有图片 
DiskCacheStrategy.ALL
//默认采用DiskCacheStrategy.RESULT策略，对于download only操作要使用DiskCacheStrategy.SOURCE。

//指定加载的优先级，优先级越高越优先加载，但不保证所有图片都按序加载
//枚举Priority.IMMEDIATE，Priority.HIGH，Priority.NORMAL，Priority.LOW。默认为Priority.NORMAL。
priority(Priority priority)
    
//设置淡入淡出效果，默认300ms，可以传参    
crossFade(5000)
//移除所有的动画 
dontAnimate()
//在异步加载资源完成时会执行该动画
animate(int animationId)
//在异步加载资源完成时会执行该动画
animate(ViewPropertyAnimation.Animator animator)

//设置资源加载过程中的占位Drawable
placeholder(int resourceId)
//设置资源加载过程中的占位Drawable
placeholder(Drawable drawable)
//设置load失败时显示的Drawable
error(int resourceId)
//设置load失败时显示的Drawable
error(Drawable drawable)

//Glide支持两种图片缩放形式，CenterCrop 和 FitCenter
CenterCrop // 等比例缩放图片， 直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。
FitCenter  // 等比例缩放图片，宽或者是高等于ImageView的宽或者是高。

//当列表在滑动的时候，调用pauseRequests()取消请求，滑动停止时，调用resumeRequests()恢复请求

listener(RequestListener<? super ModelType, TranscodeType> requestListener).
//监听资源加载的请求状态，可以使用两个回调
//但不要每次请求都使用新的监听器，要避免不必要的内存申请，可以使用单例进行统一的异常监听和处理。
onResourceReady(R resource, T model, Target<R> target, boolean isFromMemoryCache, boolean isFirstResource)
onException(Exception e, T model, Target<R> target, boolean isFirstResource)

//清除掉所有的图片加载请求
clear() 
//重新设置Target的宽高值（单位为pixel）
override(int width, int height)
//设置资源将被加载到的Target
into(Y target)
//设置资源将被加载到的ImageView。取消该ImageView之前所有的加载并释放资源
into(ImageView view)
//后台线程加载时要加载资源的宽高值（单位为pixel)
into(int width, int height)
//预加载resource到缓存中（单位为pixel)
preload(int width, int height)
//无论资源是不是gif动画，都作为Bitmap对待。如果是gif动画会停在第一帧。
asBitmap()
//把资源作为GifDrawable对待。如果资源不是gif动画将会失败，会回调.error() 
asGif()
```
### Glide的原理分析
#### Glide加载和Activity的生命周期一致
```java
//Glide.java
public static RequestManager with(Activity activity) {
    RequestManagerRetriever retriever = RequestManagerRetriever.get();
    return retriever.get(activity);
}

//RequestManagerRetriever.java
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public RequestManager get(Activity activity) {
    if (Util.isOnBackgroundThread() || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
        return get(activity.getApplicationContext());
    } else {
        // 一般正常走这块
        assertNotDestroyed(activity);
        android.app.FragmentManager fm = activity.getFragmentManager();
        return fragmentGet(activity, fm);
    }
}

//获取属于该页面的页面请求管理 RequestManager
//RequestManagerFragment继承自Fragment
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
RequestManager fragmentGet(Context context, android.app.FragmentManager fm) {
    RequestManagerFragment current = getRequestManagerFragment(fm);
    RequestManager requestManager = current.getRequestManager();
    if (requestManager == null) {
        requestManager = new RequestManager(context, current.getLifecycle(), current.getRequestManagerTreeNode());
        current.setRequestManager(requestManager);
    }
    return requestManager;
}

//继承自Fragment,在拥有了声明周期的回调，同时拥有RequestManager
//这样就可以通过生命周期回调来控制请求的发送
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RequestManagerFragment extends Fragment {
    // 这个是一个没有这View的Fragment
    ...
    ActivityFragmentLifecycle getLifecycle() {
        return lifecycle;
    }
    
    @Override
    public void onStart() {
        super.onStart();
        lifecycle.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        lifecycle.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycle.onDestroy();
    }
     
    // 注意，在RequestManagerFragment中的lifeCycle正好是ActivityFragmetnLifecycle对象
    // 所以，在生命周期回调的时候，便会调用对应的请求处理方法
    // lifecycle.onStart() , lifecycle.onStop() 
    public RequestManagerFragment() {
        this(new ActivityFragmentLifecycle());
    }
  
}
```
```java
//ActivityFragmentLifecycle对象
class ActivityFragmentLifecycle implements Lifecycle {
    private final Set<LifecycleListener> lifecycleListeners =
            Collections.newSetFromMap(new WeakHashMap<LifecycleListener, Boolean>());
    private boolean isStarted;
    private boolean isDestroyed;

    @Override
    public void addListener(LifecycleListener listener) {
        lifecycleListeners.add(listener);
        if (isDestroyed) {
            listener.onDestroy();
        } else if (isStarted) {
            listener.onStart();
        } else {
            listener.onStop();
        }
    }

    void onStart() {
        isStarted = true;
        for (LifecycleListener lifecycleListener : Util.getSnapshot(lifecycleListeners)) {
            // 启动请求
            // lifecycleListener就是ReuestManager对象 
            lifecycleListener.onStart();
        }
    }

    void onStop() {
        isStarted = false;
        for (LifecycleListener lifecycleListener : Util.getSnapshot(lifecycleListeners)) {
            lifecycleListener.onStop();
        }
    }

    void onDestroy() {
        isDestroyed = true;
        for (LifecycleListener lifecycleListener : Util.getSnapshot(lifecycleListeners)) {
            lifecycleListener.onDestroy();
        }
    }
}
```
```java
//RequestManager.java
//此处就是接的上面的start()方法 
@Override
public void onStart() {
    // onStart might not be called because this object may be created after the fragment/activity's onStart method.
    resumeRequests();
}

public void resumeRequests() {
    Util.assertMainThread();
    //此处是重点！！！
    requestTracker.resumeRequests();
}
```
```java
//RequestTracker.java
public void resumeRequests() {
    isPaused = false;
    for (Request request : Util.getSnapshot(requests)) {
        if (!request.isComplete() && !request.isCancelled() && !request.isRunning()) {
            // 如果请求没有结束或者没有被取消或者没有正在运行 则开始请求
            request.begin();
        }
    }
    pendingRequests.clear();
}
```
```java
//GenericRequest.java
public void begin() {
    startTime = LogTime.getLogTime();
    if (model == null) {
        onException(null);
        return;
    }
    status = Status.WAITING_FOR_SIZE;
    if (Util.isValidDimensions(overrideWidth, overrideHeight)) {
        //开始请求数据
        onSizeReady(overrideWidth, overrideHeight);
    } else {
        target.getSize(this);
    }
    if (!isComplete() && !isFailed() && canNotifyStatusChanged()) {
        target.onLoadStarted(getPlaceholderDrawable());
    }
    if (Log.isLoggable(TAG, Log.VERBOSE)) {
        logV("finished run method in " + LogTime.getElapsedMillis(startTime));
    }
}

@Override
public void onSizeReady(int width, int height) {   
    status = Status.RUNNING;
    width = Math.round(sizeMultiplier * width);
    height = Math.round(sizeMultiplier * height);

    ModelLoader<A, T> modelLoader = loadProvider.getModelLoader();
    final DataFetcher<T> dataFetcher = modelLoader.getResourceFetcher(model, width, height);
    if (dataFetcher == null) {
       onException(new Exception("Failed to load model: \'" + model + "\'"));
       return;
    }
    ResourceTranscoder<Z, R> transcoder = loadProvider.getTranscoder();
    if (Log.isLoggable(TAG, Log.VERBOSE)) {
        logV("finished setup for calling load in " + LogTime.getElapsedMillis(startTime));
    }
    loadedFromMemoryCache = true;
    //engine是获取数据的关键点 获取网络图片的引擎
    loadStatus = engine.load(signature, width, height, dataFetcher, loadProvider, transformation, transcoder, priority, isMemoryCacheable, diskCacheStrategy, this);
    loadedFromMemoryCache = resource != null;
    ...
}
```
```java
//Engine.java
 public <T, Z, R> LoadStatus load(Key signature, int width, int height, DataFetcher<T> fetcher,
            DataLoadProvider<T, Z> loadProvider, Transformation<Z> transformation, ResourceTranscoder<Z, R> transcoder,
            Priority priority, boolean isMemoryCacheable, DiskCacheStrategy diskCacheStrategy, ResourceCallback cb) {
        EngineJob engineJob = engineJobFactory.build(key, isMemoryCacheable);
        DecodeJob<T, Z, R> decodeJob = new DecodeJob<T, Z, R>(key, width, height, fetcher, loadProvider, transformation, transcoder, diskCacheProvider, diskCacheStrategy, priority);
        EngineRunnable runnable = new EngineRunnable(engineJob, decodeJob, priority);
        jobs.put(key, engineJob);
        engineJob.addCallback(cb);
        // 启动线程开始获取数据
        engineJob.start(runnable);
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            logWithTimeAndKey("Started new load", startTime, key);
        }
        return new LoadStatus(cb, engineJob);
}
```
```java
//EngineJob.java
public void start(EngineRunnable engineRunnable) {
    this.engineRunnable = engineRunnable;
    future = diskCacheService.submit(engineRunnable);
}
```
```java
//EngineRunnable.java
class EngineRunnable implements Runnable, Prioritized {
   
   @Override
    public void run() {
       ...
        try {
            //decode() 获取数据，开始下载图片操作 
            resource = decode();
        } catch (Exception e) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Exception decoding", e);
            }
            exception = e;
        }
        ...
    }
}
```
```java
//DecodeJob.java
private Resource<T> decodeSource() throws Exception {
    Resource<T> decoded = null;
    try {
        long startTime = LogTime.getLogTime();
        //1. fetcher是HttpUrlFetcher类型的
        final A data = fetcher.loadData(priority);
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            logWithTimeAndKey("Fetched data", startTime);
        }
        if (isCancelled) {
            return null;
        }
        //2. 将流转化成我们需要的格式 
        decoded = decodeFromSourceData(data);
    } finally {
        fetcher.cleanup();
    }
    return decoded;
}

// 将图片从流转换成Bitmap,并且缓存在缓存中
private Resource<T> decodeFromSourceData(A data) throws IOException {
    final Resource<T> decoded;
    if (diskCacheStrategy.cacheSource()) {
        decoded = cacheAndDecodeSourceData(data);
    } else {
        long startTime = LogTime.getLogTime();
        decoded = loadProvider.getSourceDecoder().decode(data, width, height);
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            logWithTimeAndKey("Decoded from source", startTime);
        }
    }
    return decoded;
}

```
```java
//HttpUrlFetcher.java
@Override
public InputStream loadData(Priority priority) throws Exception {
     return loadDataWithRedirects(glideUrl.toURL(), 0 /*redirects*/, null /*lastUrl*/, 
     glideUrl.getHeaders());
}
```

