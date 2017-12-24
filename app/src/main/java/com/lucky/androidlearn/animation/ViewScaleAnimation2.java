package com.lucky.androidlearn.animation;

import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

public class ViewScaleAnimation2 extends Animation {
	private int mCurrenX;
	private int mCurrenY;

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		mCurrenX=width/2;
		mCurrenY=height/2;
		setDuration(3000);
		setFillAfter(true);
		setInterpolator(new LinearInterpolator());
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		super.applyTransformation(interpolatedTime, t);
		final Matrix matrix=t.getMatrix();
		matrix.preTranslate(-mCurrenX, -mCurrenY);
		matrix.setScale(interpolatedTime, interpolatedTime);
		matrix.postTranslate(mCurrenX, mCurrenY);
		
	}

}
