package com.lucky.androidlearn.annotation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.lucky.androidlearn.R;

import butterknife.BindView;

public class SimpleButterKnifeActivity extends AppCompatActivity {


    TextView mTvContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simplebutterknife);
        SimpleButterKnife.bind(this);
    }
}
