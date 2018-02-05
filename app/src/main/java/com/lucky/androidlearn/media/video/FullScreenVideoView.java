package com.lucky.androidlearn.media.video;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by zfz on 2018/1/20.
 */

public class FullScreenVideoView extends VideoView {

    public FullScreenVideoView(Context context) {
        super(context);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width  = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        //MeasureSpec.getMode(widthMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
