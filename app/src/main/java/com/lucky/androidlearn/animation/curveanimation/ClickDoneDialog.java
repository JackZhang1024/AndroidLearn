package com.lucky.androidlearn.animation.curveanimation;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import com.lucky.androidlearn.R;

/**
 * Created by zfz on 2016/12/27.
 */

public class ClickDoneDialog extends AlertDialog {

    private ImageView circle;

    protected ClickDoneDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_layout);
        circle=(ImageView) findViewById(R.id.circle);
    }

    public ImageView getCircle() {
        return circle;
    }
}
