package com.lucky.androidlearn.widget.marquee;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lucky.androidlearn.R;

import java.util.ArrayList;

public class CustomMarqueeView extends FrameLayout implements View.OnClickListener {

    private static final String TAG = "CustomMarqueeView";

    private ArrayList<String> marqueeData;

    private int internal = 3000;//默认滚动时间间隔

    private int textSize = 16;

    private int textColor = Color.BLACK;

    private int textGravity = Gravity.LEFT;

    private TextView child1;

    private TextView child2;

    private boolean isRunningAnim = false;

    private int currentPosition = 0;
    private boolean isInitiated;

    private FloatEvaluator floatEval = new FloatEvaluator();

    public CustomMarqueeView(Context context) {
        this(context, null);
    }

    public CustomMarqueeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomMarqueeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //init default attrs
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomMarqueeView, defStyleAttr, 0);
        internal = typedArray.getInteger(R.styleable.CustomMarqueeView_marqueeInterval, internal);
        textSize = typedArray.getDimensionPixelSize(R.styleable.CustomMarqueeView_marqueeTextSize, textSize);
        textColor = typedArray.getColor(R.styleable.CustomMarqueeView_marqueeTextColor, textColor);
        int gravity = typedArray.getInt(R.styleable.CustomMarqueeView_marqueeTextGravity, 0);
        switch (gravity) {
            case 0:
                textGravity = Gravity.LEFT;
                break;
            case 1:
                textGravity = Gravity.CENTER;
                break;
            case 2:
                textGravity = Gravity.RIGHT;
                break;
        }
        typedArray.recycle();
        initChild();
    }


    @Override

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    /**
     * 设置文字切换的时间间隔
     *
     * @param internal
     */
    public void setInterval(int internal) {
        this.internal = internal;
    }


    /**
     * 设置要滚动的字符串数据，设置完毕后会自动滚动
     * @param marqueeData
     */
    public void setMarqueeData(ArrayList<String> marqueeData) {
        if (isInitiated){
            refresh(marqueeData);
        }else{
            isInitiated = true;
            this.marqueeData = marqueeData;
            start();
        }
    }


    private void initChild() {
        if (getChildCount() == 2) {
            return;
        }
        child1 = createTextView();
        child2 = createTextView();
        addView(child1);
        addView(child2);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                ViewCompat.setTranslationY(child2, child2.getHeight());
                if (marqueeData != null) {
                    if (marqueeData.size() > 1) {
                        String childText = marqueeData.get(currentPosition);
                        String childText2 = marqueeData.get(currentPosition + 1);
                        child1.setText(childText);
                        child2.setText(childText2);
                        currentPosition += 1;
                    } else if (marqueeData.size() > 0) {
                        String childText = marqueeData.get(currentPosition);
                        child1.setText(childText);
                    }
                }

            }
        });
        setOnClickListener(this);
    }


    private TextView createTextView() {

        TextView textView = new TextView(getContext());

        textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        textView.setSingleLine();

        textView.setTextColor(textColor);

        textView.setGravity(Gravity.CENTER_VERTICAL | textGravity);

        return textView;

    }


    /**
     * 重新开始滚动，调用setMarqueeData方法后会自动调用该方法
     */

    public void start() {
        if (marqueeData == null || marqueeData.size() == 0 ) return;
        if (marqueeData.size() == 1){
            marqueeData.add(marqueeData.get(0));
        }
        if (isRunningAnim) return;
        isStarted = true;
        postDelayed(translationTask, internal);
    }


    private void refresh(ArrayList<String> marqueeData) {
        this.marqueeData = marqueeData;
        removeCallbacks(translationTask);
        removeAllViews();
        this.currentPosition = 0;
        initChild();
        start();
    }


    private boolean isStarted = false;


    /**
     * 停止滚动
     */

    public void stop() {

        removeCallbacks(translationTask);

        isStarted = false;

    }


    /**
     * 切换滚动与停止
     */

    public void toggleMarquee() {
        if (isStarted) {
            stop();
        } else {
            start();
        }
    }


    @Override

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }


    Runnable translationTask = new Runnable() {

        @Override

        public void run() {
            //doAnim
            final float startY1 = child1.getTranslationY();

            final float startY2 = child2.getTranslationY();

            final float endY1 = startY1 == 0 ? -getHeight() : 0;

            final float endY2 = startY2 == 0 ? -getHeight() : 0;

            ValueAnimator animator = ValueAnimator.ofFloat(0, 1);

            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override

                public void onAnimationUpdate(ValueAnimator animation) {

                    float fraction = animation.getAnimatedFraction();

                    child1.setTranslationY(floatEval.evaluate(fraction, startY1, endY1));

                    child2.setTranslationY(floatEval.evaluate(fraction, startY2, endY2));

                }

            });

            animator.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    isRunningAnim = true;
                    if (currentPosition == marqueeData.size()) {
                        currentPosition = 0;
                    }
                }


                @Override

                public void onAnimationEnd(Animator animation) {
                    //position
                    currentPosition += 1;
                    TextView moveView = (child1.getTranslationY() == -getHeight() ? child1 : child2);

                    moveView.setTranslationY(getHeight() * 2);
                    if (currentPosition == marqueeData.size()) {
                        moveView.setText(marqueeData.get(0));
                    } else {
                        moveView.setText(marqueeData.get(currentPosition));
                    }
                    //again
                    postDelayed(translationTask, internal);
                    isRunningAnim = false;

                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                }
            });
            animator.setDuration(getHeight() * 6).setInterpolator(new LinearInterpolator());
            animator.start();
        }

    };


    public MarqueeViewItemClickListener mMarqueeViewItemClickListener;

    public void setMarqueeViewItemClickListener(MarqueeViewItemClickListener mMarqueeViewItemClickListener) {
        this.mMarqueeViewItemClickListener = mMarqueeViewItemClickListener;
    }

    public interface MarqueeViewItemClickListener {

        void onMarqueeViewItemClick(int position);
    }

    @Override
    public void onClick(View v) {
        if (mMarqueeViewItemClickListener != null) {
            mMarqueeViewItemClickListener.onMarqueeViewItemClick(currentPosition);
        }
    }
}
