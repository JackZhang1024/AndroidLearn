package com.lucky.androidlearn.imageload;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.jingewenku.abrahamcaijin.commonutil.PicassoUtils;
import com.lucky.androidlearn.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GlideActivity extends AppCompatActivity {

    @BindView(R.id.iv_display)
    ImageView mIvDisplay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);
        ButterKnife.bind(this);
        //String url = "http://p2.so.qhimgs1.com/t01fb6de990d05c7752.jpg";
        //Glide.with(this).load(url).into(mIvDisplay);
        Glide.with(this).load("file:///android_asset/hurry_up.gif").into(mIvDisplay);
        //Picasso.with(this).load("file:///android_asset/hurry_up.gif").into(mIvDisplay);
    }



}
