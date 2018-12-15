package com.lucky.androidlearn.yoga;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.widget.TextView;

import com.facebook.soloader.SoLoader;
import com.facebook.yoga.YogaAlign;
import com.facebook.yoga.YogaEdge;
import com.facebook.yoga.YogaFlexDirection;
import com.facebook.yoga.YogaJustify;
import com.facebook.yoga.YogaNode;
import com.facebook.yoga.YogaWrap;
import com.facebook.yoga.android.YogaLayout;
import com.lucky.androidlearn.R;

import org.json.JSONObject;

public class YogaLayoutActivity extends AppCompatActivity {

    YogaLayout mYogaLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SoLoader.init(this, false);
        setContentView(R.layout.activity_yoga_learn);
        mYogaLayout = (YogaLayout) findViewById(R.id.yoga_layout);
        addTextView();
    }

    private void addTextView() {
        TextView tvTitle = new TextView(this);
        tvTitle.setText("Hello World!");
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        mYogaLayout.addView(tvTitle);
        YogaNode tvTitleNode = mYogaLayout.getYogaNodeForView(tvTitle);
        handYoGaNodeDefault(tvTitleNode);
        //tvTitleNode.setWidth(100);
        //tvTitleNode.setWidthPercent(60.0f);
        //tvTitleNode.setWidthAuto();
        tvTitleNode.setHeight(100);
        tvTitleNode.setMargin(YogaEdge.LEFT, 20);
    }

    public void handYoGaNodeDefault(YogaNode yogaNode) {
        yogaNode.setFlexDirection(YogaFlexDirection.ROW);
        yogaNode.setWrap(YogaWrap.NO_WRAP);
        yogaNode.setJustifyContent(YogaJustify.FLEX_START);
        yogaNode.setFlexGrow(0);
        yogaNode.setFlexShrink(0);
        yogaNode.setAlignSelf(YogaAlign.AUTO);
    }

}
