package com.lucky.androidlearn.yoga;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.soloader.SoLoader;
import com.facebook.yoga.YogaAlign;
import com.facebook.yoga.YogaDisplay;
import com.facebook.yoga.YogaEdge;
import com.facebook.yoga.YogaFlexDirection;
import com.facebook.yoga.YogaJustify;
import com.facebook.yoga.YogaNode;
import com.facebook.yoga.YogaWrap;
import com.facebook.yoga.android.YogaLayout;
import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;
import com.lucky.androidlearn.R;
import com.lucky.androidlearn.exception.toast.ToastUtil;

public class YogaLayoutActivity extends AppCompatActivity {

    private YogaLayout mYogaLayout;
    private Button mBtnChangeBackground, mBtnDisplayScrollNone;
    private ZiRuTextView textView;
    private YogaNode mTvYogaNode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SoLoader.init(this, false);
        setContentView(R.layout.activity_yoga_learn);
        mYogaLayout = (YogaLayout) findViewById(R.id.yoga_layout);
        mBtnChangeBackground = (Button) findViewById(R.id.btn_change_background);
        mBtnDisplayScrollNone = (Button) findViewById(R.id.btn_scroll_display_none);
        LinearLayout llOuter = findViewById(R.id.ll_outer);
        llOuter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppToastMgr.shortToast(YogaLayoutActivity.this, "Outer");
            }
        });
        TextView tvInner = findViewById(R.id.tv_inner);
//        tvInner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AppToastMgr.shortToast(YogaLayoutActivity.this, "Inner");
//            }
//        });
        //createYogaLayout(mYogaLayout);
        //createVerticalYogaLayout(mYogaLayout);
        //createComplexYogaLayout(mYogaLayout);
//        createComplexZiRuTextView(mYogaLayout);
//        mBtnChangeBackground.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mTvYogaNode.setWidth(200);
//                mTvYogaNode.setHeight(200);
//                textView.setWidth(200);
//                textView.setHeight(200);
////              mTvYogaNode.calculateLayout(200, 200);
//                textView.setBackgroundColors(Color.GREEN);
//            }
//        });
//        // ["1px","1px","1px","1px"]
//        try {
//            String styleJsonValue = "[\"1px\",\"1px\",\"1px\",\"1px\"]";
//            JSONArray jsonArray = new JSONArray(styleJsonValue);
//            for (int index = 0; index < jsonArray.length(); index++) {
//                String value = jsonArray.getString(index);
//                System.out.println("value "+value);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        createInnerScrollLayout(mYogaLayout);
        createInnerScrollLayoutNew(mYogaLayout);
