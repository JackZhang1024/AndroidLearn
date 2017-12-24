/**
 * 2014-11-12administor2014
 */
package com.lucky.androidlearn.animation;

import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * @author administor
 */
public class ViewScaleAnimation extends Animation {

    @Override
    public void initialize(int width, int height, int parentWidth,
                           int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        setDuration(3000);
        setFillAfter(true);
        setInterpolator(new LinearInterpolator());
    }


    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        final Matrix matrix = t.getMatrix();
        matrix.setScale(1, 1 - interpolatedTime);
    }
}
