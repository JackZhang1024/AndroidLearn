package com.lucky.androidlearn.yoga;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class ZiRuTextView extends AppCompatTextView {

    private static final String TAG = "ZiRuTextView";
    private Paint mPaint;
    private Rect mRectF;
    private float mRadius = 4;
    private float[] radiis = new float[8];
    private boolean mSetRadius, mSetBorder;
    private int mStrokeWidth = 1;
    private int mBorderColor;
    private int mBackgroundColor = -1;
    private PaintFlagsDrawFilter paintFlagsDrawFilter;
    private float mTopRightRadius, mBottomRightRadius, mBottomLeftRadius, mTopLeftRadius;
    private float mTopBorderWidth, mRightBorderWidth, mBottomBorderWidth, mLeftBorderWidth;
    private int mTopBorderColor, mRightBorderColor, mBottomBorderColor, mLeftBorderColor;
    private Bitmap mBackgroundBitmap;
    private Matrix matrix = null;

    public ZiRuTextView(Context context) {
        this(context, null);
    }

    public ZiRuTextView(Context context, @Nullable AttributeSet attrs) {
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
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF.left = 0;
        mRectF.top = 0;
        mRectF.right = w;
        mRectF.bottom = h;
        matrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int halfStrokeWidth = (int) (mStrokeWidth / 2 + 0.5f);
        adjustAllRadius(width, height);
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
            // 计算收缩比例
            float bmpWidth = mBackgroundBitmap.getWidth();
            float bmpHeight = mBackgroundBitmap.getHeight();
            float xScale = width / bmpWidth;
            float yScale = height / bmpHeight;
            xScale = xScale == 0 ? 1 : xScale;
            yScale = yScale == 0 ? 1 : yScale;
            matrix.postScale(xScale, yScale);
            Bitmap desiredBitmap = Bitmap.createBitmap(mBackgroundBitmap, 0, 0, mBackgroundBitmap.getWidth(), mBackgroundBitmap.getHeight(), matrix, true);
            canvas.drawBitmap(desiredBitmap, 0, 0, mPaint);
        }
        if (mSetBorder) {
            if (mTopBorderWidth > 0) {
                mPaint.setColor(mTopBorderColor);
                mPaint.setStrokeWidth(mTopBorderWidth);
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
        super.onDraw(canvas);
    }

    public void setCornerRadius(boolean setCornerRadius, float topRadius, float rightRadius, float bottomRadius, float leftRadius) {
        this.mSetRadius = setCornerRadius;
        this.mTopLeftRadius = topRadius;
        this.mTopRightRadius = rightRadius;
        this.mBottomRightRadius = bottomRadius;
        this.mBottomLeftRadius = leftRadius;
    }

    public void setAllBorders(float topBorderWidth, float rightBorderWidth, float bottomBorderWidth, float leftBorderWidth) {
        mSetBorder = true;
        mTopBorderWidth = topBorderWidth;
        mRightBorderWidth = rightBorderWidth;
        mBottomBorderWidth = bottomBorderWidth;
        mLeftBorderWidth = leftBorderWidth;
        invalidate();
    }

    public void setAllBorderColors(String topBorderColor, String rightBorderColor, String bottomBorderColor, String leftBorderColor) {
        mTopBorderColor = Color.parseColor(Colors.getHexColor(topBorderColor));
        mRightBorderColor = Color.parseColor(Colors.getHexColor(rightBorderColor));
        mBottomBorderColor = Color.parseColor(Colors.getHexColor(bottomBorderColor));
        mLeftBorderColor = Color.parseColor(Colors.getHexColor(leftBorderColor));
        invalidate();
    }

    public void setBackgroundColors(int colorResourceId) {
        mBackgroundColor = colorResourceId;
        invalidate();
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

    // 调整圆弧大小
    private void adjustAllRadius(int width, int height) {
        int miniSize = Math.min(width, height);
        float halfMiniSize = miniSize / 2;
        if (mTopLeftRadius > halfMiniSize) {
            radiis[0] = halfMiniSize;
            radiis[1] = halfMiniSize;
            mTopLeftRadius = halfMiniSize;
        }
        if (mTopRightRadius > halfMiniSize) {
            radiis[2] = halfMiniSize;
            radiis[3] = halfMiniSize;
            mTopRightRadius = halfMiniSize;
        }
        if (mBottomRightRadius > halfMiniSize) {
            radiis[4] = halfMiniSize;
            radiis[5] = halfMiniSize;
            mBottomRightRadius = halfMiniSize;
        }
        if (mBottomLeftRadius > halfMiniSize) {
            radiis[6] = halfMiniSize;
            radiis[7] = halfMiniSize;
            mBottomLeftRadius = halfMiniSize;
        }
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
}
