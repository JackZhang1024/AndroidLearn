package com.lucky.androidlearn.yoga;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.facebook.yoga.YogaNode;
import com.facebook.yoga.android.YogaLayout;
import com.jingewenku.abrahamcaijin.commonutil.PicassoUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ZiRuImageView extends AppCompatImageView implements Target {
    private static final String TAG = "ZiRuImageView";
    private Paint mPaint;
    private int mWidth, mHeight;
    private int mLeft, mBottom;
    private float mRadius = 0;
    private boolean mSetRadius;
    private float[] radiis = new float[8];
    private Path mPath;
    private RectF mRectF;
    private boolean mAutoWidth, mAutoHeight;

    public ZiRuImageView(Context context) {
        this(context, null);
    }

    public ZiRuImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.STROKE);
        mPath = new Path();
    }

    public void setImageResource(String url) {
        PicassoUtils.getInstance().loadImageBitmap(getContext(), url, this);
    }

    public void setImageResource(String url, boolean autoWidth, boolean autoHeight) {
        mAutoWidth = autoWidth;
        mAutoHeight = autoHeight;
        PicassoUtils.getInstance().loadImageBitmap(getContext(), url, this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRectF = new RectF(0, 0, w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mSetRadius) {
            int width = getWidth();
            int height = getHeight();
            //mRadius = Math.min(width, height) / 2;
            setAllRadius();
            mPath.addRoundRect(mRectF, radiis, Path.Direction.CW);
            canvas.clipPath(mPath);
        }
        super.onDraw(canvas);
    }

    private void setBorderColor(int borderColor) {
        mPaint.setColor(borderColor);
    }

    public void setRadius(boolean setRadius, float radius) {
        this.mSetRadius = setRadius;
        this.mRadius = radius;
    }

    private void setAllRadius() {
        for (int index = 0; index < radiis.length; index++) {
            radiis[index] = mRadius;
        }
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        if (getWidth() == 0 || getHeight() == 0) {
            int bmpWidth = bitmap.getWidth();
            int bmpHeight = bitmap.getHeight();
            float scale = getContext().getResources().getDisplayMetrics().density;
            int layoutWidth = (int) (Math.ceil(bmpWidth * scale));
            int layoutHeight = (int) (Math.ceil(bmpHeight * scale));
            if (getParent() instanceof YogaLayout) {
                YogaLayout yogaLayout = (YogaLayout) getParent();
                final YogaNode imageNode = yogaLayout.getYogaNodeForView(this);
                if (mAutoHeight) {
                    imageNode.setHeight(layoutHeight);
                }
                if (mAutoWidth) {
                    imageNode.setWidth(layoutWidth);
                }
            }
            setImageBitmap(bitmap);
            return;
        }
        setImageBitmap(bitmap);
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }
}
