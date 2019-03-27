package com.lucky.androidlearn.performance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LeakCanaryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leakcanary);
        ButterKnife.bind(this);
        new MyTask().start();
    }

    // 非静态匿名内部类持有外部类的引用 可能会造成内存泄漏
    @OnClick(R.id.btn_click)
    public void createLeak() {
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    Thread.sleep(60 * 1000);
//                    System.out.println("doSomethings....");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();

//      new MyTask().start();
    }


    // 非静态内部类会持有外部类的引用（此处为LeakCanaryActivity的实例）
    // 如果需要解决此内存泄漏问题  需要将MyTask 申明成static的

    class MyTask extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                Thread.sleep(60 * 1000);
                System.out.println("doSomethings....");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


//    static class MyTask extends Thread {
//
//        @Override
//        public void run() {
//            super.run();
//            try {
//                Thread.sleep(60 * 1000);
//                System.out.println("doSomethings....");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
