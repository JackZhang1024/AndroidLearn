package com.lucky.androidlearn.performance;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lucky.androidlearn.R;
import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;
import butterknife.OnClick;

// 内存优化
public class MemoryOptimizationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_optimization);
        ButterKnife.bind(this);
        //CommonUtil commonUtil = CommonUtil.getInstance(this);
    }


    @OnClick(R.id.btn_memory_optimization)
    public void onMemoryOptimizationClick(View view){

    }


    @OnClick(R.id.btn_leakcanary)
    public void onLeakCanaryClick(View view){
        Intent intent = new Intent(this, LeakCanaryActivity.class);
        startActivity(intent);
    }

}