//        switchHiddenViewInScrollView(mYogaLayout);
//        mBtnDisplayScrollNone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                oneYogaLayout.getYogaNode().setDisplay(YogaDisplay.NONE);
//                oneYogaLayout.setVisibility(View.GONE);
//                twoYogaLayout.getYogaNode().setDisplay(YogaDisplay.NONE);
//                twoYogaLayout.setVisibility(View.GONE);
//            }
//        });
    }

    public void handYoGaNodeDefault(YogaNode yogaNode) {
        yogaNode.setFlexDirection(YogaFlexDirection.ROW);
        yogaNode.setWrap(YogaWrap.NO_WRAP);
        yogaNode.setJustifyContent(YogaJustify.FLEX_START);
        yogaNode.setFlexGrow(0);
        yogaNode.setFlexShrink(0);
        yogaNode.setAlignSelf(YogaAlign.AUTO);
    }

    // FlexGrow相当于原生的layout_weight widthPercent是100 会直接占父容器的全部宽度
    // FlexGrow跟FlexDirection的方向有关系
    // yogaNode的padding设置只对YogaLayout起作用，其他控件不起作用
    private void createYogaLayout(YogaLayout parentView) {
        YogaLayout yogaLayout = new YogaLayout(this);
        parentView.addView(yogaLayout);
        YogaNode yogaNode = parentView.getYogaNodeForView(yogaLayout);
        yogaNode.setFlexDirection(YogaFlexDirection.ROW);
        yogaNode.setWrap(YogaWrap.NO_WRAP);
        //yogaNode.setJustifyContent(YogaJustify.SPACE_AROUND);
        yogaNode.setFlexGrow(0);
        yogaNode.setFlexShrink(0);
        yogaNode.setAlignSelf(YogaAlign.AUTO);
        yogaNode.setMargin(YogaEdge.LEFT, 40);
        yogaNode.setPadding(YogaEdge.ALL, 40);

        TextView tvMusic = new TextView(this);
        tvMusic.setBackgroundColor(Color.YELLOW);
        tvMusic.setText("Music");
        tvMusic.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tvMusic.setPadding(40, 40, 40, 40);
        yogaLayout.addView(tvMusic);
        YogaNode tvMusicNode = yogaLayout.getYogaNodeForView(tvMusic);
        //tvMusicNode.setHeight(100);
//        tvMusicNode.setPadding(YogaEdge.TOP, 10);
//        tvMusicNode.setPadding(YogaEdge.BOTTOM, 10);
        tvMusicNode.setWidthPercent(40);
        //tvMusicNode.setMargin(YogaEdge.LEFT, 40);
//        tvMusicNode.setFlexGrow(1);
        //tvMusicNode.setWidth(100);

        TextView tvMovie = new TextView(this);
        tvMovie.setBackgroundColor(Color.RED);
        tvMovie.setText("Movie");
        tvMovie.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        yogaLayout.addView(tvMovie);
        YogaNode tvMovieNode = yogaLayout.getYogaNodeForView(tvMovie);
        tvMovieNode.setHeight(200);
        //tvMovieNode.setWidth(100);
        tvMovieNode.setWidthPercent(60);
    }


    private void createVerticalYogaLayout(YogaLayout parentView) {
        YogaLayout yogaLayout = new YogaLayout(this);
        parentView.addView(yogaLayout);
        YogaNode yogaNode = parentView.getYogaNodeForView(yogaLayout);
        yogaNode.setFlexDirection(YogaFlexDirection.COLUMN);
        yogaNode.setWrap(YogaWrap.NO_WRAP);
        //yogaNode.setJustifyContent(YogaJustify.SPACE_AROUND);
        yogaNode.setFlexGrow(1);
//        yogaNode.setFlexShrink(0);
        yogaNode.setAlignSelf(YogaAlign.AUTO);

        TextView tvMusic = new TextView(this);
        tvMusic.setText("Music");
        tvMusic.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        yogaLayout.addView(tvMusic);
        YogaNode tvMusicNode = yogaLayout.getYogaNodeForView(tvMusic);
        //tvMusicNode.setHeight(100);
//        tvMusicNode.setPadding(YogaEdge.TOP, 10);
//        tvMusicNode.setPadding(YogaEdge.BOTTOM, 10);
        tvMusicNode.setWidthPercent(40);
        //tvMusicNode.setHeightPercent(100);
        tvMusicNode.setFlexGrow(1);
        //tvMusicNode.setMargin(YogaEdge.LEFT, 40);
//        tvMusicNode.setFlexGrow(1);
        //tvMusicNode.setWidth(100);

        TextView tvMovie = new TextView(this);
        tvMovie.setText("Movie");
        tvMovie.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        yogaLayout.addView(tvMovie);
        YogaNode tvMovieNode = yogaLayout.getYogaNodeForView(tvMovie);
        tvMovieNode.setHeight(200);
        //tvMovieNode.setHeightPercent(100);
        //tvMovieNode.setWidth(100);
        tvMovieNode.setWidthPercent(60);
    }


    private void createComplexYogaLayout(YogaLayout parentView) {
        YogaLayout yogaLayout = new YogaLayout(this);
        yogaLayout.setBackgroundColor(Color.YELLOW);
        parentView.addView(yogaLayout);
        YogaNode yogaNode = parentView.getYogaNodeForView(yogaLayout);
        yogaNode.setFlexDirection(YogaFlexDirection.ROW);
        yogaNode.setWrap(YogaWrap.NO_WRAP);
        //yogaNode.setJustifyContent(YogaJustify.SPACE_AROUND);
        yogaNode.setFlexGrow(0);
        yogaNode.setFlexShrink(0);
        yogaNode.setPadding(YogaEdge.LEFT, 20);
        yogaNode.setPadding(YogaEdge.RIGHT, 20);
        yogaNode.setAlignSelf(YogaAlign.AUTO);
        //yogaNode.setWidthPercent(60);

        YogaLayout musicYogaLayout = new YogaLayout(this);
        musicYogaLayout.setBackgroundColor(Color.BLUE);
        yogaLayout.addView(musicYogaLayout);
        YogaNode musicNode = yogaLayout.getYogaNodeForView(musicYogaLayout);
        musicNode.setHeight(100);
        musicNode.setWidthPercent(80);
//        tvMusicNode.setFlexGrow(1);
        //tvMusicNode.setWidth(100);

    }

    private void createComplexZiRuTextView(YogaLayout parentView) {
        textView = new ZiRuTextView(this);
        parentView.addView(textView);
        textView.setId(100001);
        textView.setPadding(40, 40, 40, 40);
        textView.setDuplicateParentStateEnabled(true);
        mTvYogaNode = parentView.getYogaNodeForView(textView);
        mTvYogaNode.setMargin(YogaEdge.ALL, 20);
        textView.setAllBorderColors("red", "red", "red", "red");
        textView.setAllBorders(1, 1, 1, 1);
        textView.setCornerRadius(true, 20, 20, 20, 20);
        //textView.setBackgroundColor(Color.YELLOW);
        textView.setBackgroundColors(Color.YELLOW);
        textView.setText("Hello World!");
        textView.setGravity(Gravity.CENTER);
        //textView.setSingleLine();
        //yogaCreator.handYoGaNodeDefault(jsonObject.optJSONObject(ViewProperty.STYLE), childNode);
        //yogaCreator.getTextDisplayStyle(jsonObject, textView);
        //        //        //yogaCreator.getTextViewAlignContentStyle(true, styleJsonObj, childNode, textView);
        //yogaCreator.handViewGroupYogaNode(styleJsonObj, childNode, textView);
    }

    private void createInnerScrollLayout(YogaLayout parentView) {
        ZiRuYogaLayout yogaLayout = new ZiRuYogaLayout(this);
        yogaLayout.setBackgroundColor(Color.YELLOW);
        yogaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppToastMgr.shortToast(YogaLayoutActivity.this, "Yellow Rose ");
            }
        });
        parentView.addView(yogaLayout);
        YogaNode yogaNode = parentView.getYogaNodeForView(yogaLayout);
        yogaNode.setFlexDirection(YogaFlexDirection.COLUMN);
        yogaNode.setAlignItems(YogaAlign.CENTER);
        yogaNode.setMargin(YogaEdge.ALL, 40);
        yogaNode.setFlexGrow(1);

        ZiRuScrollView ziRuScrollView = new ZiRuScrollView(this);
        yogaLayout.addView(ziRuScrollView);
        YogaNode scrollNode = yogaLayout.getYogaNodeForView(ziRuScrollView);
        scrollNode.setMargin(YogaEdge.ALL, 20);
        ziRuScrollView.setBackgroundColor(Color.BLUE);
        scrollNode.setWidth(1000);
        scrollNode.setFlexGrow(1);
        scrollNode.setFlexShrink(0);

        //scrollNode.setHeight(0);


        // scrollView中添加YogaLayout
        ZiRuYogaLayout scrollContentYogaLayout = new ZiRuYogaLayout(this);
        scrollContentYogaLayout.setBackgroundColor(Color.GRAY);
