package com.lucky.androidlearn.yoga;

import android.graphics.Color;
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

public class YogaLayoutActivity extends AppCompatActivity {

    YogaLayout mYogaLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SoLoader.init(this, false);
        setContentView(R.layout.activity_yoga_learn);
        mYogaLayout = (YogaLayout) findViewById(R.id.yoga_layout);
        //createYogaLayout(mYogaLayout);
        //createVerticalYogaLayout(mYogaLayout);
        createComplexYogaLayout(mYogaLayout);
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
    private void createYogaLayout(YogaLayout parentView){
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


    private void createVerticalYogaLayout(YogaLayout parentView){
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


    private void createComplexYogaLayout(YogaLayout parentView){
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

}
