package com.lucky.androidlearn.window;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class FloatView extends ImageView implements View.OnClickListener {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private boolean mRelease;
    private boolean mLongPress;
    private int mX, mY;
    private int mNewX, mNewY;

    public FloatView(Context context) {
        super(context);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams();
        setOnClickListener(this);
    }

    public FloatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Runnable runner = new Runnable() {
        @Override
        public void run() {
            // 短按
            if (mRelease) {
                onClick(FloatView.this);
            }
            // 长按
            mLongPress = true;
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        mX = (int) event.getRawX();
        mY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mRelease = false;
                // 这块定的是偏移量
                mNewX = (int) event.getX();
                mNewY = (int) event.getY();
                postDelayed(runner, 300);
                break;
            case MotionEvent.ACTION_MOVE:
                // 如果在长按的情况下 那么就可以进行移动窗口操作
                if (mLongPress) {
                    // 更新窗口位置
                    updateView();
                }
                break;
            case MotionEvent.ACTION_UP:
                // 手指抬起 释放 将长按设置成false
                mRelease = true;
                if (mLongPress) {
                    mLongPress = false;
                }
                break;
        }
        return true;
    }

    private void updateView() {
        if (mLayoutParams == null) {
            return;
        }
        mLayoutParams.x = mX - mNewX;
        mLayoutParams.y = mY - mNewY;
        mWindowManager.updateViewLayout(this, mLayoutParams);
    }


    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(), "dad", Toast.LENGTH_SHORT).show();
    }


    private boolean isAdd;

    public void show() {
        if (isAdd) {
            return;
        }
        isAdd = true;
        mLayoutParams.flags =
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
        ;
        mLayoutParams.width = 200;
        mLayoutParams.height = 200;
        mLayoutParams.x = 0;
        mLayoutParams.y = 0;
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.format = PixelFormat.TRANSPARENT;
        // >24 phone  <24 Toast
        if (Build.VERSION.SDK_INT > 24) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        }
        mWindowManager.addView(this, mLayoutParams);
    }

    public void dismiss() {
        if (isAdd) {
            mWindowManager.removeView(this);
            isAdd = false;
        }
    }


}
