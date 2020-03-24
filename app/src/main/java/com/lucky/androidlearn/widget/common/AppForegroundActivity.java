package com.lucky.androidlearn.widget.common;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.core.util.AppRunningForegroundUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfz on 2017/11/30.
 */

public class AppForegroundActivity extends AppCompatActivity {

    @BindView(R.id.btn_method1)
    Button btnMethod1;
    @BindView(R.id.btn_method2)
    Button btnMethod2;
    private Timer mTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreground);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_method1)
    public void onMethod1Click(View view) {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                AppRunningForegroundUtils.isAppBroughtBackGround(AppForegroundActivity.this);
            }
        }, 200, 1000 * 2);
    }

    @OnClick(R.id.btn_method2)
    public void onMethod2Click(View view) {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                AppRunningForegroundUtils.isBackground(AppForegroundActivity.this);
            }
        }, 200, 1000 * 2);
    }


    @OnClick(R.id.btn_stop_check)
    public void onStopCheck(View view) {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
