package com.lucky.androidlearn.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lucky.androidlearn.R;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author zfz
 */
public class HandlerThreadActivity extends AppCompatActivity {
    public static final int MSG_HELLO = 1;
    private HandlerThread handlerThread = new HandlerThread("Handler线程");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_thread);
        ButterKnife.bind(this);
        handlerThread.start();
    }


    @OnClick(R.id.btn_work_to_handlerthread)
    public void workToHandlerThread() {
        new Handler().postDelayed(()->{
            new Thread(new WorkThread()).start();
        }, 3*1000);
    }

    // 将HandlerThread的looper与Handler绑定
    // 然后mHandlerThreadHandler将为HandlerThread所用
    private Handler mHandlerThreadHandler = null;

    class WorkThread implements Runnable {

        @Override
        public void run() {
            mHandlerThreadHandler = new Handler(handlerThread.getLooper()) {

                private static final String TAG = "HandlerThreadHandler";

                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Log.e(TAG, "handleMessage: " + Thread.currentThread().getName());
                    switch (msg.what) {
                        case MSG_HELLO:
                            Log.e(TAG, "handleMessage: 我是来自WorkThread的空消息...");
                            break;
                    }
                }
            };
            mHandlerThreadHandler.sendEmptyMessage(MSG_HELLO);
        }
    }


}
