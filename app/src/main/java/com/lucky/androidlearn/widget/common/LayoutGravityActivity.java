package com.lucky.androidlearn.widget.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lucky.androidlearn.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LayoutGravityActivity extends AppCompatActivity {

    @BindView(R.id.ll_root)
    LinearLayout mLLRoot;
    @BindView(R.id.btn_click)
    Button mBtnClick;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravity);
        ButterKnife.bind(this);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mBtnClick.getLayoutParams();
        params.gravity = Gravity.LEFT;
        mBtnClick.setLayoutParams(params);
    }


}
