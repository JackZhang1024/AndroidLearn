package com.lucky.androidlearn.animation;

import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.lucky.androidlearn.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zfz on 2018/1/2.
 */

public class EarthMoonSystemActivity extends AppCompatActivity {

    @BindView(R.id.image_sun_earth)
    ImageView imageViewSunEarth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sun_earth);
        ButterKnife.bind(this);
        ((Animatable) imageViewSunEarth.getDrawable()).start();
    }
}
