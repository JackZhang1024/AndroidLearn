- [EventBus使用及原理分析](#eventbus使用及原理分析)
  - [EventBus简单实用](#eventbus简单实用)
  - [EventBus原理分析](#eventbus原理分析)
    - [EventBus订阅](#eventbus订阅)
    - [EventBus发送事件](#eventbus发送事件)
    - [EventBus线程切换](#eventbus线程切换)
    - [EventBus粘滞事件](#eventbus粘滞事件)
    - [EventBus的线程模式](#eventbus的线程模式)
## EventBus使用及原理分析
### EventBus简单实用
```java
public class EventContainer {

    private static final String TAG = "EventContainer";

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandleEvent(Event event) {
        Log.e(TAG, "onHandleEvent: code " + event.getCode() 
        + " msg " + event.getMsg() + " ThreadName " + Thread.currentThread().getName());
    }

    // ThreadMode.MAIN 适合用于处理任务耗时短的操作
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandleStickyEvent(StickyEvent event) {
        Log.e(TAG, "onHandleStickyEvent: code " + event.getCode()
        + " msg " + event.getMsg() + " ThreadName " + Thread.currentThread().getName());
    }

    static class Event {
        private String msg;
        private int code;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

    // 粘滞事件 就是先发送事件 然后再注册监听事件
    static class StickyEvent {
        private String msg;
        private int code;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}

public class EventBusActivity extends AppCompatActivity {

    private static final String TAG = "EventBusActivity";
    private EventContainer mEventContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus);
        ButterKnife.bind(this);
        mEventContainer = new EventContainer();
        registerEventListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterEventListener();
    }

    @OnClick(R.id.btn_send)
    public void onSendMessageClick() {
        new Thread(()->{
            EventContainer.Event event = new EventContainer.Event();
            event.setCode(100);
            event.setMsg("我来自工作线程");
            EventBus.getDefault().post(event);
        }).start();
    }

    @OnClick(R.id.btn_send_sticky)
    public void onSendStickyMessageClick(){
        EventContainer.StickyEvent event = new EventContainer.StickyEvent();
        event.setCode(101);
        event.setMsg("我来主线程，我是粘滞消息");
        EventBus.getDefault().postSticky(event);
        startActivity(new Intent(this, EventBusSecondActivity.class));
    }

    private void registerEventListener() {
        EventBus.getDefault().register(mEventContainer);
    }

    private void unregisterEventListener(){
        EventBus.getDefault().unregister(mEventContainer);
    }
}

public class EventBusSecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus_second);
        ButterKnife.bind(this);
        registerEventListener();
    }

    private void registerEventListener() {
        EventContainer container = new EventContainer();
        EventBus.getDefault().register(container);
    }
}
```
### EventBus原理分析

#### EventBus订阅
```java
/**
 * Registers the given subscriber to receive events. Subscribers must call {@link #unregister(Object)} once they
 * are no longer interested in receiving events.
 * <p/>
 * Subscribers have event handling methods that must be annotated by {@link Subscribe}.
 * The {@link Subscribe} annotation also allows configuration like {@link
 * ThreadMode} and priority.
 */
public void register(Object subscriber) {
    Class<?> subscriberClass = subscriber.getClass();
    // 1. 查找subscriber所属的类的所有被@Subscribe注解的方法
    List<SubscriberMethod> subscriberMethods = subscriberMethodFinder
    .findSubscriberMethods(subscriberClass);
    synchronized (this) {
          for (SubscriberMethod subscriberMethod : subscriberMethods) {
              subscribe(subscriber, subscriberMethod);
        }
    }
}

// SubScribeMethodFinder.java
List<SubscriberMethod> findSubscriberMethods(Class<?> subscriberClass) {
    List<SubscriberMethod> subscriberMethods = METHOD_CACHE.get(subscriberClass);
    if (subscriberMethods != null) {
        return subscriberMethods;
    }
    // 此处ignoreGenerateIndex默认是false
    if (ignoreGeneratedIndex) {
        subscriberMethods = findUsingReflection(subscriberClass);
    } else {
        // 查找注册方法 
        subscriberMethods = findUsingInfo(subscriberClass);
    }
    if (subscriberMethods.isEmpty()) {
        throw new EventBusException("Subscriber " + subscriberClass
                + " and its super classes have no public methods with the @Subscribe annotation");
    } else {
        METHOD_CACHE.put(subscriberClass, subscriberMethods);
        return subscriberMethods;
    }
}

// 查找注册方法
private List<SubscriberMethod> findUsingInfo(Class<?> subscriberClass) {
    FindState findState = prepareFindState();
    findState.initForSubscriber(subscriberClass);
    while (findState.clazz != null) {
        findState.subscriberInfo = getSubscriberInfo(findState);
        if (findState.subscriberInfo != null) {
            SubscriberMethod[] array = findState.subscriberInfo.getSubscriberMethods();
            for (SubscriberMethod subscriberMethod : array) {
                if (findState.checkAdd(subscriberMethod.method, subscriberMethod.eventType)) {
                    findState.subscriberMethods.add(subscriberMethod);
                }
            }
        } else {
            // 断点跟踪到这块 获取方法
            findUsingReflectionInSingleClass(findState);
        }
        findState.moveToSuperclass();
    }
    return getMethodsAndRelease(findState);
}

private void findUsingReflectionInSingleClass(FindState findState) {
    Method[] methods;
    try {
        // This is faster than getMethods, especially when subscribers are fat classes like Activities
        methods = findState.clazz.getDeclaredMethods();
    } catch (Throwable th) {
        // Workaround for java.lang.NoClassDefFoundError, see https://github.com/greenrobot/EventBus/issue149
       methods = findState.clazz.getMethods();
       findState.skipSuperClasses = true;
    }
    for (Method method : methods) {
          if ((modifiers & Modifier.PUBLIC) != 0 && (modifiers & MODIFIERS_IGNORE) == 0) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 1) {
                // 
                Subscribe subscribeAnnotation = method.getAnnotation(Subscribe.class);
                if (subscribeAnnotation != null) {
                    Class<?> eventType = parameterTypes[0];
                    //1. 防止重复添加方法
                    if (findState.checkAdd(method, eventType)) {
                        ThreadMode threadMode = subscribeAnnotation.threadMode();
                        //2. 向findState的subscribeMethods的列表添加查找出被@Subscribe注解的方法
                        findState.subscriberMethods.add(new SubscriberMethod(method, eventType, threadMode,
                                subscribeAnnotation.priority(), subscribeAnnotation.sticky()));
                    }
                }
            } else if (strictMethodVerification && method.isAnnotationPresent(Subscribe.class)) {
              // 被@Subscribe注解的方法只能有一个参数
              // method.isAnnotataionPresenter(Subscribe.class) methods是否被@Subscribe注解
                  throw new EventBusException("@Subscribe method " + methodName +
                        "must have exactly 1 parameter but has " + parameterTypes.length);
            }
        } else if (strictMethodVerification && method.isAnnotationPresent(Subscribe.class)) {
            String methodName = method.getDeclaringClass().getName() + "." + method.getName();
            throw new EventBusException(methodName +
                    " is a illegal @Subscribe method: must be public, non-static, and non-abstract");
        }
    }
}

// SubScribeMethodFinder类中的静态内部类
static class FindState {
    final List<SubscriberMethod> subscriberMethods = new ArrayList<>();
    final Map<Class, Object> anyMethodByEventType = new HashMap<>();
    final Map<String, Class> subscriberClassByMethodKey = new HashMap<>();
    final StringBuilder methodKeyBuilder = new StringBuilder(128);

    Class<?> subscriberClass;
    Class<?> clazz;
    boolean skipSuperClasses;
    SubscriberInfo subscriberInfo;
    ...
}
```
```java
 // Must be called in synchronized block
private void subscribe(Object subscriber, SubscriberMethod subscriberMethod) {
    Class<?> eventType = subscriberMethod.eventType; //类似 Event.class
    Subscription newSubscription = new Subscription(subscriber, subscriberMethod);
    CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
    if (subscriptions == null) {
        subscriptions = new CopyOnWriteArrayList<>();
        // 下面表示一个事件类型对应着一个事件订阅列表
        subscriptionsByEventType.put(eventType, subscriptions);
    } else {
        if (subscriptions.contains(newSubscription)) {
            throw new EventBusException("Subscriber " + subscriber.getClass() 
            + " already registered to event "+ eventType);
        }
       int size = subscriptions.size();
        for (int i = 0; i <= size; i++) {
            if (i == size || subscriberMethod.priority > subscriptions.get(i).subscriberMethod.priority) {
            subscriptions.add(i, newSubscription);
            break;
        }
    }
    // 一个订阅者有对应一个Event列表 Map<Object, List<Class<?>>> typesBySubscriber;
    List<Class<?>> subscribedEvents = typesBySubscriber.get(subscriber);
    if (subscribedEvents == null) {
        subscribedEvents = new ArrayList<>();
        typesBySubscriber.put(subscriber, subscribedEvents);
    }
    subscribedEvents.add(eventType);
    if (subscriberMethod.sticky) {
        if (eventInheritance) {
            // Existing sticky events of all subclasses of eventType have to be considered.
            // Note: Iterating over all events may be inefficient with lots of sticky events,
            // thus data structure should be changed to allow a more efficient lookup
            // (e.g. an additional map storing sub classes of super classes: Class -> List<Class>).
            Set<Map.Entry<Class<?>, Object>> entries = stickyEvents.entrySet();
            for (Map.Entry<Class<?>, Object> entry : entries) {
                Class<?> candidateEventType = entry.getKey();
                if (eventType.isAssignableFrom(candidateEventType)) {
                    Object stickyEvent = entry.getValue();
                    checkPostStickyEventToSubscription(newSubscription, stickyEvent);
                }
            }
        } else {
            Object stickyEvent = stickyEvents.get(eventType);
            checkPostStickyEventToSubscription(newSubscription, stickyEvent);
        }
    }
}
```
总体上通过上面的分析，我们可以得知，在register(container)的操作中，会将Container中的所有被@Subscribe注解的方法都存储到一个

#### EventBus发送事件
```java
EventBus.java 
/** Posts the given event to the event bus. */
public void post(Object event) {
    PostingThreadState postingState = currentPostingThreadState.get();
    List<Object> eventQueue = postingState.eventQueue;
    eventQueue.add(event);
    if (!postingState.isPosting) {
        postingState.isMainThread = isMainThread();
        postingState.isPosting = true;
        if (postingState.canceled) {
            throw new EventBusException("Internal error. Abort state was not reset");
        }
        try {
            while (!eventQueue.isEmpty()) {
                postSingleEvent(eventQueue.remove(0), postingState);
            }
        } finally {
            postingState.isPosting = false;
            postingState.isMainThread = false;
        }
    }
}

// 最终发送事件调用到此处
private boolean postSingleEventForEventType(Object event, PostingThreadState postingState, 
Class<?> eventClass) {
    CopyOnWriteArrayList<Subscription> subscriptions;
    synchronized (this) {
        // 按照时间类型获取到含有所有订阅Subscription的列表
        subscriptions = subscriptionsByEventType.get(eventClass);
    }
    if (subscriptions != null && !subscriptions.isEmpty()) {
        for (Subscription subscription : subscriptions) {
            postingState.event = event;
            postingState.subscription = subscription;
            boolean aborted = false;
            try {
                // postToSubscription
                postToSubscription(subscription, event, postingState.isMainThread);
                aborted = postingState.canceled;
            } finally {
                postingState.event = null;
                postingState.subscription = null;
                postingState.canceled = false;
           }
           if (aborted) {
              break;
           }
        }
        return true;
    }
  return false;
}

// 事件的发送
private void postToSubscription(Subscription subscription, Object event, boolean isMainThread) {
    switch (subscription.subscriberMethod.threadMode) {
        case POSTING:
            invokeSubscriber(subscription, event);
            break;
        case MAIN:
            if (isMainThread) {
                invokeSubscriber(subscription, event);
            } else {
                mainThreadPoster.enqueue(subscription, event);
            }
            break;
        case MAIN_ORDERED:
            if (mainThreadPoster != null) {
                mainThreadPoster.enqueue(subscription, event);
            } else {
                // temporary: technically not correct as poster not decoupled from subscriber
                invokeSubscriber(subscription, event);
            }
            break;
        case BACKGROUND:
            if (isMainThread) {
                backgroundPoster.enqueue(subscription, event);
            } else {
                invokeSubscriber(subscription, event);
            }
            break;
        case ASYNC:
            asyncPoster.enqueue(subscription, event);
            break;
        default:
            throw new IllegalStateException("Unknown thread mode: " + subscription.subscriberMethothreadMode);
    }
}

// invokeSubscriber()
void invokeSubscriber(Subscription subscription, Object event) {
    try {
        // 这块用反射来最后回调事件处理方法
        // 思考，为啥不直接用Subscriber来会回调方法? 虽然我们在注册的时候可以获取多有的订阅者对象，
        // 但是订阅者对象不同，方法也不同，但是方法都有两个共同的特点
        // 1.被@Subscribe进行了注解 2.被注解的方法参数只有一个且相同
        // 我们可以通过反射来考虑，如果把方法都集合起来，最后利用method.invoke(obj, param)
        // 我们不需要知道method具体是啥，也不需要知道subscriptio.subscriber具体是啥（其实定义成了Object）
        // 但是就是可以调用，那就顺序完成了被注解的方法回调
        subscription.subscriberMethod.method.invoke(subscription.subscriber, event);
    } catch (InvocationTargetException e) {
        handleSubscriberException(subscription, event, e.getCause());
    } catch (IllegalAccessException e) {
        throw new IllegalStateException("Unexpected exception", e);
    }
}
```

#### EventBus线程切换
```java
EventBus.java

private void postToSubscription(Subscription subscription, Object event, boolean isMainThread) {
    switch (subscription.subscriberMethod.threadMode) {
        ....
        case MAIN:
            if (isMainThread) {
                invokeSubscriber(subscription, event);
            } else {
                // 发送事件的线程不是主线程，那么被注解的方法需要在主线程中执行
                // 那么，需要线程切换，将消息发送到主线程的消息队列中？
                mainThreadPoster.enqueue(subscription, event);
            }
            break;
    }
    ...
}

// 在EventBus的构造方法中进行初始化
mainThreadPoster = mainThreadSupport != null ? mainThreadSupport.createPoster(this) : null;

// 创建MainThreadSupport对象
MainThreadSupport getMainThreadSupport() {
    if (mainThreadSupport != null) {
        return mainThreadSupport;
    } else if (Logger.AndroidLogger.isAndroidLogAvailable()) {
        Object looperOrNull = getAndroidMainLooperOrNull();
        return looperOrNull == null ? null :
                new MainThreadSupport.AndroidHandlerMainThreadSupport((Looper) looperOrNull);
    } else {
        return null;
    }
}
```

```java
// MainThreadSupport.java
public interface MainThreadSupport {
    boolean isMainThread();
    Poster createPoster(EventBus eventBus);

    class AndroidHandlerMainThreadSupport implements MainThreadSupport {
        private final Looper looper;
        public AndroidHandlerMainThreadSupport(Looper looper) {
            this.looper = looper;
        }

        @Override
        public boolean isMainThread() {
            return looper == Looper.myLooper();
        }

        @Override
        public Poster createPoster(EventBus eventBus) {
            // 创建HandlerPoster后，然后调用enqueue(subscription, event)
            return new HandlerPoster(eventBus, looper, 10);
        }
    }
}
```

```java
//HandlerPoster.java
public class HandlerPoster extends Handler implements Poster {
    private final PendingPostQueue queue;
    private final int maxMillisInsideHandleMessage;
    private final EventBus eventBus;
    private boolean handlerActive;

    protected HandlerPoster(EventBus eventBus, Looper looper, int maxMillisInsideHandleMessage) {
        super(looper);
        this.eventBus = eventBus;
        this.maxMillisInsideHandleMessage = maxMillisInsideHandleMessage;
        queue = new PendingPostQueue();
    }

    public void enqueue(Subscription subscription, Object event) {
        PendingPost pendingPost = PendingPost.obtainPendingPost(subscription, event);
        synchronized (this) {
            // 将事件订阅subscription和事件发送到队列中
            queue.enqueue(pendingPost);
            if (!handlerActive) {
                handlerActive = true;
                if (!sendMessage(obtainMessage())) {
                    throw new EventBusException("Could not send handler message");
                }
            }
        }
    }

    @Override
    public void handleMessage(Message msg) {
        boolean rescheduled = false;
        try {
            long started = SystemClock.uptimeMillis();
            while (true) {
                PendingPost pendingPost = queue.poll();
                if (pendingPost == null) {
                    synchronized (this) {
                        // Check again, this time in synchronized
                        pendingPost = queue.poll();
                        if (pendingPost == null) {
                            handlerActive = false;
                            return;
                        }
                    }
                }
                // 最终又回调EventBus对象的invokeSubscriber()方法
                eventBus.invokeSubscriber(pendingPost);
                long timeInMethod = SystemClock.uptimeMillis() - started;
                if (timeInMethod >= maxMillisInsideHandleMessage) {
                    if (!sendMessage(obtainMessage())) {
                        throw new EventBusException("Could not send handler message");
                    }
                    rescheduled = true;
                    return;
                }
            }
        } finally {
            handlerActive = rescheduled;
        }
    }
}
```
```java
/**
* Invokes the subscriber if the subscriptions is still active. Skipping subscriptions prevents race conditions
* between {@link #unregister(Object)} and event delivery. Otherwise the event might be delivered after the
* subscriber unregistered. This is particularly important for main thread delivery and registrations bound to the live cycle of an Activity or Fragment.
*/
void invokeSubscriber(PendingPost pendingPost) {
    Object event = pendingPost.event;
    Subscription subscription = pendingPost.subscription;
    PendingPost.releasePendingPost(pendingPost);
    if (subscription.active) {
        // 最终利用反射来调用事件回调方法
        invokeSubscriber(subscription, event);
    }
}
```

#### EventBus粘滞事件
粘滞事件就是在事件发送出去以后，然后再订阅的时候，依然能够收到发送的事件
```java
// EventBus.java
/**
* Posts the given event to the event bus and holds on to the event (because it is sticky). The most recent sticky event of an event's type is kept in memory for future access by subscribers using {@link Subscribe#sticky()}.
*/
public void postSticky(Object event) {
    synchronized (stickyEvents) {
        stickyEvents.put(event.getClass(), event);
    }
    // Should be posted after it is putted, in case the subscriber wants to remove immediately
    post(event);
}
```
```java
// EventBus.java
private void subscribe(Object subscriber, SubscriberMethod subscriberMethod) {
     ...
    if (subscriberMethod.sticky) {
        if (eventInheritance) {
              
        } else { 
            Object stickyEvent = stickyEvents.get(eventType);
            // 在subscribe的时候就要准备处理回调事件
            checkPostStickyEventToSubscription(newSubscription, stickyEvent);
        }
    }
}

private void checkPostStickyEventToSubscription(Subscription newSubscription, Object stickyEvent) {
    if (stickyEvent != null) {
        // 最终和普通的事件回调一样，利用反射和subscriber来完成事件回调
        postToSubscription(newSubscription, stickyEvent, isMainThread());
     }
}
```

#### EventBus的线程模式

```java
// ThreadMode.java
// 每个subscriber方法都有一个线程模式，这个线程模式决定了这个方法将在那个线程里被EventBus。这些线程是独立于发送事件的线程（和发送事件的线程没有依赖关系）。

public enum ThreadMode {
    /**
     * Subscriber will be called directly in the same thread, which is posting the event. This is the default. Event delivery
     * implies the least overhead because it avoids thread switching completely. Thus this is the recommended mode for
     * simple tasks that are known to complete in a very short time without requiring the main thread. Event handlers
     * using this mode must return quickly to avoid blocking the posting thread, which may be the main thread.
     */
    POSTING,

    /**
     * On Android, subscriber will be called in Android's main thread (UI thread). If the posting thread is
     * the main thread, subscriber methods will be called directly, blocking the posting thread. Otherwise the event
     * is queued for delivery (non-blocking). Subscribers using this mode must return quickly to avoid blocking the main thread.
     * If not on Android, behaves the same as {@link #POSTING}.
     * 这种的可能会发生线程阻塞情况，所以尽量不要在这这种线程模式中进行耗时操作，否则会卡顿。
     */
    MAIN,

    /**
     * On Android, subscriber will be called in Android's main thread (UI thread). Different from {@link #MAIN},
     * the event will always be queued for delivery. This ensures that the post call is non-blocking.
     * 非阻塞的，但是依旧执行在主线程中。
     */
    MAIN_ORDERED,

    /**
     * On Android, subscriber will be called in a background thread. If posting thread is not the main thread, subscriber methods
     * will be called directly in the posting thread. If the posting thread is the main thread, EventBus uses a single
     * background thread, that will deliver all its events sequentially. Subscribers using this mode should try to
     * return quickly to avoid blocking the background thread. If not on Android, always uses a background thread.
     */
    BACKGROUND,

    /**
     * Subscriber will be called in a separate thread. This is always independent from the posting thread and the
     * main thread. Posting events never wait for subscriber methods using this mode. Subscriber methods should
     * use this mode if their execution might take some time, e.g. for network access. Avoid triggering a large number
     * of long running asynchronous subscriber methods at the same time to limit the number of concurrent threads. EventBus
     * uses a thread pool to efficiently reuse threads from completed asynchronous subscriber notifications.
     */
    ASYNC
}
```
