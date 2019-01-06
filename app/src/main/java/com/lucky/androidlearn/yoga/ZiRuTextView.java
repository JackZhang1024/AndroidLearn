package com.lucky.androidlearn.yoga;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

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
    private int mTopBorderWidth, mRightBorderWidth, mBottomBorderWidth, mLeftBorderWidth;
    private int mTopBorderColor, mRightBorderColor, mBottomBorderColor, mLeftBorderColor;
    public static final int TEXT_ALIGN_LEFT = 0x00000001;
    public static final int TEXT_ALIGN_RIGHT = 0x00000010;
    public static final int TEXT_ALIGN_CENTER_VERTICAL = 0x00000100;
    public static final int TEXT_ALIGN_CENTER_HORIZONTAL = 0x00001000;
    public static final int TEXT_ALIGN_TOP = 0x00010000;
    public static final int TEXT_ALIGN_BOTTOM = 0x00100000;

    /**
     * 文本中轴线X坐标
     */
    private float textCenterX;
    /**
     * 文本baseline线Y坐标
     */
    private float textBaselineY;
    private Paint.FontMetrics fm;
    /**
     * 要显示的文字
     */
    private String text;
    /**
     * 文字的颜色
     */
    private int textColor;
    /**
     * 文字的大小
     */
    private int textSize;
    /**
     * 文字的方位
     */
    private int textAlign;
    private int viewWidth, viewHeight;

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
        //硬件加速
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        textAlign = TEXT_ALIGN_CENTER_HORIZONTAL | TEXT_ALIGN_CENTER_VERTICAL;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        mRectF.left = 0;
        mRectF.top = 0;
        mRectF.right = w;
        mRectF.bottom = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int halfStrokeWidth = (int) (mStrokeWidth / 2 + 0.5f);
        Log.e(TAG, "onDraw: 是否重绘.........");
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

    public void setCornerRadius(boolean setCornerRadius, float rightTop, float rightBottom, float leftBottom, float leftTop) {
        this.mSetRadius = setCornerRadius;
        this.mTopRightRadius = rightTop;
        this.mBottomRightRadius = rightBottom;
        this.mBottomLeftRadius = leftBottom;
        this.mTopLeftRadius = leftTop;
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

    public void setBackgroundColors(int colorResourceId) {
        mBackgroundColor = colorResourceId;
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

    /**
     * 定位文本绘制的位置
     */
    private void setTextLocation() {
        mPaint.setTextSize(textSize);
        mPaint.setColor(textColor);
        fm = mPaint.getFontMetrics();
        //文本的宽度
        float textWidth = mPaint.measureText(text);
        float textCenterVerticalBaselineY = viewHeight / 2 - fm.descent + (fm.descent - fm.ascent) / 2;
        switch (textAlign) {
            case TEXT_ALIGN_CENTER_HORIZONTAL | TEXT_ALIGN_CENTER_VERTICAL:
                textCenterX = (float) viewWidth / 2;
                textBaselineY = textCenterVerticalBaselineY;
                break;
            case TEXT_ALIGN_LEFT | TEXT_ALIGN_CENTER_VERTICAL:
                textCenterX = textWidth / 2;
                textBaselineY = textCenterVerticalBaselineY;
                break;
            case TEXT_ALIGN_RIGHT | TEXT_ALIGN_CENTER_VERTICAL:
                textCenterX = viewWidth - textWidth / 2;
                textBaselineY = textCenterVerticalBaselineY;
                break;
            case TEXT_ALIGN_BOTTOM | TEXT_ALIGN_CENTER_HORIZONTAL:
                textCenterX = viewWidth / 2;
                textBaselineY = viewHeight - fm.bottom;
                break;
            case TEXT_ALIGN_TOP | TEXT_ALIGN_CENTER_HORIZONTAL:
                textCenterX = viewWidth / 2;
                textBaselineY = -fm.ascent;
                break;
            case TEXT_ALIGN_TOP | TEXT_ALIGN_LEFT:
                textCenterX = textWidth / 2;
                textBaselineY = -fm.ascent;
                break;
            case TEXT_ALIGN_BOTTOM | TEXT_ALIGN_LEFT:
                textCenterX = textWidth / 2;
                textBaselineY = viewHeight - fm.bottom;
                break;
            case TEXT_ALIGN_TOP | TEXT_ALIGN_RIGHT:
                textCenterX = viewWidth - textWidth / 2;
                textBaselineY = -fm.ascent;
                break;
            case TEXT_ALIGN_BOTTOM | TEXT_ALIGN_RIGHT:
                textCenterX = viewWidth - textWidth / 2;
                textBaselineY = viewHeight - fm.bottom;
                break;
        }
    }

    public void setShowText(String showText){
        this.text = showText;
    }

//    /**
//     * 设置文本内容
//     *
//     * @param text
//     */
//    public void setText(String text) {
//        this.text = text;
//        invalidate();
//    }
//
//    /**
//     * 设置文本大小
//     *
//     * @param textSizeSp 文本大小，单位是sp
//     */
//    public void setTextSize(int textSizeSp) {
//        this.textSize = DisplayUtils.sp2px(getContext(), textSizeSp);
//        //invalidate();
//    }
//
//    /**
//     * 设置文本的方位
//     */
//    public void setTextAlign(int textAlign) {
//        this.textAlign = textAlign;
//        //invalidate();
//    }
//
//    /**
//     * 设置文本的颜色
//     *
//     * @param textColor
//     */
//    public void setTextColor(int textColor) {
//        this.textColor = textColor;
//        //invalidate();
//    }

}
