package com.lucky.androidlearn.plugin.proxyplugin;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;


// 占位式插件化
public class DelegatePluginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delegate_plugin);
    }


    public void startPluginActivity(View view) {

    }


}
