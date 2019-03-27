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
    public void onMemoryOptimizationClick(View view) {
        Intent intent = new Intent(this, MemoryOptimizationActivity.class);
        startActivity(intent);
    }

    //btn_graphic_optimization
    @OnClick(R.id.btn_graphic_optimization)
    public void onGraphicOptimizationClick(View view) {
        Intent intent = new Intent(this, ViewGraphicOptimizationActivity.class);
        startActivity(intent);
    }

    //btn_hierarchy_viewer
    @OnClick(R.id.btn_hierarchy_viewer)
    public void onHierarchyViewerClick(View view) {
        Intent intent = new Intent(this, HierarchyViewerActivity.class);
        startActivity(intent);
    }

    //btn_trace_view
    @OnClick(R.id.btn_trace_view)
    public void onTraceViewClick(View view) {
        Intent intent = new Intent(this, TraceViewActivity.class);
        startActivity(intent);
    }
}
