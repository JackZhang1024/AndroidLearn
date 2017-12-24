package com.lucky.androidlearn.animation;

import android.graphics.Matrix;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;


public class ViewExpandAnimation extends Animation {
    private static final String TAG = "ViewExpandAnimation";
    private View mAnimationView = null;
    private int height = 0;

    public ViewExpandAnimation(View view) {
        animationSettings(view, 5000);
    }

    public ViewExpandAnimation(View view, int duration) {
        animationSettings(view, 5000);
    }

    private void animationSettings(View view, int duration) {
        setDuration(duration);
        mAnimationView = view;
        height = view.getHeight();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        Matrix matrix = t.getMatrix();
        matrix.preTranslate(0, (1 - interpolatedTime) * height);
        matrix.postTranslate(0, (1 - interpolatedTime) * height);
    }

}
