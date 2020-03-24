package com.lucky.androidlearn.yoga;

import android.content.Context;
import androidx.core.widget.NestedScrollView;
import android.util.AttributeSet;

public class ZiRuScrollView extends NestedScrollView implements NestedScrollView.OnScrollChangeListener {
    private static final String TAG = "ZiRuScrollView";
    private int mLastScrollX, mLastScrollY;

    public ZiRuScrollView(Context context) {
        this(context, null);
    }

    public ZiRuScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnScrollChangeListener(this);
    }


    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        mLastScrollX = scrollX;
        mLastScrollY = scrollY;
    }

    public int getZiRuScrollX() {
        return mLastScrollX;
    }

    public int getZiRuScrollY() {
        return mLastScrollY;
    }

}
