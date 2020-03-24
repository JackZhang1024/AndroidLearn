package com.lucky.androidlearn.widget.constraintlayout;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import butterknife.ButterKnife;

/**
 * 学习ConstraintLayout
 * http://blog.csdn.net/qq_33689414/article/details/75103731
 *
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
