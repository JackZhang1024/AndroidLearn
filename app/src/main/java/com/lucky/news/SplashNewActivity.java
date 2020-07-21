package com.lucky.news;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.lucky.androidlearn.R;
import com.lucky.news.main.NewsMainActivity;

public class SplashNewActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_splash_new);
        ImageView ivSplash = findViewById(R.id.iv_splash);
        String url = "https://c-ssl.duitang.com/uploads/item/201912/10/20191210095337_3ZmyK.jpeg";
        Glide.with(this).load(url).crossFade(1000).into(ivSplash);
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashNewActivity.this, NewsMainActivity.class));
            finish();
            overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
        }, 2000);
        try {
            reportFullyDrawn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
