package com.lucky.androidlearn.launchmode;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

    @BindView(R.id.tv_instance_name)
    TextView mTvInstanceName;

    @BindView(R.id.tv_task_id)
    TextView tvTaskID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard);
        ButterKnife.bind(this);
        mTvInstanceName.setText(this.toString());
        tvTaskID.setText(String.format("TaskID %s",this.getTaskId()));
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

}
