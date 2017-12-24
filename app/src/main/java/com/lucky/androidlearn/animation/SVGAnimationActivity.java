package com.lucky.androidlearn.animation;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.lucky.androidlearn.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfz on 2018/1/2.
 */

public class SVGAnimationActivity extends AppCompatActivity {

    @BindView(R.id.image_trick)
    ImageView imageTrick;

    @BindView(R.id.image_trace)
    ImageView imageTrace;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_trick)
    public void onTrickClick(View view){
        ((Animatable) imageTrick.getDrawable()).start();
    }

    @OnClick(R.id.btn_trace)
    public void onTraceClick(View view){
        ((Animatable) imageTrace.getDrawable()).start();
    }

    @OnClick(R.id.btn_sun_earth)
    public void onSunEarthClick(View view){
        startActivity(new Intent(this, EarthMoonSystemActivity.class));
    }
}
