package com.lucky.androidlearn.animation.curveanimation;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AlertDialog;

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
