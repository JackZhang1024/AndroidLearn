package com.lucky.androidlearn.aspectj;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

// AOP 面向切面编程
public class AspectJActivity extends AppCompatActivity {

    private static final String TAG = "AspectJActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aspectj);
        ButterKnife.bind(this);
    }

    // 摇一摇
    @BehaviorTrace(value = "摇一摇", type = 0)
    @OnClick(R.id.btn_shake)
    public void onShakeClick(){
        SystemClock.sleep(2000);
        Log.d(TAG, "onShakeClick: 美女：今晚有空吗？好热啊");
    }


    // 发红包
    @BehaviorTrace(value = "发送包", type = 1)
    @OnClick(R.id.btn_redpackage)
    public void onSendRedPackage(){
        SystemClock.sleep(1500);
        Log.d(TAG, "onSendRedPackage: 美女：亲爱的，我爱你哟，么么哒");
    }


    // 发语音
    @BehaviorTrace(value = "发语音", type = 2)
    @OnClick(R.id.btn_audio)
    public void onSendAudio(){
        SystemClock.sleep(1000);
        Log.d(TAG, "onSendAudio: 美女：去哪儿见面，期待马上见到你");
    }


}