//        scrollContentYogaLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AppToastMgr.shortToast(YogaLayoutActivity.this, "Hello ");
//            }
//        });
        YogaNode scrollContentNode = new YogaNode();
        scrollContentNode.setData(scrollContentYogaLayout);
        ziRuScrollView.addView(scrollContentYogaLayout);
        //scrollContentNode.setData(scrollContentYogaLayout);
        scrollContentNode.setFlexDirection(YogaFlexDirection.COLUMN);
        scrollContentNode.setAlignItems(YogaAlign.FLEX_END);
        scrollContentNode.setMargin(YogaEdge.ALL, 20);
        //scrollContentNode.setHeight(500);
//        scrollContentNode.setWidth(800);
//        scrollContentNode.setHeightAuto();
//        scrollContentNode.setHeight(0);
//        scrollContentNode.setHeightPercent(100);

        scrollContentNode.setFlexGrow(1);
//      scrollContentNode.setFlexShrink(0);

        // 添加文字
        ZiRuTextView ziRuTextView = new ZiRuTextView(this);
        ziRuTextView.setBackgroundColor(Color.CYAN);
        ziRuTextView.setText("1212");
        scrollContentYogaLayout.addView(ziRuTextView);
        YogaNode textYogaNode = scrollContentYogaLayout.getYogaNodeForView(ziRuTextView);
        textYogaNode.setWidth(500);
        textYogaNode.setHeight(100);

        ZiRuYogaLayout musicYogaLayout = new ZiRuYogaLayout(this);
        musicYogaLayout.setBackgroundColor(Color.BLACK);
        yogaLayout.addView(musicYogaLayout);
        YogaNode musicYogaNode = yogaLayout.getYogaNodeForView(musicYogaLayout);
        //YogaNode ziRuYogaNode = new YogaNode();
        //ziRuYogaNode.setData(ziRuYogaLayout);
        musicYogaNode.setWidth(500);
        musicYogaNode.setHeight(200);
    }

    private void createInnerScrollLayoutNew(YogaLayout parentView) {
        ZiRuYogaLayout yogaLayout = new ZiRuYogaLayout(this);
        yogaLayout.setBackgroundColor(Color.YELLOW);
        yogaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppToastMgr.shortToast(YogaLayoutActivity.this, "Yellow Rose ");
            }
        });
        parentView.addView(yogaLayout);
        YogaNode yogaNode = parentView.getYogaNodeForView(yogaLayout);
        yogaNode.setFlexDirection(YogaFlexDirection.COLUMN);
        yogaNode.setAlignItems(YogaAlign.CENTER);
        yogaNode.setMargin(YogaEdge.ALL, 40);
        yogaNode.setFlexGrow(1);


        ZiRuYogaLayout yogaLayoutInner = new ZiRuYogaLayout(this);
        yogaLayoutInner.setBackgroundColor(Color.BLUE);
        yogaLayout.addView(yogaLayoutInner);
        yogaLayoutInner.setClickable(true);
        YogaNode innerYogaNode = yogaLayout.getYogaNodeForView(yogaLayoutInner);
        innerYogaNode.setFlexDirection(YogaFlexDirection.COLUMN);
        innerYogaNode.setAlignItems(YogaAlign.CENTER);
        innerYogaNode.setFlexGrow(1);

        // 添加文字
        ZiRuTextView ziRuTextView = new ZiRuTextView(this);
        ziRuTextView.setBackgroundColor(Color.CYAN);
        ziRuTextView.setText("1212");
        yogaLayoutInner.addView(ziRuTextView);
        YogaNode textYogaNode = yogaLayoutInner.getYogaNodeForView(ziRuTextView);
        textYogaNode.setHeight(500);
        textYogaNode.setWidth(500);
    }

    private YogaLayout oneYogaLayout;
    private YogaLayout twoYogaLayout;

    private void switchHiddenViewInScrollView(YogaLayout parentView) {
        ZiRuScrollView ziRuScrollView = new ZiRuScrollView(this);
        parentView.addView(ziRuScrollView);
        YogaNode scrollYogaNode = parentView.getYogaNodeForView(ziRuScrollView);
        scrollYogaNode.setFlexGrow(1);

        ZiRuYogaLayout scrollContentYogaLayout = new ZiRuYogaLayout(this);
        scrollContentYogaLayout.setBackgroundColor(Color.GRAY);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, -1);
        YogaNode scrollContentYogaNode = scrollContentYogaLayout.getYogaNode();
        scrollContentYogaNode.setMargin(YogaEdge.ALL, 20);
        ziRuScrollView.addView(scrollContentYogaLayout, layoutParams);

        oneYogaLayout = new YogaLayout(this);
        oneYogaLayout.setBackgroundColor(Color.RED);
        scrollContentYogaLayout.addView(oneYogaLayout);
        YogaNode oneYogaNode = scrollContentYogaLayout.getYogaNodeForView(oneYogaLayout);
        oneYogaNode.setHeight(500);

        twoYogaLayout = new YogaLayout(this);
        twoYogaLayout.setBackgroundColor(Color.YELLOW);
        scrollContentYogaLayout.addView(twoYogaLayout);
        YogaNode twoYogaNode = scrollContentYogaLayout.getYogaNodeForView(twoYogaLayout);
        twoYogaNode.setHeight(500);

        YogaLayout threeYogaLayout = new YogaLayout(this);
        threeYogaLayout.setBackgroundColor(Color.BLUE);
        scrollContentYogaLayout.addView(threeYogaLayout);
        YogaNode threeYogaNode = scrollContentYogaLayout.getYogaNodeForView(threeYogaLayout);
        threeYogaNode.setHeight(500);

        YogaLayout fourYogaLayout = new YogaLayout(this);
        fourYogaLayout.setBackgroundColor(Color.GREEN);
        scrollContentYogaLayout.addView(fourYogaLayout);
        YogaNode fourYogaNode = scrollContentYogaLayout.getYogaNodeForView(fourYogaLayout);
        fourYogaNode.setHeight(500);

        YogaLayout fiveYogaLayout = new YogaLayout(this);
        fiveYogaLayout.setBackgroundColor(Color.GRAY);
        scrollContentYogaLayout.addView(fiveYogaLayout);
        YogaNode fiveYogaNode = scrollContentYogaLayout.getYogaNodeForView(fiveYogaLayout);
        fiveYogaNode.setHeight(500);

        YogaLayout sixYogaLayout = new YogaLayout(this);
        sixYogaLayout.setBackgroundColor(Color.BLACK);
        scrollContentYogaLayout.addView(sixYogaLayout);
        YogaNode sixYogaNode = scrollContentYogaLayout.getYogaNodeForView(sixYogaLayout);
        sixYogaNode.setHeight(500);

    }


}
