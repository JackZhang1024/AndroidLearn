package com.lucky.androidlearn.animation.viewanimation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.lucky.androidlearn.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfz on 2018/1/1.
 */

public class ViewAnimationActivity extends AppCompatActivity {

    @BindView(R.id.content)
    LinearLayout llContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_animation);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_translate_animation)
    public void onTranslateAnimationClick(View view) {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 500, 0, 1000);
        translateAnimation.setFillAfter(true);
        translateAnimation.setDuration(3000);
        llContent.startAnimation(translateAnimation);
    }

    @OnClick(R.id.btn_scale_animation)
    public void onScaleAnimationClick(View view) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 2.0f, 1.0f, 1.0f, ScaleAnimation.RELATIVE_TO_SELF, ScaleAnimation.RELATIVE_TO_SELF);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(3000);
        llContent.startAnimation(scaleAnimation);
    }

    @OnClick(R.id.btn_rotate_animation)
    public void onRotateAnimationClick(View view) {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 500, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(3000);
        llContent.startAnimation(rotateAnimation);
    }


    @OnClick(R.id.btn_alpha_animation)
    public void onAlphaAnimationClick(View view) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1); // 0 是全透明 1是不透明
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(3000);
        llContent.startAnimation(alphaAnimation);
    }


    @OnClick(R.id.btn_animation_set)
    public void onAnimationSetClick(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(1000);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 800, 0, 0);
        translateAnimation.setDuration(1000);
        animationSet.addAnimation(translateAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0); // 0 是全透明 1是不透明
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(1000);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setFillAfter(true);
        llContent.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}
