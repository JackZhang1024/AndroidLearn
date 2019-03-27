package com.lucky.androidlearn.performance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.debug.hv.ViewServer;
import com.lucky.androidlearn.BuildConfig;
import com.lucky.androidlearn.R;

public class HierarchyViewerActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traceview);
        if (BuildConfig.DEBUG) {
            ViewServer.get(this).addWindow(this);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (BuildConfig.DEBUG) {
            ViewServer.get(this).setFocusedWindow(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (BuildConfig.DEBUG) {
            ViewServer.get(this).removeWindow(this);
        }
    }
}
