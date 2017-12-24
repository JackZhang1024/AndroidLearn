package com.lucky.androidlearn.core.service;

import android.app.Service;
import android.content.Intent;
import android.media.audiofx.LoudnessEnhancer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lucky.androidlearn.core.util.AppRunningForegroundUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.security.auth.login.LoginException;

/**
 * Created by zfz on 2017/11/14.
 */

public class PollService extends Service {

    private static final String TAG = "PollService";
    public static final String ACTION = "POLLING_ACTION";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: startId " + startId+" 当前线程名称 "+Thread.currentThread().getName());
        startLoadNetWorkData();
        return START_NOT_STICKY;
    }

    // 当重新onStartCommand时 所有的动作必须取消 重新开始
    private void startLoadNetWorkData(){
        if (mCountDownTimer!=null){
            mCountDownTimer.cancel();
            mCountDownTimer.getHandler().removeCallbacksAndMessages(null);
        }
        new Thread(new LoadDataTask(listener)).start();
    }

    // 加载网络数据
    public OnLoadDataFinishListener listener = new OnLoadDataFinishListener() {

        @Override
        public void OnLoadDataFinish() {
            Looper.prepare();
            // 每次调用接口之后都要重新进行倒计时操作
            if (mCountDownTimer != null) {
                mCountDownTimer.cancel();
            }
            mCountDownTimer = new MyCountDownTimer(1000 * 30, 1000);
            mCountDownTimer.start();
            Looper.loop();
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private MyCountDownTimer mCountDownTimer;

    public class LoadDataTask implements Runnable {
        OnLoadDataFinishListener listener;

        public LoadDataTask(OnLoadDataFinishListener listener) {
            this.listener = listener;
        }

        @Override
        public void run() {
            try {
                // 模拟网络请求
                Thread.sleep(3000);
                // 开始倒计时操作 30秒倒计时 间隔一秒
                if (listener!= null){
                    listener.OnLoadDataFinish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // 内部倒计时
    class MyCountDownTimer extends CountDownTimer {
        private Handler handler;
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            Log.e(TAG, "当前线程 "+Thread.currentThread().getName() +"  onTick: 剩余 " + l / 1000 + " 秒");
            //AppRunningForegroundUtils.isBackground(PollService.this);
            AppRunningForegroundUtils.isAppBroughtBackGround(PollService.this);
        }

        @Override
        public void onFinish() {
            Log.e(TAG, "onFinish: 倒计时结束");
            handler = new Handler(Looper.getMainLooper()){

                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Log.e(TAG, "handleMessage: 当前线程"+Thread.currentThread().getName());
                    // 倒计时结束之后 重新拉取网络数据 继续倒计时
                    // new Thread(new LoadDataTask(listener)).start();
                    // 1. 如果用户处理 进入游戏 停止服务 退出游戏 重新启动服务
                    // 2. 如果不处理 重启服务 然后继续拉取接口 进行倒计时

                }
            };
            handler.sendEmptyMessage(0);
        }

        public Handler getHandler() {
            return handler;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy mCountDownTimer " + (mCountDownTimer == null));
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    interface OnLoadDataFinishListener {
        void OnLoadDataFinish();
    }

}
