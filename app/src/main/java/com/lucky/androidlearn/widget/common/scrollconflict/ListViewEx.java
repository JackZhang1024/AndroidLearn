package com.lucky.androidlearn.widget.common.scrollconflict;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

public class ListViewEx extends ListView {

    private static final String TAG = "ListViewEx";

    //分别记录上次滑动的坐标
    private int mLastX = 0;
    private int mLastY = 0;
    private HorizontalScrollViewEx2 mHorizontalScrollViewEx2;

    public ListViewEx(Context context) {
        super(context);
    }

    public ListViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setHorizontalScrollViewEx2(HorizontalScrollViewEx2 horizontalScrollViewEx2) {
        mHorizontalScrollViewEx2 = horizontalScrollViewEx2;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "dispatchTouchEvent: ACTON_DOWN");
                // 不允许父容器进行拦截 那么就是从DOW事件开始到UP事件结束 就不会再次调用父容器的onInterceptTouchEvent方法
                // 除非 再次调用 parentView.requestDisallowInterceptTouchEvent(false); 才会调用父容器的onInterceptTouchEvent()
                // 这个不拦截 针对的是整个一个完整的事件序列 即从Down时间开始到Up事件结束
                mHorizontalScrollViewEx2.requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "dispatchTouchEvent: ACTON_MOVE");
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                Log.e(TAG, "dispatchHoverEvent: dx " + deltaX + "  dy " + deltaY);
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    // 允许父容器进行拦截 那么就会重新调用父容器的onInterceptTouchEvent事件
                    // 至于真的是否做到拦截 那不是由这句disallow来决定的，而是由父容器的onInterceptTouchEvent方法
                    // 怎么处理MOVE事件的
                    mHorizontalScrollViewEx2.requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "dispatchTouchEvent: ACTON_UP");
                break;
        }
        mLastX = x;
        mLastY = y;
        return super.dispatchTouchEvent(event);
    }

}
