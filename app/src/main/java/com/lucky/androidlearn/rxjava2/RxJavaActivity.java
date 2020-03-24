package com.lucky.androidlearn.rxjava2;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.rxjava.ObserverActivity;
import com.lucky.androidlearn.rxjava2.search.RealTimeSearchActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * RxJava 响应式编程是利用可观测序列构建成的一种异步，基于事件的程序。
 * <p>
 * RxJava的好处：
 * 1. 对于服务器更加友好
 * 2. 有条件的异步执行
 * 3. 一种更好的方式来避免回调地狱
 * 4. 一种响应式方法
 * <p>
 * RXJava与一般的生产者消费者模式的区别
 * 1. 在生产者不在生产的时候会有onCompleted() 事件
 * 2. 在发生错误的时候会有onError() 事件
 * 3. RxJava Observables是可以组合使用 而不是嵌套使用 避免了回调地狱
 * <p>
 *
 * RxJava的学习资料
 * https://www.jianshu.com/p/464fa025229e
 *
 * Created by zfz on 2018/2/7.
 */

public class RxJavaActivity extends AppCompatActivity {

    private static final String TAG = "RxJavaActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_obsever_pattern)
    public void observerPattern(){
        startActivity(new Intent(this, ObserverActivity.class));
    }

    @OnClick(R.id.btn_lesson_01)
    public void lesson01(){
        startActivity(new Intent(this, RxJavaBasicActivity.class));
    }

    @OnClick(R.id.btn_lesson_02)
    public void lesson02(){
        startActivity(new Intent(this, RxJavaThreadControlActivity.class));
    }

    @OnClick(R.id.btn_lesson_03)
    public void lesson03(){
        startActivity(new Intent(this, RxJavaOperatorActivity.class));
    }

    @OnClick(R.id.btn_lesson_04)
    public void lesson04(){
        startActivity(new Intent(this, RxJavaKeyWordsSearchActivity.class));
    }

    @OnClick(R.id.btn_lesson_05)
    public void lesson05(){
        startActivity(new Intent(this, RxJavaThrottleFirstActivity.class));
    }

    @OnClick(R.id.btn_lesson_06)
    public void lesson06(){
        startActivity(new Intent(this, RxJavaMergeActivity.class));
    }

    @OnClick(R.id.btn_lesson_07)
    public void lesson07(){
        startActivity(new Intent(this, RxJavaCountDownActivity.class));
    }


    @OnClick(R.id.btn_lesson_08)
    public void lesson08(){
        startActivity(new Intent(this, RxJavaImageDownloadActivity.class));
    }

    @OnClick(R.id.btn_lesson_09)
    public void lesson09(){
        startActivity(new Intent(this, RealTimeSearchActivity.class));
    }
}
