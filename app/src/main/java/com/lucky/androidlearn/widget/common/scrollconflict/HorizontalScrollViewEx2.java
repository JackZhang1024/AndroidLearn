package com.lucky.androidlearn.widget.common.scrollconflict;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 内部拦截法
 */

public class HorizontalScrollViewEx2 extends ViewGroup {

    private static final String TAG = "HorizontalScrollViewEx";
    private int mChildrenSize;
    private int mChildWidth;
    private int mChildIndex;

    //记录上次滑动的坐标
    private int mLastX = 0;
    private int mLastY = 0;

    // 分别记录上次滑动的坐标（onInterceptTouchEvent）
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    public HorizontalScrollViewEx2(Context context) {
        super(context);
        init();
    }

    public HorizontalScrollViewEx2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
        mVelocityTracker = VelocityTracker.obtain();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercepted = false;
        int x = (int) event.getX();
        int y = (int) event.getY();
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            Log.e(TAG, "onInterceptTouchEvent: ACTION_DOWN");
            mLastX = x;
            mLastY = y;
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
                return true;
            }
            return false; // 表示不拦截事件 后面的MOVE，UP事件是可以继续传递过来的
            //return true; // 表示onInterceptTouchEvent就DOWN结束了 不会再次调用onInterceptTouchEvent() 除非重新来一个事件序列 但是要注意仅仅是不再调用
            //在OnTouchEvent事件中 属于这个事件序列的MOVE，UP事件是依旧能传递过来的，不要搞混了。
        } else {
            if (action == MotionEvent.ACTION_MOVE) {
                Log.e(TAG, "onInterceptTouchEvent: ACTION_MOVE");
                // return false 这样就表示事件序列仍然可以继续（后面的MOVE,UP事件是可以继续通过onInterceptTouchEvent方法来继续传递过来的）
                //return true 表示这个事件序列就到此结束了 后面属于这个事件序列的MOVE，UP事件是不会继续传递过来的
                //这样就可能会造成一个问题 就是UP事件传递不过来 那么点击时间也就不能成立了 无法响应
            } else if (action == MotionEvent.ACTION_UP) {
                Log.e(TAG, "onInterceptTouchEvent: ACTION_UP");
            }
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 表示追踪当前点击事件的速度
        mVelocityTracker.addMovement(event);
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                scrollBy(-deltaX, 0);
                break;
            case MotionEvent.ACTION_UP:
                /*
                 *  表示计算速度，比如：时间间隔为1000ms,在1秒内,
                 *  手指在水平方向从左向右滑过100像素，那么水平速度就是100；
                 *  计算速度+获取速度---三部曲
                 *  1. mVelocityTracker.computeCurrentVelocity(1000);
                 *  2. float xVelocity = mVelocityTracker.getXVelocity(); // 获取水平方向的滑动速度
                 *  3. float yVelocity = mVelocityTracker.getYVelocity(); // 获取垂直方向的滑动速度
                 *
                 *  注意：这里的速度是指一段时间内手指滑过的像素数，
                 *
                 *
                 * */
                int scrollX = getScrollX();
                int scrollToChildIndex = scrollX / mChildWidth;
                mVelocityTracker.computeCurrentVelocity(1000);
                float xVelocity = mVelocityTracker.getXVelocity();
                if (Math.abs(xVelocity) >= 50) {
                    mChildIndex = xVelocity > 0 ? mChildIndex - 1 : mChildIndex + 1;
                } else {
                    mChildIndex = (scrollX + mChildWidth / 2) / mChildWidth;
                }
                mChildIndex = Math.max(0, Math.min(mChildIndex, mChildrenSize - 1));
                int dx = mChildIndex * mChildWidth - scrollX; // 缓慢地滑动到目标的X坐标
                smoothScrollBy(dx, 0);
                // 对速度跟踪进行回收
                mVelocityTracker.clear();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = 0;
        int measureHeight = 0;
        final int childCount = getChildCount();
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int widthSpaceSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSpaceSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        if (childCount == 0) {
            setMeasuredDimension(0, 0);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            final View childView = getChildAt(0);
            measureHeight = childView.getMeasuredHeight();
            setMeasuredDimension(widthSpaceSize, childView.getMeasuredHeight());
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            final View childView = getChildAt(0);
            measureWidth = childView.getMeasuredWidth() * childCount;
            setMeasuredDimension(measureWidth, heightSpaceSize);
        } else {
            final View childView = getChildAt(0);
            measureWidth = childView.getMeasuredWidth() * childCount;
            measureHeight = childView.getMeasuredHeight();
            setMeasuredDimension(measureWidth, measureHeight);
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        final int childCount = getChildCount();
        mChildrenSize = childCount;
        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                final int childWidth = childView.getMeasuredWidth();
                mChildWidth = childWidth;
                childView.layout(childLeft, 0, childLeft + childWidth,
                        childView.getMeasuredHeight());
                childLeft += childWidth;
            }
        }

    }


    private void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(getScrollX(), 0, dx, dy, 500);
        invalidate();
    }


    /**
     * computeScroll: 主要功能是计算拖动的位移量，更新背景
     * 通常是用mScroller记录、计算View滚动的位置，再重写View的computeScroll()
     * 完成实际的滚动
     */
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        mVelocityTracker.recycle();
        super.onDetachedFromWindow();
    }


}
