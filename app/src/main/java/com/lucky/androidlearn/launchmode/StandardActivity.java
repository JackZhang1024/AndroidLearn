package com.lucky.androidlearn.launchmode;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lucky.androidlearn.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfz on 2017/11/25.
 */

public class StandardActivity extends AppCompatActivity {
    private static final String TAG = "StandardActivity";
    @BindView(R.id.tv_instance_name)
    TextView mTvInstanceName;

    @BindView(R.id.tv_task_id)
    TextView tvTaskID;

    private static final String SAVE_STATE_KEY = "standard-key";

    //android:configChanges="orientation|screenSize" 在清单文件中使用该属性 则不会在配置发生改变时 Activity进行重建

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard);
        if (savedInstanceState != null) {
            String savedInstanceStateValue = (String) savedInstanceState.get(SAVE_STATE_KEY);
            Log.e(TAG, "onCreate: savedInstanceStateValue " + savedInstanceStateValue);
        }
        Log.e(TAG, "onCreate: ");
        ButterKnife.bind(this);
        mTvInstanceName.setText(this.toString());
        tvTaskID.setText(String.format("TaskID %s", this.getTaskId()));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            String savedInstanceStateValue = (String) savedInstanceState.get(SAVE_STATE_KEY);
            Log.e(TAG, "onRestoreInstanceState: " + savedInstanceStateValue);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }

    @OnClick(R.id.btn_open_standrd2)
    public void onSecondClick(View view) {
        Intent intent = new Intent(this, SecondStandardActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_open_standrd)
    public void onFirstClick(View view) {
        Intent intent = new Intent(this, StandardActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_open_singletop)
    public void onSingleTopClick(View view) {
        Intent intent = new Intent(this, SingleTopActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_open_singletask)
    public void onSingleTaskClick(View view) {
        Intent intent = new Intent(this, SingleTaskActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_open_singleinstance)
    public void onSingleInstanceClick(View view) {
        Intent intent = new Intent(this, SingleInstanceActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(SAVE_STATE_KEY, "stanard-value");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;
        if (Configuration.ORIENTATION_LANDSCAPE == orientation) {
            Log.e(TAG, "onConfigurationChanged: 横屏显示");
        } else {
            Log.e(TAG, "onConfigurationChanged: 竖屏显示");
        }
    }
}
