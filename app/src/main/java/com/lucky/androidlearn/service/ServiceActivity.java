package com.lucky.androidlearn.service;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.core.service.PollService;
import com.lucky.androidlearn.core.util.PollingManageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfz on 2017/11/14.
 */

public class ServiceActivity extends AppCompatActivity {

    @BindView(R.id.btn_start_polling)
    Button btnStartPolling;
    @BindView(R.id.btn_stop_polling)
    Button btnStopPolling;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        ButterKnife.bind(this);
    }

    // 开始轮询
    @OnClick(R.id.btn_start_polling)
    public void startPolling(View view){
        PollingManageUtils.startPollingService(this, 60, PollService.class, PollService.ACTION);
    }

    // 结束轮询
    @OnClick(R.id.btn_stop_polling)
    public void stopPolling(View view){
        PollingManageUtils.stopPollingService(this, PollService.class, PollService.ACTION);
    }

    @OnClick(R.id.btn_immediate_start)
    public void startPollingImmediate(View view){
        PollingManageUtils.startServiceImmediately(this, PollService.class, PollService.ACTION);
    }

    @OnClick(R.id.btn_immediate_stop)
    public void stopPollingImmediate(View view){
        PollingManageUtils.stopServiceImmediately(this, PollService.class, PollService.ACTION);
    }


}
