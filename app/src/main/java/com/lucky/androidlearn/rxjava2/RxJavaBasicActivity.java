package com.lucky.androidlearn.rxjava2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lucky.androidlearn.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author zfz
 *         Created by zfz on 2018/3/17.
 */

public class RxJavaBasicActivity extends AppCompatActivity {
    private static final String TAG = "RxJavaBasicActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava_basic);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_rxjava_basic)
    public void onRxJavaClick() {
        Observable<String> observable = getObservable();
        Observer<String> observer = getObserver();
        //observable.subscribe(observer);

//        observable.subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                Log.e(TAG, "accept: "+s);
//            }
//        });

        observable.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.e(TAG, "accept: "+s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, "accept: "+ throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                Log.e(TAG, "run: ");
            }
        });

    }

    public Observable<String> getObservable() {

        // 第一种写法
//        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
//
//            @Override
//            public void subscribe(ObservableEmitter<String> e) throws Exception {
//                e.onNext("告白气球");
//                e.onNext("夜曲");
//                e.onNext("东风破");
//                e.onComplete();
//            }
//        });

        // 第二种写法
        //Observable<String> observable = Observable.just("告白气球", "夜曲", "东风破");

        // 第三种写法
        Observable<String> observable = Observable.fromArray("告白气球", "夜曲", "东风破");
        return observable;
    }

    public Observer<String> getObserver() {
        Observer<String> observer = new Observer<String>() {
            Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                Log.e(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(String music) {
                if ("夜曲".equals(music)) {
                    disposable.dispose();
                }
                Log.e(TAG, "onNext: " + music);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: ");
            }
        };
        return observer;
    }

}
