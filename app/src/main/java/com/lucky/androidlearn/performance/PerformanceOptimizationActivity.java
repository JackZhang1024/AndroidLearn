package com.lucky.androidlearn.performance;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lucky.androidlearn.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PerformanceOptimizationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_optimization);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_memory_optimization)
    public void onMemoryOptimizationClick(View view){
        Intent intent = new Intent(this, MemoryOptimizationActivity.class);
        startActivity(intent);
    }
}
