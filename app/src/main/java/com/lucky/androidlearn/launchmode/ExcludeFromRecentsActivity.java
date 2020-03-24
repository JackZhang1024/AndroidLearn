package com.lucky.androidlearn.launchmode;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 不会被从历史记录中找到我们的Activity 一般用到rootActivity
 */
public class ExcludeFromRecentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_exclude_recents);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_standard)
    public void onStandardClick() {
        //Intent intent = new Intent(this, StandardActivity.class);
        //startActivity(intent);
    }


}
