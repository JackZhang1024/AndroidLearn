- [RxJava2使用和原理分析](#rxjava2使用和原理分析)
  - [RxJava2使用](#rxjava2使用)
    - [RxJava2的简单操作](#rxjava2的简单操作)
    - [RxJava2的操作符](#rxjava2的操作符)
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


#### RxJava2结合OkHttpClient使用 

#### RxJava2配合Retrofit使用

#### RxBus的创建和原理分析

### RxJava2原理

#### RxJava注册订阅原理

#### RxJava取消订阅原理

#### RxJava线程切换原理

#### RxJava变换符操作原理

