package com.lucky.androidlearn.media;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.media.image.ImageActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfz on 2017/12/24.
 */

public class MediaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_image)
    public void onImageClick() {
        startActivity(new Intent(this, ImageActivity.class));
    }
}
