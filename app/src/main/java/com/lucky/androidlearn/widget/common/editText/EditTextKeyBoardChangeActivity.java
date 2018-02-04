package com.lucky.androidlearn.widget.common.editText;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.lucky.androidlearn.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zfz on 2018/1/31.
 */

public class EditTextKeyBoardChangeActivity extends AppCompatActivity {
    private static final String TAG = "EditTextKeyBoardChange";
    @BindView(R.id.rl_content)
    RelativeLayout mRlContent;
    @BindView(R.id.rl_input)
    RelativeLayout mRlInput;
    SoftKeyboard softKeyboard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editkeyboard);
        ButterKnife.bind(this);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        softKeyboard = new SoftKeyboard(mRlContent, inputMethodManager);
        softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {
            @Override
            public void onSoftKeyboardHide() {
                Log.e(TAG, "onSoftKeyboardHide: ");
            }

            @Override
            public void onSoftKeyboardShow() {
                Log.e(TAG, "onSoftKeyboardShow: ");
            }
        });
        addLayoutListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        softKeyboard.unRegisterSoftKeyboardCallback();
    }

    private void addLayoutListener() {
        mRlContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect mainRect = new Rect();
                mRlContent.getWindowVisibleDisplayFrame(mainRect);
                int contentInvisibleHeight = mRlContent.getRootView().getHeight() - mainRect.bottom;
                if (contentInvisibleHeight > 180) {
                    int[] location = new int[2];
                    mRlInput.getLocationInWindow(location);
                    int scrollHeight = (location[1] + mRlInput.getHeight()) - mainRect.bottom;
                    if (scrollHeight > 0) {
                        mRlContent.scrollTo(0, scrollHeight);
                    }
                } else {
                    mRlContent.scrollTo(0, 0);
                }
            }
        });
    }
}
