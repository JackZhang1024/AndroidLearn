package com.lucky.androidlearn.animation.objectanimation;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.lucky.androidlearn.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfz on 2018/1/1.
 */

public class ObjectorAnimationActivity extends AppCompatActivity {

    @BindView(R.id.content)
    LinearLayout llContent;
    private boolean isTranslationX = true;
    private boolean isScaleX = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_animation);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_translation)
    public void onTranslationClick(View view) {
        if (isTranslationX) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(llContent, "translationX", 300, 200);
            animator.setDuration(2000);
            animator.start();
            isTranslationX = false;
        } else {
            ObjectAnimator animator = ObjectAnimator.ofFloat(llContent, "translationY", 300);
            animator.setDuration(2000);
            animator.start();
            isTranslationX = true;
        }
    }

    @OnClick(R.id.btn_scale)
    public void onScaleClick(View view) {
        if (isScaleX) {
            ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(llContent, "scaleX", 2.0f);
            scaleAnimator.setDuration(2000);
            scaleAnimator.start();
            isScaleX = false;
        } else {
            ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(llContent, "scaleY", 2.0f);
            scaleAnimator.setDuration(2000);
            scaleAnimator.start();
            isScaleX = true;
        }
    }

    @OnClick(R.id.btn_rotate_2d)
    public void onRotate2DClick(View view) {
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(llContent, "rotation", 360);
        rotateAnimator.setDuration(2000);
        rotateAnimator.start();
    }

    @OnClick(R.id.btn_rotate_3d)
    public void onRotate3DClick(View view) {
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(llContent, "rotationX", 360);
        //ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(llContent, "rotationY", 360);
        rotateAnimator.setDuration(2000);
        rotateAnimator.start();
    }

    @OnClick(R.id.btn_xy)
    public void onXYClick(View view) {
        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(llContent, "x", 800);
        //ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(llContent, "y", 800);
        xAnimator.setDuration(2000);
        xAnimator.start();
    }

    @OnClick(R.id.btn_alpha)
    public void onAlphaClick(View view) {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(llContent, "alpha", 0.2f);
        alphaAnimator.setDuration(2000);
        alphaAnimator.start();
    }

    @OnClick(R.id.btn_custom_property)
    public void onCustomPropertyClick(View view) {
        WrapperView wrapperView = new WrapperView(llContent);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofInt(wrapperView, "width", 700);
        alphaAnimator.setDuration(2000);
        alphaAnimator.start();
    }

    @OnClick(R.id.btn_property_holder)
    public void onPropertyValuesHolderClick(View view) {
        PropertyValuesHolder pv1= PropertyValuesHolder.ofFloat("translationX", 300);
        PropertyValuesHolder pv2= PropertyValuesHolder.ofFloat("translationY", 300);
        PropertyValuesHolder pv3= PropertyValuesHolder.ofFloat("alpha", 0);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(llContent, pv1, pv2, pv3);
        objectAnimator.setDuration(2000);
        objectAnimator.start();
    }

    @OnClick(R.id.btn_animator_set)
    public void onAnimatorSetClick(View view){
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(llContent, "translationX", 300f);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(llContent, "scaleX", 1f, 0f, 1f);
        ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(llContent, "scaleY", 1f, 0f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(2000);
        animatorSet.playTogether(objectAnimator1, objectAnimator2, objectAnimator3);
        animatorSet.start();
    }

    @OnClick(R.id.btn_animator_xml)
    public void onAnimatorXMLClick(View view){
        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.scale_x_animation);
        animator.setTarget(llContent);
        animator.start();
    }


    @OnClick(R.id.btn_view_animation)
    public void onViewAnimateClick(View view){
        llContent.animate().alpha(0.3f).x(200).y(300).withStartAction(new Runnable() {
            @Override
            public void run() {

            }
        }).withEndAction(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }


    private static class WrapperView {
        private View mTarget;

        public WrapperView(View target) {
            this.mTarget = target;
        }

        public void setWidth(int width) {
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();
        }

        public int getWidth() {
            return mTarget.getLayoutParams().width;
        }
    }

}
