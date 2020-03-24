package com.lucky.androidlearn.handler;

import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ThreadLocal在Handler的原理中起到很关键的作用
 * ThreadLocal将Looper和线程绑定
 * Hanlder的sendMessage方法 相当于把数据塞到Looper的MessageQueue中
 * 然后Looper中有个死循环在不断的执行 最后按照时间顺序来将加入的数据取出来
 * 然后又将数据发送会给Handler处理
 * Created by zfz on 2018/4/1.
 */

public class HandlerLearnActivity extends AppCompatActivity {

    private static final String TAG = "HandlerLearnActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_learn);
        ButterKnife.bind(this);
    }


    // Looper 的loop
    @OnClick(R.id.btn_looper_loop)
    public void onLooperLoopClick(){
        while (true) {
            Log.e(TAG, "onLooperLoopClick: 测试耗时时间");
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Looper mainLooper = Looper.getMainLooper();
                        final Looper me = mainLooper;
                        final MessageQueue queue;
                        Field fieldQueue = me.getClass().getDeclaredField("mQueue");
                        fieldQueue.setAccessible(true);
                        queue = (MessageQueue) fieldQueue.get(me);
                        Method methodNext = queue.getClass().getDeclaredMethod("next");
                        methodNext.setAccessible(true);
                        Binder.clearCallingIdentity();
                        for (; ; ) {
                            Message msg = (Message) methodNext.invoke(queue);
                            if (msg == null) {
                                return;
                            }
                            msg.getTarget().dispatchMessage(msg);
                            msg.recycle();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }


    @OnClick(R.id.btn_work_to_main)
    public void workToMain() {
        new Thread(new ConsumerThread()).start();
    }


    @OnClick(R.id.btn_main_to_work)
    public void mainToWork() {
        new Handler().postDelayed(() -> {
            mWorkThreadHandler.sendEmptyMessage(WorkThreadHandler.MSG_HELLO);
        }, 3 * 1000);
        new Thread(new WorkThread()).start();
    }

    @OnClick(R.id.btn_work_to_work)
    public void workToWork() {
        new Handler().postDelayed(() -> {
            new Thread(new WorkThread()).start();
        }, 3 * 1000);
        new Thread(new ConsumerThread()).start();
    }

    // 主线程中的Handler
    private class MainThreadHandler extends Handler {
        private static final String TAG = "MainThreadHandler";
        public static final int MSG_HELLO = 1;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_HELLO:
                    Log.e(TAG, "handleMessage: 我是从ConsumerThread发送过来的空消息...");
                    break;
            }
        }
    }

    MainThreadHandler mMainThreadHandler = new MainThreadHandler();

    // 工作线程中的Handler
    private class WorkThreadHandler extends Handler {
        private static final String TAG = "WorkThreadHandler";
        private static final int MSG_HELLO = 1;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_HELLO:
                    Log.e(TAG, "handleMessage: 我是从主线程发送过来的空消息...");
                    break;
            }
        }
    }

    WorkThreadHandler mWorkThreadHandler = null;
    private Looper mWorkThreadLooper;

    class WorkThread implements Runnable {


        @Override
        public void run() {
            // 在当前线程中创建Looper 并将Looper与该线程绑定
            // 利用ThreadLocal将Looper与WorkThread线程绑定
            Looper.prepare();
            /**
             * Default constructor associates this handler with the {@link Looper} for the
             * current thread.
             *
             * If this thread does not have a looper, this handler won't be able to receive messages
             * so an exception is thrown.
             */
            // 文档注释说明了 使用默认的构造方法时, 在当前线程中必须有Looper对象
            mWorkThreadHandler = new WorkThreadHandler();
            // 获取Looper
            mWorkThreadLooper = Looper.myLooper();

            // 如果让从WorkThread发送消息到ConsumerThread 要获取mConsumerThreadHandler对象
            // 所以 必须先让ConsumerThread先执行
            if (mConsumerHandler != null) {
                mConsumerHandler.sendEmptyMessage(ConsumerHandler.MSG_HELLO);
            }
            // 启动消息循环 因为启动了消息循环(死循环) 所以该线程一直不会销毁 除非执行 looper.quit()方法
            // 然后执行Looper.loop()后面的语句直至该线程死亡
            Looper.loop();
            // 适时可以停掉消息循环 防止内存泄漏
            //mLooper.quit();
            // 比较安全的停止掉消息循环
            //mLooper.quitSafely();
        }
    }

    // 消费者Handler
    class ConsumerHandler extends Handler {
        private static final String TAG = "ConsumerHandler";
        public static final int MSG_HELLO = 1;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_HELLO:
                    Log.e(TAG, "handleMessage: 我是从WorkThread发送过来的空消息...");
                    break;
            }
        }
    }

    ConsumerHandler mConsumerHandler = null;
    private Looper mConsumerLooper;

    // 消费者线程
    class ConsumerThread implements Runnable {

        @Override
        public void run() {
            Looper.prepare();
            mConsumerHandler = new ConsumerHandler();
            mConsumerLooper  = Looper.myLooper();
            if (mMainThreadHandler != null) {
                mMainThreadHandler.sendEmptyMessage(MainThreadHandler.MSG_HELLO);
            }
            Looper.loop();
            // 适时进行移除操作
            // mLooper.quitSafely();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWorkThreadLooper != null) {
            mWorkThreadLooper.quitSafely();
        }
        if (mConsumerLooper != null) {
            mConsumerLooper.quitSafely();
        }
        if (mMainThreadHandler != null) {
            mMainThreadHandler.removeCallbacksAndMessages(null);
        }
        if (mWorkThreadHandler != null) {
            mWorkThreadHandler.removeCallbacksAndMessages(null);
        }
        if (mConsumerHandler != null) {
            mConsumerHandler.removeCallbacksAndMessages(null);
        }
    }
}
