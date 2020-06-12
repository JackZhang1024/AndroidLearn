package com.lucky.androidlearn.multithread;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.dagger2learn.learn04.ToastManager;
import com.lucky.androidlearn.exception.toast.ToastUtil;

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
                    " executorService  " + Thread.currentThread().getName()));
        }
        executorService.shutdown();
    }

    @OnClick({R.id.btn_asynctask})
    public void onAsyncTaskClick(View view) {
        //Toast.makeText(this,"AsyncTask", Toast.LENGTH_SHORT).show();
        for (int index=0; index< 5; index++){
            MultiAsyncTask asyncTask = new MultiAsyncTask();
            // 多个线程按照创建的顺序依次执行
            asyncTask.execute(String.format("Task %s", index));
            // 多个线程同时进行
            //asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.format("Task %s", index));
        }
    }






}
