- [RxJava2使用和原理分析](#rxjava2使用和原理分析)
  - [RxJava2使用](#rxjava2使用)
    - [RxJava2的简单操作](#rxjava2的简单操作)
    - [RxJava2的操作符](#rxjava2的操作符)
      - [1. 创建操作符](#1-创建操作符)
      - [2. 变换操作符](#2-变换操作符)
      - [3. 过滤操作符](#3-过滤操作符)
      - [4. 组合操作符](#4-组合操作符)
      - [5. 辅助操作符](#5-辅助操作符)
      - [6. 错误处理操作符](#6-错误处理操作符)
      - [7. 条件和布尔操作符](#7-条件和布尔操作符)
    - [RxJava2结合OkHttpClient使用](#rxjava2结合okhttpclient使用)
    - [RxJava2配合Retrofit使用](#rxjava2配合retrofit使用)
    - [RxBus的创建和原理分析](#rxbus的创建和原理分析)
  - [RxJava2原理](#rxjava2原理)
    - [RxJava注册订阅原理](#rxjava注册订阅原理)
    - [RxJava取消订阅原理](#rxjava取消订阅原理)
    - [RxJava线程切换原理](#rxjava线程切换原理)
    - [RxJava变换符操作原理](#rxjava变换符操作原理)
## RxJava2使用和原理分析
RxJava是16，17年比较大火的异步线程框架，相比较之前最开始Android开发使用的AsyncTask, Handler这些Android原生自带的一些，有很多好处。AsyncTask是很好用，但是用法比较单一，没有RxJava那种比较多的操作符，可以简化很多代码，Handler这种会有很多回调，最后造成代码比较复杂。这两种方法最后都有一个比较坑的问题，内存泄漏，如果没有好的管理，则在页面退出的时候，很容易出现这个问题，然而在RxJava中，这种问题很好解决，不管在Activity还是在Fragment中，在发起请求的时候，将Disposable添加到CompositeDisposable这个容器中，在页面关闭的时候，在对CompositeDisposable进行dispose，并将对应的视图置空，这样就可以很完美的解决，同时RxJava还有很多的操作符可以使用，可以合并请求等等。

### RxJava2使用
#### RxJava2的简单操作
在app的build.gradle文件中添加RxJava2的依赖
```java
// RxJava
implementation 'io.reactivex.rxjava2:rxjava:2.1.1' 
implementation 'io.reactivex.rxjava2:rxandroid:2.0.1' // rxandroid提供的是一些线程调度器
```
首先确定几个概念：
1. ObservableSource是数据源接口，接口方法是subscribe(Observer<? super T> observer)
2. Observable是实现ObservableSource接口的抽象类
3. Observer是观察者，用于订阅Observable并接收Observable发送过来的数据，并且定义了事件开始，处理，结束，错误的回调函数。
4. Dispoable是一个接口，用于事件处理的丢弃。
常见的使用方法
```java
// Observable的创建
// 第一种写法
Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
    @Override
    public void subscribe(ObservableEmitter<String> e) throws Exception {
        e.onNext("告白气球");
        e.onNext("夜曲");
        e.onNext("东风破");
        e.onComplete();
    }
});
// 第二种写法
Observable<String> observable = Observable.just("告白气球", "夜曲", "东风破");

// 第三种写法
Observable<String> observable = Observable.fromArray("告白气球", "夜曲", "东风破");


//Observer的创建
Observer<String> observer = new Observer<String>() {
    Disposable disposable;

    // 当observer订阅Observable的时候进行回调
    @Override
    public void onSubscribe(Disposable d) {
        // disposable可以对事件进行取消
        disposable = d;
        Log.e(TAG, "onSubscribe: ");
    }

    // 当数据通过onNext()发送数据给Observer 
    @Override
    public void onNext(String music) {
          
        if ("夜曲".equals(music)) {
            disposable.dispose();
        }
        Log.e(TAG, "onNext: " + music);
    }

    // 当发生错误的时候进行回调
    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "onError: " + e.getMessage());
    }

    // 当完成的时候进行回调 
    @Override
    public void onComplete() {
        Log.e(TAG, "onComplete: ");
    }
};

// 进行订阅
// 方法一：
observable.subscribe(observer);

// 方法二：
observable.subscribe(new Consumer<String>(){
    @Override
    public void accept(String s) throws Exception {
        
    }
});

// 方法三：
observable.subscribe(new Consumer<String>(){ // onNext

}, new Consumer<Throwable>(){ // onError

}, new Action(){ // onComplete

};

....
```
#### RxJava2的操作符
在上面的代码中，我们可以看到通过create(), just(), from的静态方法创建Observable, 这些其实都是RxJava的操作符，除了这些，还有一些其他的操作符供我们使用，这些操作符也具有不同的意义和使用场景。

RxJava中操作符分为1.创建操作符 2.变换操作符 3.过滤操作符 4.组合操作符 5.错误处理操作符 6.辅助操作符 7.条件和布尔操作符 8. 算数和聚合操作符 9.连接操作符等

##### 1. 创建操作符
1. interval 
```java
// 每隔1秒发射一次 延迟0秒
int count = 10;
Observable.interval(0, 1, TimeUnit.SECONDS)
        // 进行10次 发射
        .take(count+1)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long aLong) {
            Log.e(TAG, "accept: "+(count - aLong));
                mBtnCountDown.setText(String.format("倒计时 %s", (count - aLong)));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: ");
                mBtnCountDown.setText("倒计时");
            }
        });
```    
2. range
```java
// 从0 开始 到5结束 左闭右开 0,1,2,3,4
Observable.range(0, 5).subscribe(new Consumer<Integer>() {
    @Override
    public void accept(Integer integer) throws Exception {
        // ...
    }
});
```   
3. repeat
```java
// 从0 开始 到3结束 左闭右开
// repeat创建一个重复多次发射的Observable
Observable.range(0,3).repeat(2).subscribe(new Consumer<Integer>() {
    @Override
    public void accept(Integer integer) throws Exception {
         //....
    }
});
输出结果：0,1,2,0,1,2
```    
##### 2. 变换操作符
变化操作符就是将原有发射的数据进行变换，然后将变换后的数据重新发射出去, 并产生一个新的Observable。变换操作符有map, flatMap, concatMap, switchMap, flatMapIterable, buffer, groupBy, cast, window, scan等。
1. map
```java
// map操作符就是将原有的发射的数据通过一个函数Function进行变换，然后将转换后的数据返回并发射出去，并创建新的Observale。
Observable.just(1, 2, 3)
        .map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return String.format("我是第%s", integer);
            }
        })
        .subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.e(TAG, "accept: " + s);
            }
        });
```   
输出结果：
```java
输出结果：
我是第1
我是第2
我是第3
```
2. flatMap, cast
```java
/**
flatMap和map的操作符都是将一个要发射的对象转换成另外的一个对象，但是过程不一样，结果也不一样。参考别人的说法，总结如下：
相同点：
1. 都是利用Function来进行转换（将一个类型根据逻辑转换成另外一个类型，依据入参和返回值）
2. 都能在转换后直接被subscribe
不同点
1. map返回的是结果集（这个结果集可以是一次，也可以是多次被返回，看初始发送的元素的个数，如果是1个，那么结果集一次，Obserser就只接收一次，如果是多个，就接收多次。）flatmap返回的是包含结果集的Observable(个人认为这不操作只是一个中间状态，最后订阅的时候只有一个Observable, 发送一次就可以将集合中的数据发射出去)。
2. map被订阅的时候，每传递一个事件就执行一次onNext方法，flatmap多用于多对多（多人对多个课程），一对多（一个人对一个课程，这块将的是数据情况，不是数据模型，就是说数据结构里存在多个人，一个人里含有不同数目的课程），再被转为多个时，一般利用from/just进行--分发（Observable.from(student.getCourses())，被订阅时将所有数据传递完毕汇总到一个Observable然后一一执行onNext方法
3. map只能单一转换，单一只的是只能一对一进行转换，指一个对象可以转化为另一个对象但是不能转换成对象数组（map返回结果集不能直接使用from/just再次进行事件分发，一旦转换成对象数组的话，再处理集合/数组的结果时需要利用for一一遍历取出，而使用RxJava就是为了剔除这样的嵌套结构，使得整体的逻辑性更强。）
flatmap既可以单一转换也可以一对多/多对多转换，flatmap要求返回Observable，因此可以再内部进行from/just的再次事件分发，一一取出单一对象（转换对象的能力不同）
4. flatmap可以多次发射，map不能多次发射。多次发射意味着就可以进行多次请求，那么就可以进行串行接口的请求。

**/

```    
3. contactMap
```java
contactMap和FlatMap的使用方式相同，但是flatMap发射数据会发生错乱，contactMap不会。
```   
4. buffer
```java
// 将原有Observable转换成新的Observable, 将数据一组一组进行发射，而不是进行一个一个的发射。
Observable.just(1,2,3,4,5,6)
        .buffer(3).subscribe(new Observer<List<Integer>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Integer> integers) {
                for (Integer i: integers){
                    Log.e(TAG, "onNext: "+i);
                }
                Log.e(TAG, "onNext: -----------");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });     
``` 
输出结果：
```java
// 输出结果
2020-07-13 10:04:21.139 20859-20859/com.lucky.androidlearn E/RxJavaOperators: onNext: 1
2020-07-13 10:04:21.139 20859-20859/com.lucky.androidlearn E/RxJavaOperators: onNext: 2
2020-07-13 10:04:21.139 20859-20859/com.lucky.androidlearn E/RxJavaOperators: onNext: 3
2020-07-13 10:04:21.139 20859-20859/com.lucky.androidlearn E/RxJavaOperators: onNext: -----------
2020-07-13 10:04:21.140 20859-20859/com.lucky.androidlearn E/RxJavaOperators: onNext: 4
2020-07-13 10:04:21.140 20859-20859/com.lucky.androidlearn E/RxJavaOperators: onNext: 5
2020-07-13 10:04:21.140 20859-20859/com.lucky.androidlearn E/RxJavaOperators: onNext: 6
2020-07-13 10:04:21.140 20859-20859/com.lucky.androidlearn E/RxJavaOperators: onNext: -----------   
```
##### 3. 过滤操作符
过滤操作符用于过滤和选择Observable发射的数据序列，让Observable只返回满足条件的数据。过滤操作符有
filter, elementAt, distinct, skip, take, ignoreElements, throttleFirst, throttleWithTimeOut。
1. filter 
```java
// filter过滤符就是对Observable产生的结果进行规则过滤，只有满足条件的数据才会被发送给订阅者。
Observable.just(1,2,3,4,5).filter(new Predicate<Integer>() {
          @Override
          public boolean test(Integer integer) throws Exception {
              return integer>3; // 过滤出大于3的数据发送给订阅者
          }
      }).subscribe(new Observer<Integer>() {
          @Override
          public void onSubscribe(Disposable d) {

          }
          @Override
          public void onNext(Integer integer) {
              Log.e(TAG, "onNext: "+integer);
          }
          @Override
            public void onError(Throwable e) {
          }

          @Override
         public void onComplete() {
          }
     });
```   
2. elementAt
```java
// 取出指定位置的数据
Observable.just(1,2,3,4).elementAt(2).subscribe(new MaybeObserver<Integer>() {
        @Override
        public void onSubscribe(Disposable d) {
        }
        @Override
        public void onSuccess(Integer integer) {
            Log.e(TAG, "onSuccess: "+integer); // onSuccess: 3
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {
        }
});
```   
3. distinct
```java
// 只允许没有发射过的数据进行发射
Observable.just(1, 2, 3, 1, 3, 5).distinct().subscribe(new Observer<Integer>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(Integer integer) {
            Log.e(TAG, "onNext: "+integer); // 1, 2, 3, 5
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
});
```
4. skip, take
```java
//skip操作符将源Observable发射的数据过滤掉前n项，take操作符则只取前n项
Observable.just(1, 2, 3, 5).skip(2).subscribe(new Observer<Integer>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(Integer integer) {
            Log.e(TAG, "skip onNext: "+integer); // skip onNext: 3 / skip onNext: 5
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
         public void onComplete() {

        }
});
Observable.just(1, 2, 3, 5).take(2).subscribe(new Observer<Integer>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(Integer integer) {
            Log.e(TAG, "take onNext: "+integer); // take onNext: 1 / take onNext: 2
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onComplete() {
        }
});

```
5. ignoreElements 
ignoreElements操作符忽略所有源的Observable产生的结果，只把Observable的onCompleted和onError事件通知给订阅者。
6. throttleFirst
```java
throttleFirst操作符会定期发射这个时间段里源Observable发射的第一个数据。throttleFirst操作符默认在computation调取器上执行。
```
##### 4. 组合操作符
组合操作符可以同时处理多个Observable来创建我们需要的Observable.组合操作符有startWith,merge,contact, zip等。
1. startWith操作符会在源Observable发射的数据前面插上一些数据。
2. merge操作符将多个Observable合并到一个Observable中进行发射，merge可能会让合并的Observable发生的数据交错。
3. contact将多个Observable发射的数据进行合并发射。contact严格按照顺序发射数据，前一个Observable没有发射完成是不会发射后一个Observable。
4. zip操作符合并两个或者多个Obsevable发射的数据项，根据指定的函数变换它们，并发射一个新值。
##### 5. 辅助操作符
辅助操作符可以帮助我们更方便地处理Observable。delay, Do, subscribeOn, observeOn和timeOut。
1. delay
delay操作符让原始Observable在发射每项数据之前都暂停一段指定的时间段。   
2. Do
Do系列操作符就是为原始Observable的生命周期事件注册一个回调，当Observable的某个事件发生时就会调用这些回调。
doOnEach: 为Observable注册这样一个回调，当Observable每发射一项数据时就会调用一次。
doOnNext: 只执行onNext的时候会被调用。
doOnSubscribe: 当观察者订阅Observable时就会被调用。
doOnError: 当Observable异常终止调用onError时会被调用。
doOnTerminate:  当Observable终止之前会被调用。  
3. subscribeOn，observeOn
subscribeOn操作符用于指定Observable自身在那个线程上运行。如果Observable需要执行耗时操纵，一般可以让其在新开的一个子线程上运行。observerOn用来指定Observer所运行的线程，也就是发射出来的数据在那个线程上使用。一般会指定在主线程上运行。   
4. timeOut
如果原始Observable过了指定的一段时长没有发射任何数据，timeout操作符会以一个onError通知终止这个Observable，或者继续执行
一个备用的Observable。timeout有很多变体，这里介绍其中的一种，timeout(long, TimeUnit, Observable), 它在超时时会切换到
使用一个你指定的Observable, 而不是发送错误通知。
```java
Observable.create(new ObservableOnSubscribe<Integer>() {
        @Override
        public void subscribe(ObservableEmitter<Integer> e) throws Exception {
            for (int i = 0; i < 4; i++) {
                try {
                    Thread.sleep(i * 100);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                e.onNext(i);
            }
            e.onComplete();
        }
    }).timeout(200, TimeUnit.MILLISECONDS, Observable.just(10, 11)).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.e(TAG, "onNext: "+integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
```   
##### 6. 错误处理操作符
RxJava在错误出现的时候就会调用Subscribe的onError方法将错误分发出去，由Subscriber自己来处理错误。但是如果每个Subscriber都处理一遍的话，工作量比较大，这时候就可以使用错误处理操作符。错误处理操作符有catch和retry。
日常开发中，没有涉及到，后面再研究分析
##### 7. 条件和布尔操作符
日常开发中，没有涉及到，后面再研究分析

#### RxJava2结合OkHttpClient使用 
```java
private Observable getObservable() {
    return Observable.create((ObservableOnSubscribe<String>) e -> {
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = "http://ip-api.com/json/?lang=zh-CN";
        Request request = new Request.Builder().get().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException exception) {
                e.onError(exception);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                e.onNext(result);
                e.onComplete();
            }
        });
    });
}

public void onIPFetch() {
    getObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    .subscribe(new Observer<String>() {
        @Override
        public void onSubscribe(Disposable d) {
        }
        @Override
        public void onNext(String o) {
            Log.e(TAG, "onNext: " + o);
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: " + e.getMessage());
        }

        @Override
        public void onComplete() {
        }
    });
}
```
输出结果：
```java
onNext: {"status":"success","country":"中国","countryCode":"CN","region":"BJ","regionName":"北京市","city":"Jinrongjie","zip":"","lat":39.9174,"lon":116.361,"timezone":"Asia/Shanghai","isp":"China Unicom Beijing Province Network","org":"","as":"AS4808 China Unicom Beijing Province Network","query":"61.135.125.14"}
```
#### RxJava2配合Retrofit使用
1. build.gradle中进行配置
```java
// 将call转换成Observable
implementation 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
// 将字符串转化成对象
implementation 'com.squareup.retrofit2:converter-gson:2.3.0'
// retrofit2
implementation 'com.squareup.retrofit2:retrofit:2.0.0'
// 网络请求日志拦截器
implementation 'com.squareup.okhttp3:logging-interceptor:3.8.1'
```
2. 创建Retrofit对象 
```java
public class RetrofitFactory {
    private static final String TAG = "RetrofitFactory";
    // 访问超时
    private static final int TIME_OUT = 10*1000;

    // Retrofit是基于OkHttpClient的, 可以创建一个OkHttpClient进行一些配置
    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            // 打印接口信息 方便调试接口
            .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.e(TAG, "log: "+message);
                }
            }).setLevel(HttpLoggingInterceptor.Level.BASIC))
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .build();


    private static RetrofitService retrofitService = new Retrofit.Builder()
            .baseUrl(RetrofitService.BASE_URL)
            // 添加Gson转换器
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                    .setLenient()
                    .create()))
            // 添加Retrofi到RxJava的转换器
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient)
            .build()
            .create(RetrofitService.class);

    public static RetrofitService getInstance(){
        return retrofitService;
    }
}
```
3. 创建访问接口文件
RetrofitService.java   
```java
public interface RetrofitService {

    String BASE_URL = "http://tj.nineton.cn/";

    // @GET("xxx") // xxx可以是相对路径也可以是绝对路径
    @GET("http://ip-api.com/json/?lang=zh-CN")
    Observable<IPInfo> getIPInfo();
}
```
4. 访问接口
```java
public void getIPInfo(){
    RetrofitFactory.getInstance().getIPInfo().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<IPInfo>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(IPInfo ipInfo) {
                    Log.e(TAG, "onNext: "+ipInfo.getCity());
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
}
```   
   
#### RxBus的创建和原理分析
```java
// 添加rxlifecycle来控制生命周期
implementation 'com.trello.rxlifecycle2:rxlifecycle-android-lifecycle:2.2.2'
```
```java
public class RxBus {
    private volatile static RxBus mDefaultInstance;
    private final Subject<Object> mBus;

    private final Map<Class<?>, Object> mStickyEventMap;

    private RxBus() {
        mBus = PublishSubject.create().toSerialized();
        mStickyEventMap = new ConcurrentHashMap<>();
    }

    public static RxBus getInstance() {
        if (mDefaultInstance == null) {
            synchronized (RxBus.class) {
                if (mDefaultInstance == null) {
                    mDefaultInstance = new RxBus();
                }
            }
        }
        return mDefaultInstance;
    }

    /**
     * 发送事件
     */
    public void post(Object event) {
        mBus.onNext(event);
    }

    /**
     * 使用Rxlifecycle解决RxJava引起的内存泄漏
     */
    public <T> Observable<T> toObservable(LifecycleOwner owner, final Class<T> eventType) {
        LifecycleProvider<Lifecycle.Event> provider = AndroidLifecycle.createLifecycleProvider(owner);
        return mBus.ofType(eventType).compose(provider.<T>bindToLifecycle());
    }

    /**
     * 判断是否有订阅者
     */
    public boolean hasObservers() {
        return mBus.hasObservers();
    }

    public void reset() {
        mDefaultInstance = null;
    }


    /**
     * Stciky 相关
     */

    /**
     * 发送一个新Sticky事件
     */
    public void postSticky(Object event) {
        synchronized (mStickyEventMap) {
            mStickyEventMap.put(event.getClass(), event);
        }
        post(event);
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     * 使用Rxlifecycle解决RxJava引起的内存泄漏
     */
    public <T> Observable<T> toObservableSticky(LifecycleOwner owner, final Class<T> eventType) {
        synchronized (mStickyEventMap) {
            LifecycleProvider<Lifecycle.Event> provider = AndroidLifecycle.createLifecycleProvider(owner);
            Observable<T> observable = mBus.ofType(eventType).compose(provider.<T>bindToLifecycle());
            final Object event = mStickyEventMap.get(eventType);

            if (event != null) {
                return observable.mergeWith(Observable.create(new ObservableOnSubscribe<T>() {
                    @Override
                    public void subscribe(ObservableEmitter<T> subscriber) throws Exception {
                        subscriber.onNext(eventType.cast(event));
                    }
                }));
            } else {
                return observable;
            }
        }
    }

    /**
     * 根据eventType获取Sticky事件
     */
    public <T> T getStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.get(eventType));
        }
    }

    /**
     * 移除指定eventType的Sticky事件
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.remove(eventType));
        }
    }

    /**
     * 移除所有的Sticky事件
     */
    public void removeAllStickyEvents() {
        synchronized (mStickyEventMap) {
            mStickyEventMap.clear();
        }
    }
}
```
```java
//1. 发送事件
RxBus.getInstance().post(new MessagEvent());
//2. 注册事件
RxBus.getInstance().toObservableSticky(this, MessageEvent.class)
     .subscribe(new Consumer<MessageEvent>(){
      public void accept(MessageEvent event) throws Exception{
 
      }
});
```
### RxJava2原理
RxJava2的原理其实也很简单，就是一个观察者模式，但是要搞清楚其中的实现机制，必须清楚有哪些类，以及类之间的关系。
```java
类
Observable
ObservaleSource
ObservableOnSubscribe
Observer
ObservableEmitter
Disposable

方法
Observable.create
Observable.subscribe
onSubscribe
onNext
onComplete
onError
```
#### RxJava注册订阅原理
首先从Observable的接口开始，
```java
public abstract class Observable<T> implements ObservableSource<T> {
    ...
}
```
```java
// ObservableSource的代码
public interface ObservableSource<T> {

    /**
     * Subscribes the given Observer to this ObservableSource instance.
     * @param observer the Observer, not null
     * @throws NullPointerException if {@code observer} is null
     */
     // 用于添加Observer 类似与观察者模式中的subject.add(new Observer())操作，方便后面进行拿到Obserder的引用，
     // 然后通知观察者
    void subscribe(@NonNull Observer<? super T> observer);
}
```
然后分析Observable的creat()方法，最后返回的ObservableCreate对象
```java
@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
public static <T> Observable<T> create(ObservableOnSubscribe<T> source) {
    ObjectHelper.requireNonNull(source, "source is null"); // 没啥用
    return RxJavaPlugins.onAssembly(new ObservableCreate<T>(source)); // 
}

// RxPlugins.java中的onAssembly方法
@NonNull
public static <T> Observable<T> onAssembly(@NonNull Observable<T> source) {
    Function<? super Observable, ? extends Observable> f = onObservableAssembly;
    if (f != null) {
        return apply(f, source);
    }
    // 结果最终是返回的source 
    return source; 
}
```
分析ObservableCreate对象, 里面有个成员变量是ObservableOnSubscribe类型
```java
public final class ObservableCreate<T> extends Observable<T> {
    final ObservableOnSubscribe<T> source;

    public ObservableCreate(ObservableOnSubscribe<T> source) {
        this.source = source;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        CreateEmitter<T> parent = new CreateEmitter<T>(observer);
        observer.onSubscribe(parent);
        try {
            source.subscribe(parent);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            parent.onError(ex);
        }
    }
    .....
}
```
返回到最开始的create()方法，我们传递进去的参数是ObservableOnSubscribe类型的，是个接口，我们分析下
```java
public interface ObservableOnSubscribe<T> {

    /**
     * Called for each Observer that subscribes.
     * @param e the safe emitter instance, never null
     * @throws Exception on error
     */
    void subscribe(@NonNull ObservableEmitter<T> e) throws Exception;
}
```
有个回调对象是ObservableEmitter类型的，这个暂时不用管了，但是我们要看下这个subscribe(Emmiter e)方法是在那块执行的？
往上看，我们看到了是在subscribeActual方法中被调用的，但是subscribeActual方法是被谁调用的，在哪块调用，我们暂时放下，看下
Observable对象的subscribe方法
```java
@SchedulerSupport(SchedulerSupport.NONE)
@Override
public final void subscribe(Observer<? super T> observer) {
    ObjectHelper.requireNonNull(observer, "observer is null");
    try {
        observer = RxJavaPlugins.onSubscribe(this, observer); // 返回的还是observer对象
        ObjectHelper.requireNonNull(observer, "Plugin returned null Observer");
        subscribeActual(observer);// 重点来了 
    } catch (NullPointerException e) { // NOPMD
        throw e;
    } catch (Throwable e) {
        Exceptions.throwIfFatal(e);
            // can't call onSubscribe because the call might have set a Subscription already
        RxJavaPlugins.onError(e);

        NullPointerException npe = new NullPointerException("Actually not, but can't throw other exceptionsdue to RS");
        npe.initCause(e);
        throw npe;
    }
}
```
我们发现Observable对象也有个subscribeActual(observer)方法，那么内容是啥？
```java
/**
* Operator implementations (both source and intermediate) should implement this method that
* performs the necessary business logic.
* <p>There is no need to call any of the plugin hooks on the current Observable instance or
* the Subscriber.
* @param observer the incoming Observer, never null
*/
protected abstract void subscribeActual(Observer<? super T> observer);
```
很遗憾，我们看到的是一个抽象方法，没有具体的实现，那么具体的实现在哪里？看着有点眼熟，对了我们在前面已经碰到过了，ObservableCreate
这个类就有这个方法，其实在我们用create()方法的时候，其实返回的就已经是ObservableCreate对象了，即现在调用subscribeActual方法的
也就是ObservableCreate对象。那么就又回去了，我们观察这个subcribeActual方法，看里面做了什么？
```java
@Override
protected void subscribeActual(Observer<? super T> observer) {
    CreateEmitter<T> parent = new CreateEmitter<T>(observer); //Emitter对观察者进行了一层包装
    observer.onSubscribe(parent); // 1. 回调Observer的onSubscribe方法
    try {
        source.subscribe(parent); // 2. 进行订阅 source是被观察者，parent是观察者， subscribe进行订阅
    } catch (Throwable ex) {
        Exceptions.throwIfFatal(ex);
        parent.onError(ex);
    }
}
```
source是ObservableOnSubscribe类型的对象，也就是我们在Observable.create()方法里面设置的参数，好家伙，又回到subscribe()方法里面，这下基本就清楚了，最终的要点就是在ObservableCreate这个类中。

`source.subscribe(parent);` 这个parent是Emmiter类型的，是回调给subscribe方法里参数，一般情况下，我们会调用这个Emmiter对象的onNext(), onComplete(), onError() 方法，最终会回调给Observer的onNext(), onComplete(), onError()方法。这部分的代码都在Emmiter中：
```java
static final class CreateEmitter<T>
extends AtomicReference<Disposable>
implements ObservableEmitter<T>, Disposable {

    private static final long serialVersionUID = -3434801548987643227L;

    final Observer<? super T> observer;

    CreateEmitter(Observer<? super T> observer) {
        this.observer = observer;
    }

    @Override
    public void onNext(T t) {
        if (t == null) {
            onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
                return;
            }
            // 同时调用dispose方法，最终停止掉onNext()方法的回调
            if (!isDisposed()) {
                observer.onNext(t);
            }
        }

        @Override
        public void onError(Throwable t) {
            if (!tryOnError(t)) {
                RxJavaPlugins.onError(t);
            }
        }

    @Override
    public boolean tryOnError(Throwable t) {
        if (t == null) {
            t = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        }
        if (!isDisposed()) {
            try {
                observer.onError(t);
            } finally {
                dispose();
            }
              return true;
            }
            return false;
        }

        @Override
        public void onComplete() {
            if (!isDisposed()) {
                try {
                    observer.onComplete();
                } finally {
                    dispose();
                }
            }
        }

        @Override
        public void setDisposable(Disposable d) {
            DisposableHelper.set(this, d);
        }

        @Override
        public void setCancellable(Cancellable c) {
            setDisposable(new CancellableDisposable(c));
        }

        @Override
        public ObservableEmitter<T> serialize() {
            return new SerializedEmitter<T>(this);
        }

        @Override
        public void dispose() {
            DisposableHelper.dispose(this);
        }

        @Override
        public boolean isDisposed() {
            return DisposableHelper.isDisposed(get());
        }
    }
```
#### RxJava取消订阅原理
通过上面的分析，我们可以看到通过调用Disposable的dispose()方法，就可以停止事件的继续发送。

#### RxJava线程切换原理
```java
Observable.create(new ObservableSubscribe())
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.MainThread())
    .subscribe(new Consumer(){
        void accept(){
            ...
        }
    });
```
我们首先看subscribeOn(Schedulers.io())
```java
@CheckReturnValue
@SchedulerSupport(SchedulerSupport.CUSTOM)
public final Observable<T> subscribeOn(Scheduler scheduler) {
    ObjectHelper.requireNonNull(scheduler, "scheduler is null");
    // 返回的是ObservableSubscribeOn对象
    return RxJavaPlugins.onAssembly(new ObservableSubscribeOn<T>(this, scheduler));
}
```

#### RxJava变换符操作原理


