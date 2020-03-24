package com.lucky.androidlearn.widget.common;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;
import com.lucky.androidlearn.R;

// https://cloud.tencent.com/developer/article/1354252
// https://www.jianshu.com/p/b3a9c4a99053

public class FlexLayoutActivity extends AppCompatActivity {
    private static final String TAG = "FlexLayoutActivity";
    private RelativeLayout mRootView;
    private LinearLayout mContentView;
    private static final String ROOT_VIEW = "root_view";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flexlayout_ziru);
        mRootView = (RelativeLayout) findViewById(R.id.root);
        mContentView = (LinearLayout) findViewById(R.id.content);
        mRootView.setTag(ROOT_VIEW);
        //addFlexBoxLayout();
        calculateChildViewWidth();
    }

    private void addFlexBoxLayout() {
        FlexboxLayout flexboxLayout = new FlexboxLayout(this);
        // 从左向右排列
        flexboxLayout.setFlexDirection(FlexDirection.ROW);
        addViewInFlexBoxLayout(flexboxLayout);
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400);
        flexboxLayout.setBackgroundColor(Color.GREEN);
        mContentView.addView(flexboxLayout, llParams);
        View view = mContentView.findViewWithTag("123");
        if (view instanceof TextView) {
            Log.e(TAG, "addFlexBoxLayout: findTextView ");
        }
    }

    private void addViewInFlexBoxLayout(FlexboxLayout flexboxLayout) {
        TextView tvBackName = new TextView(this);
        tvBackName.setText("Back");
        tvBackName.setTag("123");
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        flexboxLayout.setAlignItems(AlignItems.CENTER);
        flexboxLayout.setFlexDirection(FlexDirection.COLUMN);
        flexboxLayout.addView(tvBackName, layoutParams);
    }

    private void calculateChildViewWidth() {
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        int width = getParentViewWidth(tvTitle);
        Log.e(TAG, "calculateChildViewWidth: " + width);
        int height = getParentViewHeight(tvTitle);
        Log.e(TAG, "calculateChildViewHeight: " + height);
    }


    // TODO: 2018/11/21 通过百分比计算子控件的宽度
    // 1. 如果父视图是指定宽 则返回指定宽
    // 2. 如果父视图是撑满 则继续调用 直到返回具体的数值
    // 3. 暂时不考虑包裹内容的情况
    private int getParentViewWidth(View view) {
        ViewGroup parentView = (ViewGroup) view.getParent();
        ViewGroup.LayoutParams layoutParams = parentView.getLayoutParams();
        int width = layoutParams.width;
        if (width == ViewGroup.LayoutParams.MATCH_PARENT) {
            // 撑满父容器
            width = getParentViewWidth(parentView);
        } else if (width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            width = getParentViewWidth(parentView);
        } else if (width > -1) {
            // 指定宽
            return width;
        }
        return width;
    }


    private int getParentViewHeight(View view) {
        ViewGroup parentView = (ViewGroup) view.getParent();
        ViewGroup.LayoutParams layoutParams = parentView.getLayoutParams();
        int height = layoutParams.height;
        if (height == ViewGroup.LayoutParams.MATCH_PARENT) {
            height = getParentViewHeight(parentView);
        } else if (height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            height = getParentViewHeight(parentView);
        } else if (height > -1) {
            return height;
        }
        return height;
    }

//    class Pair<V, K> {
//        V first;
//        K second;
//
//        public Pair() {
//            first = null;
//            second = null;
//        }
//
//        public Pair(V f, K s) {
//            first = f;
//            second = s;
//        }
//
//        public boolean equals(Object o) {
//            if (!(o instanceof Pair)) {
//                return false;
//            }
//            Pair<V, K> pn = (Pair<V, K>) o;
//            return pn.first.equals(first) && pn.second.equals(second);
//        }
//
//        public int hashCode() {
//            return first.hashCode() + second.hashCode();
//        }
//    }

}
