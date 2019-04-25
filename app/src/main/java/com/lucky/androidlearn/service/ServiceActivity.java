package com.lucky.androidlearn.service;

import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
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

import java.util.List;

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(bindServiceConnection);
        Log.e(TAG, "onDestroy: ....");
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


    @OnClick(R.id.btn_start_job_service)
    public void startJobService() {
        MyJobService.enqueueWork(this, new Intent());
    }

    @OnClick(R.id.btn_stop_job_service)
    public void stopJobService() {
        stopService(new Intent(this, MyJobService.class));
    }

    JobScheduler mJobScheduler;

    @OnClick(R.id.btn_start_location_jobservice)
    public void startLocationJobService() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            mJobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            ComponentName componentName = new ComponentName(this, LocationJobService.class);
            JobInfo.Builder builder = new JobInfo.Builder(997, componentName);

            if (Build.VERSION.SDK_INT >= 24) {
//                builder.setMinimumLatency(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS); //执行的最小延迟时间
//                builder.setOverrideDeadline(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);  //执行的最长延时时间
//                builder.setMinimumLatency(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);
//                builder.setBackoffCriteria(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS, JobInfo.BACKOFF_POLICY_LINEAR);//线性重试方案
                builder.setMinimumLatency(100); //执行的最小延迟时间
                builder.setOverrideDeadline(100);  //执行的最长延时时间
                builder.setMinimumLatency(100); // 执行的最小延时
                builder.setBackoffCriteria(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS, JobInfo.BACKOFF_POLICY_LINEAR);//线性重试方案
            } else {
                //builder.setPeriodic(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);
                builder.setPeriodic(3000);
            }
            //builder.setPersisted(true);  // 设置设备重启时，执行该任务
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            builder.setRequiresCharging(true); // 当插入充电器，执行该任务
            JobInfo jobInfo = builder.build();
            mJobScheduler.schedule(jobInfo);
        }
    }


    @OnClick(R.id.btn_stop_location_jobservice)
    public void stopLocationJobService() {
        List<JobInfo> allJobs = mJobScheduler.getAllPendingJobs();
        for (JobInfo jobInfo : allJobs) {
            Log.d(TAG, String.format("Cancel %d", jobInfo.getId()));
            mJobScheduler.cancel(jobInfo.getId());
        }
    }


}
