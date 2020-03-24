package com.lucky.androidlearn.performance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 视图优化
 */
public class ViewGraphicOptimizationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewgraphic_optimization);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_merge)
    public void onViewMergeClick(View view){
        Intent intent = new Intent(this, ViewMergeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_viewstub)
    public void onViewStubClick(View view){
        ViewStub viewStub = findViewById(R.id.view_stub);
        //viewStub.inflate();
        viewStub.setVisibility(View.VISIBLE);
    }


}
