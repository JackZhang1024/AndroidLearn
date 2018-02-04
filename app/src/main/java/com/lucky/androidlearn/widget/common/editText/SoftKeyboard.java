package com.lucky.androidlearn.widget.common.editText;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by zfz on 2018/2/1.
 */

public class SoftKeyboard implements View.OnFocusChangeListener {
    private static final int CLEAR_FOCUS = 0;

    private ViewGroup layout;
    private int layoutBottom;
    private InputMethodManager im;
    private int[] coords;
    private boolean isKeyboardShow;
    private SoftKeyboardChangesThread softKeyboardThread;
    private List<EditText> editTextList;

    private View tempView; // reference to a focused EditText

    public SoftKeyboard(ViewGroup layout, InputMethodManager im) {
        this.layout = layout;
        keyboardHideByDefault();
        initEditTexts(layout);
        this.im = im;
        this.coords = new int[2];
        this.isKeyboardShow = false;
        this.softKeyboardThread = new SoftKeyboardChangesThread();
        this.softKeyboardThread.start();
    }


    public void openSoftKeyboard() {
        if (!isKeyboardShow) {
            layoutBottom = getLayoutCoordinates();
            im.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
            softKeyboardThread.keyboardOpened();
            isKeyboardShow = true;
        }
    }

    public void closeSoftKeyboard() {
        if (isKeyboardShow) {
            im.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            isKeyboardShow = false;
        }
    }

    public void setSoftKeyboardCallback(SoftKeyboardChanged mCallback) {
        softKeyboardThread.setCallback(mCallback);
    }

    public void unRegisterSoftKeyboardCallback() {
        softKeyboardThread.stopThread();
    }

    public interface SoftKeyboardChanged {
        public void onSoftKeyboardHide();

        public void onSoftKeyboardShow();
    }

    private int getLayoutCoordinates() {
        layout.getLocationOnScreen(coords);
        return coords[1] + layout.getHeight();
    }

    private void keyboardHideByDefault() {
        layout.setFocusable(true);
        layout.setFocusableInTouchMode(true);
    }

    /*
     * InitEditTexts 处理嵌套在view里的edittext
     * 感谢 Francesco Verheye (verheye.francesco@gmail.com)
     */
    private void initEditTexts(ViewGroup viewgroup) {
        if (editTextList == null)
            editTextList = new ArrayList<EditText>();

        int childCount = viewgroup.getChildCount();
        for (int i = 0; i <= childCount - 1; i++) {
            View v = viewgroup.getChildAt(i);

            if (v instanceof ViewGroup) {
                initEditTexts((ViewGroup) v);
            }

            if (v instanceof EditText) {
                EditText editText = (EditText) v;
                editText.setOnFocusChangeListener(this);
                editText.setCursorVisible(true);
                editTextList.add(editText);
            }
        }
    }

    /*
     * 当keyboard展示的时候，OnFocusChange 更新 tempView
     * Thanks to Israel Dominguez (dominguez.israel@gmail.com)
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            tempView = v;
            if (!isKeyboardShow) {
                layoutBottom = getLayoutCoordinates();
                softKeyboardThread.keyboardOpened();
                isKeyboardShow = true;
            }
        }
    }

    // 将会取消Edittext的焦点
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message m) {
            switch (m.what) {
                case CLEAR_FOCUS:
                    if (tempView != null) {
                        tempView.clearFocus();
                        tempView = null;
                    }
                    break;
            }
        }
    };

    private class SoftKeyboardChangesThread extends Thread {
        private AtomicBoolean started;
        private SoftKeyboardChanged mCallback;

        public SoftKeyboardChangesThread() {
            started = new AtomicBoolean(true);
        }

        public void setCallback(SoftKeyboardChanged mCallback) {
            this.mCallback = mCallback;
        }

        @Override
        public void run() {
            while (started.get()) {
                // 等待，知道软键盘请求打卡
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                int currentBottomLocation = getLayoutCoordinates();

                // 在调用打开软键盘函数和真正显示，有一些时间差
                while (currentBottomLocation == layoutBottom && started.get()) {
                    currentBottomLocation = getLayoutCoordinates();
                }

                if (started.get())
                    mCallback.onSoftKeyboardShow();

                // 当键盘打开，出事下底部的位置比layoutBottom更好
                // 在有些情况下等价于layoutBottom
                // 这会破坏之前的逻辑，所以我加上新的循环
                while (currentBottomLocation >= layoutBottom && started.get()) {
                    currentBottomLocation = getLayoutCoordinates();
                }

                // 现在键盘已经显示，继续检查Layout的尺寸，直到键盘关闭
                while (currentBottomLocation != layoutBottom && started.get()) {
                    synchronized (this) {
                        try {
                            wait(500);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    currentBottomLocation = getLayoutCoordinates();
                }

                if (started.get())
                    mCallback.onSoftKeyboardHide();

                // 如果 keyboard 已经打开
                if (isKeyboardShow && started.get())
                    isKeyboardShow = false;

                // 如果一个EditText获得焦点，移除它焦点（在UI县城中）
                if (started.get())
                    mHandler.obtainMessage(CLEAR_FOCUS).sendToTarget();
            }
        }

        public void keyboardOpened() {
            synchronized (this) {
                notify();
            }
        }

        public void stopThread() {
            synchronized (this) {
                started.set(false);
                notify();
            }
        }

    }
}
