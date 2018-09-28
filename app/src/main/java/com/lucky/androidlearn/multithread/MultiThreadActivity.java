package com.lucky.androidlearn.multithread;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lucky.androidlearn.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MultiThreadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multithread_safe);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_check_map_safe)
    public void onMapSafeCheck(View view){
         //MapSafeCheckTestCase checkTestCase = new MapSafeCheckTestCase();
         //checkTestCase.doTest();

         MapSafeCheckTestCase2 checkTestCase2 = new MapSafeCheckTestCase2();
         checkTestCase2.doCheckTest();
    }



}
