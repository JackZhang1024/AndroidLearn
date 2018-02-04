package com.lucky.androidlearn.widget.constraintlayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import butterknife.ButterKnife;

/**
 * Created by zfz on 2018/2/3.
 */

public class ConstraintLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraintlayout);
        ButterKnife.bind(this);
    }
}
