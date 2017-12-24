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
 * Created by zfz on 2017/11/26.
 */

public class SingleTaskActivity extends AppCompatActivity {

    @BindView(R.id.tv_instance_name)
    TextView tvInstanceName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singletask);
        ButterKnife.bind(this);
        tvInstanceName.setText(this.toString());
    }

    @OnClick(R.id.btn_standard)
    public void onStandardClick(View view){
        Intent intent = new Intent(this, StandardActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.btn_singletask)
    public void onSingleTaskClick(View view){
        Intent intent = new Intent(this, SingleTaskActivity.class);
        startActivity(intent);
    }

}
