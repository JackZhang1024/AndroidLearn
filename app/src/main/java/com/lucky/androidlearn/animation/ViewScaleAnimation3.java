package com.lucky.androidlearn.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

public class ViewScaleAnimation3 extends Animation {
    private Camera camera=new Camera();
	private int mCenterX;
	private int mCenterY;

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		mCenterX=width/2;
		mCenterY=height/2;
		setFillAfter(true);
		setDuration(5000);
		setInterpolator(new LinearInterpolator());
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		super.applyTransformation(interpolatedTime, t);
		  final Matrix matrix = t.getMatrix();   
          camera.save();   
          camera.translate(0.0f, 0.0f, (1300 - 1300.0f * interpolatedTime));   
          camera.rotateY(360 * interpolatedTime);   
          camera.getMatrix(matrix);   
          matrix.preTranslate(-mCenterX, -mCenterY);   
          matrix.postTranslate(mCenterX, mCenterY);   
          camera.restore();   
	}
}
