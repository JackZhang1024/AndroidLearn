package com.lucky.androidlearn.widget.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lucky.androidlearn.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfz on 2017/8/31.
 */

public class CommonWidgetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_widget);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.seek_bar)
    public void onSeekBarClick(View view) {

    }
}
