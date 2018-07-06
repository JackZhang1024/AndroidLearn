package com.lucky.androidlearn.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    private static final String TAG = "ServiceActivity";
    @BindView(R.id.btn_start_polling)
    Button btnStartPolling;
    @BindView(R.id.btn_stop_polling)
    Button btnStopPolling;
    public static final String SERVICE_PARAM = "SERVICE_PARAM";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        ButterKnife.bind(this);
    }

    // 开始轮询
    @OnClick(R.id.btn_start_polling)
    public void startPolling(View view) {
        PollingManageUtils.startPollingService(this, 60, PollService.class, PollService.ACTION);
    }

    // 结束轮询
    @OnClick(R.id.btn_stop_polling)
    public void stopPolling(View view) {
        PollingManageUtils.stopPollingService(this, PollService.class, PollService.ACTION);
    }

    @OnClick(R.id.btn_immediate_start)
    public void startPollingImmediate(View view) {
        PollingManageUtils.startServiceImmediately(this, PollService.class, PollService.ACTION);
    }

    @OnClick(R.id.btn_start_service)
    public void startServiceClick(View view) {
        Intent intent = new Intent(this, LearnStartService.class);
        intent.putExtra(SERVICE_PARAM, "helloWorld");
        startService(intent);
    }

    @OnClick(R.id.btn_stop_service)
    public void stopServiceClick(View view) {
        stopService(new Intent(this, LearnStartService.class));
    }

    @OnClick(R.id.btn_start_service2)
    public void startServiceIIClick(View view) {
        startService(new Intent(this, LearnStartServiceII.class));
    }

    @OnClick(R.id.btn_stop_service2)
    public void stopServiceIIClick(View view) {
        stopService(new Intent(this, LearnStartServiceII.class));
    }


    @OnClick(R.id.btn_bind_service)
    public void bindService() {
        Intent intent = new Intent(this, LearnBindService.class);
        bindService(intent, bindServiceConnection, Service.BIND_AUTO_CREATE);
    }

    private ServiceConnection bindServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected: ");
            LearnBindService.BindServiceBinder binder = (LearnBindService.BindServiceBinder) service;
            LearnBindService learnBindService = binder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected: ");
        }
    };

}
