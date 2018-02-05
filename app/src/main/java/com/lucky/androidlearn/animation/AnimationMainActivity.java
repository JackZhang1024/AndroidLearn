package com.lucky.androidlearn.animation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.animation.curveanimation.CurveAnimationMainActivity;
import com.lucky.androidlearn.animation.objectanimation.ObjectorAnimationActivity;
import com.lucky.androidlearn.animation.transitionanimation.TransitionMainStartActivity;
import com.lucky.androidlearn.animation.transitionanimation.TransitionStartActivity;
import com.lucky.androidlearn.animation.valueanimation.ValueAnimatorActivity;
import com.lucky.androidlearn.animation.viewanimation.ViewAnimationActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class AnimationMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_view_animation)
    public void onViewAnimationClick(View view) {
        startActivity(new Intent(this, ViewAnimationActivity.class));
    }

    @OnClick(R.id.btn_object_animation)
    public void onObjectorAnimationClick(View view) {
        startActivity(new Intent(this, ObjectorAnimationActivity.class));
    }

    @OnClick(R.id.btn_property_animation)
    public void onPropertyAnimationClick(View view) {
        startActivity(new Intent(this, ObjectorAnimationActivity.class));
    }

    @OnClick(R.id.btn_value_animation)
    public void onValueAnimationClick(View view) {
        startActivity(new Intent(this, ValueAnimatorActivity.class));
    }

    @OnClick(R.id.btn_layout_animation)
    public void onLayoutAnimationClick(View view) {
        startActivity(new Intent(this, LayoutAnimationActivity.class));
    }

    @OnClick(R.id.btn_custom_animation)
    public void onCustomAnimationClick(View view) {
        startActivity(new Intent(this, CustomAnimationActivity.class));
    }

    @OnClick(R.id.btn_transition_animation)
    public void onTransitionAnimationClick(View view) {
        startActivity(new Intent(this, TransitionMainStartActivity.class));
    }

    @OnClick(R.id.btn_svg_animation)
    public void onSVGAnimationClick(View view) {
        startActivity(new Intent(this, SVGAnimationActivity.class));
    }

    @OnClick(R.id.btn_curve_animation)
    public void onCurveAnimationClick(View view) {
        startActivity(new Intent(this, CurveAnimationMainActivity.class));
    }

}
