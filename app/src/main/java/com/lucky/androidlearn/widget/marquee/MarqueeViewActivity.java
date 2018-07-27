package com.lucky.androidlearn.widget.marquee;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;
import com.lucky.androidlearn.R;
import com.lucky.androidlearn.dagger2learn.lesson04.ToastManager;

import java.util.ArrayList;
import java.util.List;

public class MarqueeViewActivity extends AppCompatActivity {

    MarqueeView mMarqueeView;
    List<String> mNotices = new ArrayList<>();
    Button mBtnStartFlip, mBtnStopFlip;
    Button mBtnStartCustomFlip, mBtnStopCustomFlip, mBtnRefreshData;
    CustomMarqueeView mCustomMarqueeView;
    ArrayList<String> mMarqueeData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marquee);
        mMarqueeView = (MarqueeView) findViewById(R.id.marqueeView);
        mBtnStartFlip = findViewById(R.id.btn_start_filpping);
        mBtnStopFlip = findViewById(R.id.btn_stop_filpping);
        mBtnStartCustomFlip = findViewById(R.id.btn_start_custom_filpping);
        mBtnStopCustomFlip = findViewById(R.id.btn_stop_custom_filpping);
        mBtnRefreshData = findViewById(R.id.btn_refresh_data);
        mCustomMarqueeView = (CustomMarqueeView) findViewById(R.id.custom_marqueeview);
        mBtnStartFlip.setOnClickListener(v -> {
            startFlipping();
        });
        mBtnStopFlip.setOnClickListener(v -> {
            stopFlipping();
        });
        mNotices.add("1. 哈哈哈");
        mNotices.add("2. 哈哈哈");
        mNotices.add("3. 哈哈哈");
        mNotices.add("4. 哈哈哈");
        mNotices.add("5. 哈哈哈");
        mNotices.add("6. 哈哈哈");


        mMarqueeData.add("1. 白日依山尽");
//        mMarqueeData.add("2. 黄河入海流");
//        mMarqueeData.add("3. 欲穷千里目");
//        mMarqueeData.add("4. 更上一层楼");
        mCustomMarqueeView.setMarqueeData(mMarqueeData);
        mBtnStartCustomFlip.setOnClickListener(v -> {

            mCustomMarqueeView.toggleMarquee();
        });

        mBtnStopCustomFlip.setOnClickListener(v -> {

            mCustomMarqueeView.toggleMarquee();

        });
        mBtnRefreshData.setOnClickListener(v -> {
            refreshData();
        });
        mCustomMarqueeView.setMarqueeViewItemClickListener(new CustomMarqueeView.MarqueeViewItemClickListener() {
            @Override
            public void onMarqueeViewItemClick(int position) {
                AppToastMgr.shortToast(MarqueeViewActivity.this, "position " + position);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMarqueeView.startWithList(mNotices);
    }

    private void startFlipping() {
        if (!mMarqueeView.isFlipping()) {
            mMarqueeView.startFlipping();
        }
    }

    private void stopFlipping() {
        if (mMarqueeView.isFlipping()) {
            mMarqueeView.stopFlipping();
        }
    }

    private void refreshData() {
        mMarqueeData.clear();
        mMarqueeData.add("1. 床前明月光");
//        mMarqueeData.add("2. 疑是地上霜");
//        mMarqueeData.add("3. 举头望明月");
//        mMarqueeData.add("4. 低头思故乡");
        mCustomMarqueeView.setMarqueeData(mMarqueeData);
    }

}
