package com.lucky.androidlearn.rxjava2;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zfz
 * Created by zfz on 2018/3/18.
 */

public class RxJavaCountDownActivity extends AppCompatActivity {
    private static final String TAG = "RxJavaCountDownActivity";

    @BindView(R.id.btn_countdown)
    Button mBtnCountDown;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava_countdown);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btn_countdown)
    public void countDown(){
        int count = 10;
        Observable.interval(0, 1, TimeUnit.SECONDS)
                // 进行10次
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
    }

}
