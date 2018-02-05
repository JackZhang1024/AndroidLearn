package com.lucky.androidlearn.widget.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import com.lucky.androidlearn.R;
import butterknife.ButterKnife;

/**
 * http://blog.csdn.net/lovexjyong/article/details/17021319
 *
 * 全局性对话框
 * http://blog.csdn.net/caoyouxing/article/details/21105951
 *
 * http://blog.csdn.net/u012438830/article/details/78451575
 *
 * http://blog.csdn.net/xwx617/article/details/77619866
 *
 * 全屏显示
 * http://blog.csdn.net/qq_35064774/article/details/52674997
 *
 * Created by zfz on 2017/11/26.
 *
 */

public class ActivityDialogActivity extends Activity {
    private static final String TAG = "ActivityDialogActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        ButterKnife.bind(this);
        setWindowConfig();
    }

    private void setWindowConfig() {
        getWindow().setGravity(Gravity.CENTER); // 设置对话框出现的位置
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height= displayMetrics.heightPixels;
        Log.d(TAG, "setWindowConfig: width "+width+" height "+height);
        int windowWidth = width*85/100;
        int windowHeight= height*50/100;
        getWindow().setLayout(windowWidth, windowHeight);
    }

}
