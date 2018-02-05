package com.lucky.androidlearn.rxjava2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.jakewharton.rxbinding2.view.RxView;
import com.lucky.androidlearn.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author zfz
 *         Created by zfz on 2018/3/17.
 */

public class RxJavaThrottleFirstActivity extends AppCompatActivity {

    private static final String TAG = "RxJavaThrottleFirst";
    @BindView(R.id.btn_avoid_double_click)
    Button mBtnAvoidDoubleClick;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava_throttle_first);
        ButterKnife.bind(this);
        aVoidDoubleClick();
    }

    /**
     * 防止在两秒之内重复点击
     */
    private void aVoidDoubleClick() {
        RxView.clicks(mBtnAvoidDoubleClick)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Observer<Object>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        Log.e(TAG, "onNext: 触发点击事件  ");
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }


}
