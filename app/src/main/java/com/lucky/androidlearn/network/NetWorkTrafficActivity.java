package com.lucky.androidlearn.network;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lucky.androidlearn.R;


// 学习监控网络流量
// https://blog.csdn.net/f2006116/article/details/50914323
//
public class NetWorkTrafficActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "NetWorkTrafficActivity";
    private Button mBtnCollect;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_traffic);
        mBtnCollect = findViewById(R.id.btn_collect);
        mBtnCollect.setOnClickListener(this);
    }

    // 统计一个页面所用的流量
    private void collectStatistics() {
        long downloadTraffic = TrafficStats.getUidRxBytes(getAppUID(this));
        long uploadTraffic = TrafficStats.getUidTxBytes(getAppUID(this));
        Log.e(TAG, "collectStatistics: 下载 " + getFormattedStatics(downloadTraffic));
        Log.e(TAG, "collectStatistics: 上传 " + getFormattedStatics(uploadTraffic));
    }


    private int getAppUID(Context context) {
        int appUID = -1;
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            appUID = applicationInfo.uid;
            return appUID;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appUID;
    }


    // 获取格式化的网络流量统计数据
    public String getFormattedStatics(long traffics) {
        String trafficInByte = String.format("%s Bytes", traffics);
        long trafficInKB = traffics / 1024;
        return String.format("%s KB", trafficInKB);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_collect:
                collectStatistics();
                break;
        }
    }
}
