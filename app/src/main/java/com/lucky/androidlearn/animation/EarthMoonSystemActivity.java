package com.lucky.androidlearn.animation;

import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
