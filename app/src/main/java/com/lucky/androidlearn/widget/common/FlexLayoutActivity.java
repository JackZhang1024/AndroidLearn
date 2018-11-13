package com.lucky.androidlearn.widget.common;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexLine;
import com.google.android.flexbox.FlexboxLayout;
import com.lucky.androidlearn.R;

// https://cloud.tencent.com/developer/article/1354252
// https://www.jianshu.com/p/b3a9c4a99053

public class FlexLayoutActivity extends AppCompatActivity {

    private LinearLayout mRootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flexlayout_ziru);
        mRootView = (LinearLayout) findViewById(R.id.root_content);
        addFlexBoxLayout();
    }

    private void addFlexBoxLayout(){
        FlexboxLayout flexboxLayout = new FlexboxLayout(this);
        // 从左向右排列
        flexboxLayout.setFlexDirection(FlexDirection.ROW);
//        View view = flexboxLayout.getChildAt(0);
//        FlexboxLayout.LayoutParams lp = (FlexboxLayout.LayoutParams) view.getLayoutParams();
//        lp.order = -1;
//        lp.flexGrow = 2;
//        view.setLayoutParams(lp);
        addViewInFlexBoxLayout(flexboxLayout);
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,  400);
        flexboxLayout.setBackgroundColor(Color.GREEN);
        mRootView.addView(flexboxLayout, llParams);
    }

    private void addViewInFlexBoxLayout(FlexboxLayout flexboxLayout){
        TextView tvBackName = new TextView(this);
        tvBackName.setText("Back");
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        flexboxLayout.setAlignItems(AlignItems.CENTER);
        flexboxLayout.setFlexDirection(FlexDirection.COLUMN);
        flexboxLayout.addView(tvBackName, layoutParams);
    }




}
