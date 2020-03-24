package com.lucky.androidlearn.launchmode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfz on 2017/11/25.
 */

public class SecondStandardActivity extends AppCompatActivity {

    @BindView(R.id.tv_instance_name)
    TextView mTvInstanceName;
    @BindView(R.id.tv_task_id)
    TextView tvTaskID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_standard);
        ButterKnife.bind(this);
        mTvInstanceName.setText(this.toString());
        tvTaskID.setText(String.format("TaskID %s",this.getTaskId()));
    }

    @OnClick(R.id.btn_open_standrd1)
    public void onSecondClick(View view) {
        Intent intent = new Intent(this, StandardActivity.class);
        startActivity(intent);
    }


}
