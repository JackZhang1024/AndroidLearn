package com.lucky.androidlearn.yoga;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;

import com.facebook.yoga.android.YogaLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by zhaojianxin on 2018/12/14.
 */

public class ZiRuYogaLayout extends YogaLayout implements Target {

    private static final String TAG = "ZiRuFlexBoxLayout";
    private Rect mRectF;
    private Paint mPaint;
    private float[] radiis = new float[8];
    private boolean mSetRadius, mSetBorder;
    private int mStrokeWidth = 1;
    private int mBorderColor;
    private PaintFlagsDrawFilter paintFlagsDrawFilter;
    private Bitmap mBackgroundBitmap;
    private int mBackgroundColor = -1;
    private float mTopRightRadius, mBottomRightRadius, mBottomLeftRadius, mTopLeftRadius;
    private int mTopBorderWidth, mRightBorderWidth, mBottomBorderWidth, mLeftBorderWidth;
    private int mTopBorderColor, mRightBorderColor, mBottomBorderColor, mLeftBorderColor;

    public ZiRuYogaLayout(Context context) {
        this(context, null);
    }

    public ZiRuYogaLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mRectF = new Rect();
        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mBorderColor = Color.GRAY;
        //抗锯齿
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        //硬件加速
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF.left = 0;
        mRectF.top = 0;
        mRectF.right = w;
        mRectF.bottom = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int halfStrokeWidth = (int) (mStrokeWidth / 2 + 0.5f);
        if (mSetRadius) {
            //绘制外边框
            Path path = new Path();
            //裁切绘制图片的区域
            RectF rectF = new RectF(halfStrokeWidth, halfStrokeWidth, width - halfStrokeWidth, height - halfStrokeWidth);
            setAllRadius();
            path.addRoundRect(rectF, radiis, Path.Direction.CW);
            canvas.setDrawFilter(paintFlagsDrawFilter);
            canvas.clipPath(path);
        }
        if (mBackgroundColor != -1) {
            // 绘制纯背景色
            int a = Color.alpha(mBackgroundColor);
            int r = Color.red(mBackgroundColor);
            int g = Color.green(mBackgroundColor);
            int b = Color.blue(mBackgroundColor);
            canvas.drawARGB(a, r, g, b);
        }
        if (mBackgroundBitmap != null) {
            // 绘制背景图片
            Matrix matrix = new Matrix();
            // 计算收缩比例
            float bmpWidth = mBackgroundBitmap.getWidth();
            float bmpHeight = mBackgroundBitmap.getHeight();
            //float xScale = bmpWidth > width ? width / bmpWidth : width / bmpWidth;
            //float yScale = bmpHeight > height ? bmpHeight / height : height / bmpHeight;
            float xScale = width / bmpWidth;
            float yScale = height / bmpHeight;
            // 设置收缩比例
            float scale = xScale > yScale ? yScale : xScale;
            matrix.postScale(xScale, yScale);
            Bitmap desiredBitmap = Bitmap.createBitmap(mBackgroundBitmap, 0, 0, mBackgroundBitmap.getWidth(), mBackgroundBitmap.getHeight(), matrix, true);
            canvas.drawBitmap(desiredBitmap, 0, 0, mPaint);
        }
        if (mSetBorder) {
            if (mTopBorderWidth > 0) {
                mPaint.setColor(mTopBorderColor);
                mPaint.setStrokeWidth(mTopBorderWidth);
                //halfStrokeWidth = (int) (mTopBorderWidth / 2 + 0.5f);
                setBorderTop(width, height, halfStrokeWidth, canvas, mPaint);
            }
            if (mRightBorderWidth > 0) {
                mPaint.setColor(mRightBorderColor);
                mPaint.setStrokeWidth(mRightBorderWidth);
                setBorderRight(width, height, halfStrokeWidth, canvas, mPaint);
            }
            if (mBottomBorderWidth > 0) {
                mPaint.setColor(mBottomBorderColor);
                mPaint.setStrokeWidth(mBottomBorderWidth);
                setBorderBottom(width, height, halfStrokeWidth, canvas, mPaint);
            }
            if (mLeftBorderWidth > 0) {
                mPaint.setColor(mLeftBorderColor);
                mPaint.setStrokeWidth(mLeftBorderWidth);
                setBorderLeft(width, height, halfStrokeWidth, canvas, mPaint);
            }
        }
        super.dispatchDraw(canvas);
    }

    public void setCornerRadius(boolean setCornerRadius, float rightTop, float rightBottom, float leftBottom, float leftTop) {
        this.mSetRadius = setCornerRadius;
        this.mTopRightRadius = rightTop;
        this.mBottomRightRadius = rightBottom;
        this.mBottomLeftRadius = leftBottom;
        this.mTopLeftRadius = leftTop;
    }

    public void setBorder(int strokeWidth, int color) {
        mSetBorder = true;
        mStrokeWidth = strokeWidth;
        mBorderColor = color;
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setColor(color);
    }

    public void setAllBorders(int topBorderWidth, int rightBorderWidth, int bottomBorderWidth, int leftBorderWidth) {
        mSetBorder = true;
        mTopBorderWidth = topBorderWidth;
        mRightBorderWidth = rightBorderWidth;
        mBottomBorderWidth = bottomBorderWidth;
        mLeftBorderWidth = leftBorderWidth;
    }

    public void setAllBorderColors(String topBorderColor, String rightBorderColor, String bottomBorderColor, String leftBorderColor) {
        mTopBorderColor = Color.parseColor(Colors.getHexColor(topBorderColor));
        mRightBorderColor = Color.parseColor(Colors.getHexColor(rightBorderColor));
        mBottomBorderColor = Color.parseColor(Colors.getHexColor(bottomBorderColor));
        mLeftBorderColor = Color.parseColor(Colors.getHexColor(leftBorderColor));
    }

    public void setBackgroundBitmap(@DrawableRes int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        mBackgroundBitmap = bitmap;
    }

    public void setBackgroundColors(int colorResourceId) {
        mBackgroundColor = colorResourceId;
    }

    // 绘制单圆角边框
    private void setSingleCornerBorder(Canvas canvas, int width, int height, int halfStrokeWidth) {
        Rect rect = new Rect(halfStrokeWidth, halfStrokeWidth, width - halfStrokeWidth, height - halfStrokeWidth);
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(radiis, null, null));
        Paint paint = shapeDrawable.getPaint();
        paint.setColor(mBorderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mStrokeWidth);
        //RoundRectShape绘制的边界
        shapeDrawable.setBounds(rect);
        shapeDrawable.draw(canvas);
    }

    private void setAllRadius() {
        radiis[0] = mTopLeftRadius;
        radiis[1] = mTopLeftRadius;
        radiis[2] = mTopRightRadius;
        radiis[3] = mTopRightRadius;
        radiis[4] = mBottomRightRadius;
        radiis[5] = mBottomRightRadius;
        radiis[6] = mBottomLeftRadius;
        radiis[7] = mBottomLeftRadius;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        mBackgroundBitmap = bitmap;
        invalidate();
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }

    // 顶部边框
    private void setBorderTop(int width, int height, int halfStrokeWidth, Canvas canvas, Paint paint) {
        Path path = new Path();
        //顶部横线
        path.moveTo(0 + mTopLeftRadius + halfStrokeWidth, halfStrokeWidth);
        path.lineTo(width - mTopRightRadius - halfStrokeWidth, halfStrokeWidth);
        //左边弧线
        if (mTopLeftRadius > 0) {
            path.moveTo(0 + mTopLeftRadius + halfStrokeWidth, halfStrokeWidth);
            path.arcTo(new RectF(halfStrokeWidth, halfStrokeWidth, mTopLeftRadius * 2 - halfStrokeWidth, mTopLeftRadius * 2 - halfStrokeWidth), -90, -45);
        }
        //右边弧线
        if (mTopRightRadius > 0) {
            path.moveTo(width - mTopRightRadius - halfStrokeWidth, halfStrokeWidth);
            path.arcTo(new RectF(width - 2 * mTopRightRadius + halfStrokeWidth, halfStrokeWidth, width - halfStrokeWidth, mTopRightRadius * 2 - halfStrokeWidth),
                    -90, 45);
        }
        canvas.drawPath(path, paint);
    }

    // 底部边框
    private void setBorderBottom(int width, int height, int halfStrokeWidth, Canvas canvas, Paint paint) {
        //底部横线
        Path path = new Path();
        path.moveTo(0 + mBottomLeftRadius + halfStrokeWidth, height - halfStrokeWidth);
        path.lineTo(width - mBottomRightRadius - halfStrokeWidth, height - halfStrokeWidth);
        //左边弧线
        if (mBottomLeftRadius > 0) {
            path.moveTo(0 + mBottomLeftRadius + halfStrokeWidth, height - halfStrokeWidth);
            path.arcTo(new RectF(halfStrokeWidth, height - mBottomLeftRadius * 2 + halfStrokeWidth, mBottomLeftRadius * 2 - halfStrokeWidth, height - halfStrokeWidth),
                    90, 45);
        }
        //右边弧线
        if (mBottomRightRadius > 0) {
            path.moveTo(width - mBottomRightRadius - halfStrokeWidth, height - halfStrokeWidth);
            path.arcTo(new RectF(width - 2 * mBottomRightRadius + halfStrokeWidth, height - mBottomRightRadius * 2 + halfStrokeWidth,
                    width - halfStrokeWidth, height - halfStrokeWidth), 90, -45);
        }
        canvas.drawPath(path, paint);
    }

    // 左边边框
    private void setBorderLeft(int width, int height, int halfStrokeWidth, Canvas canvas, Paint paint) {
        Path path = new Path();
        path.moveTo(halfStrokeWidth, mTopLeftRadius + halfStrokeWidth);
        path.lineTo(halfStrokeWidth, height - mBottomLeftRadius - halfStrokeWidth);
        // 顶部弧线
        if (mTopLeftRadius > 0) {
            path.moveTo(halfStrokeWidth, mTopLeftRadius + halfStrokeWidth);
            path.arcTo(new RectF(halfStrokeWidth, halfStrokeWidth, mTopLeftRadius * 2 - halfStrokeWidth, mTopLeftRadius * 2 - halfStrokeWidth), -180, 45);
        }
        // 底部弧线
        if (mBottomLeftRadius > 0) {
            path.moveTo(halfStrokeWidth, height - mBottomLeftRadius - halfStrokeWidth);
            path.arcTo(new RectF(halfStrokeWidth, height - mBottomLeftRadius * 2 + halfStrokeWidth, mBottomLeftRadius * 2 - halfStrokeWidth, height - halfStrokeWidth),
                    -180, -45);
        }
        canvas.drawPath(path, paint);
    }

    // 右边边框
    private void setBorderRight(int width, int height, int halfStrokeWidth, Canvas canvas, Paint paint) {
        Path path = new Path();
        path.moveTo(width - halfStrokeWidth, mTopRightRadius + halfStrokeWidth);
        path.lineTo(width - halfStrokeWidth, height - mBottomRightRadius - halfStrokeWidth);
        // 顶部弧线
        if (mTopRightRadius > 0) {
            path.moveTo(width - halfStrokeWidth, mTopRightRadius + halfStrokeWidth);
            path.arcTo(new RectF(width - 2 * mTopRightRadius + halfStrokeWidth, halfStrokeWidth, width - halfStrokeWidth, mTopRightRadius * 2 - halfStrokeWidth),
                    0, -45);
        }
        // 底部弧线
        if (mBottomRightRadius > 0) {
            path.moveTo(width - halfStrokeWidth, height - mBottomRightRadius - halfStrokeWidth);
            path.arcTo(new RectF(width - 2 * mBottomRightRadius + halfStrokeWidth, height - mBottomRightRadius * 2 + halfStrokeWidth, width - halfStrokeWidth, height - halfStrokeWidth),
                    0, 45);
        }
        canvas.drawPath(path, paint);
    }


    public void setImageResource(String url) {
        //ImageLoader.getInstance(getContext()).loadImage(getContext(), this, url);
    }
}
