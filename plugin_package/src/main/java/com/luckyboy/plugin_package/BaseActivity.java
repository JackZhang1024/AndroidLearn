package com.luckyboy.plugin_package;

import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    public Resources getResources() {
        if (getApplication() != null && getApplication().getResources() != null) {
            return getApplication().getResources();
        }
        return super.getResources();
    }


}

