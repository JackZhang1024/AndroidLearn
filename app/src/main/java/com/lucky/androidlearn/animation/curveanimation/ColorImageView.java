package com.lucky.androidlearn.animation.curveanimation;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.lucky.androidlearn.R;

public class ColorImageView extends AppCompatImageView {

    private ColorStateList tint;

    public ColorImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorImageView);

        tint = a.getColorStateList(R.styleable.ColorImageView_ci_color);
        updateTint();

        a.recycle();
    }

    public void setTint(ColorStateList tint) {
        this.tint = tint;
        updateTint();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (tint != null && tint.isStateful()) {
            updateTint();
        }
    }

    private void updateTint() {
        int color;
        if (tint == null) {
            color = 0xffffffff;
        } else {
            color = tint.getColorForState(getDrawableState(), tint.getDefaultColor());
        }
        setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }
}
