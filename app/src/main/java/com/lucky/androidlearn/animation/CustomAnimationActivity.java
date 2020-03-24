package com.lucky.androidlearn.animation;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfz on 2018/1/1.
 */

public class CustomAnimationActivity extends AppCompatActivity {

    @BindView(R.id.ll_showup)
    LinearLayout llContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_animation);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_popup)
    public void showUp() {
        //llContent.startAnimation(new ViewExpandAnimation(llContent, 500));
        //llContent.startAnimation(new ViewScaleAnimation());
        //llContent.startAnimation(new ViewScaleAnimation2());
        llContent.startAnimation(new ViewScaleAnimation3());
    }

}
