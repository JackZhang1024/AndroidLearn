package com.lucky.androidlearn.multithread;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lucky.androidlearn.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import butterknife.ButterKnife;
import butterknife.OnClick;

// https://blog.csdn.net/xlgen157387/article/details/78253096
public class MultiThreadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multithread_safe);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_check_map_safe)
    public void onMapSafeCheck(View view) {
        //MapSafeCheckTestCase checkTestCase = new MapSafeCheckTestCase();
        //checkTestCase.doTest();

//         MapSafeCheckTestCase2 checkTestCase2 = new MapSafeCheckTestCase2();
//         checkTestCase2.doCheckTest();
        ThreadSafeCheckTestCase threadSafeCheckTestCase = new ThreadSafeCheckTestCase();
        threadSafeCheckTestCase.doCheck();
    }

    @OnClick(R.id.btn_condition)
    public void onCondition(View view) {
        ConditionTestCase conditionTestCase = new ConditionTestCase();
        conditionTestCase.doTest();
    }

    @OnClick(R.id.btn_thread_pool)
    public void onThreadPoolClick(View view) {
        ExecutorService executorService = new ThreadPoolExecutor(3, 4, 0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(10),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        for (int i = 0; i < 10; i++) {
            int index = i;
            executorService.submit(() -> System.out.println("i:" + index +
                    " executorService  "+Thread.currentThread().getName()));
        }
        executorService.shutdown();
    }

}
