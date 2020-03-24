package com.lucky.androidlearn.widget.common.editText;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zfz on 2018/2/1.
 */

public class keyBoardChangeActivity extends AppCompatActivity {

    private static final String TAG = "keyBoardChange";
    @BindView(R.id.rl_content)
    RelativeLayout mRlContent;
    @BindView(R.id.rl_input)
    RelativeLayout mRlInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboardchange);
        ButterKnife.bind(this);
        new KeyboardChangeListener(this).setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener(){

            @Override
            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                Log.e(TAG, "onKeyboardChange: "+isShow +" keyboardHeight "+keyboardHeight);
            }
        });
        addLayoutListener();
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
