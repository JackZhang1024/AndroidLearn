package com.lucky.androidlearn.widget.screen;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.lucky.androidlearn.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zfz on 2017/12/6.
 */

public class ScreenDensityActivity extends AppCompatActivity {
    @BindView(R.id.tv_screen_size)
    TextView mTvScreenSize;
    @BindView(R.id.tv_screen_dpi)
    TextView mTvScreenDPI;
    @BindView(R.id.tv_screen_dot_per_inch)
    TextView mTvScreenDotPerInch;
    @BindView(R.id.tv_smallest_screen_width)
    TextView mTvSmallestScreenWidth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_density);
        ButterKnife.bind(this);
        Configuration configuration = getResources().getConfiguration();
        int smallestScreenWidthDp = configuration.smallestScreenWidthDp;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int widthPixels  = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;
        float density    = displayMetrics.density;
        int densityDpi   = displayMetrics.densityDpi;
        mTvScreenSize.setText(String.format("当前屏幕 Width  %d  Height %d", widthPixels, heightPixels));
        mTvScreenDPI.setText(String.format("当前屏幕 Density %.1f", density));
        mTvScreenDotPerInch.setText(String.format("当前屏幕  DensityDPI %d", densityDpi));
        mTvSmallestScreenWidth.setText(String.format("当前屏幕最小宽度（单位dp ）%d ", smallestScreenWidthDp));
    }
}
