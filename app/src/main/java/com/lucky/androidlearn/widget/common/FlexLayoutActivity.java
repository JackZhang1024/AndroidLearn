package com.lucky.androidlearn.widget.common;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;
import com.lucky.androidlearn.R;
import com.lucky.androidlearn.presentation.presenters.MainPresenter;

// https://cloud.tencent.com/developer/article/1354252
// https://www.jianshu.com/p/b3a9c4a99053

public class FlexLayoutActivity extends AppCompatActivity {
    private static final String TAG = "FlexLayoutActivity";
    private LinearLayout mRootView;
    private LinearLayout mContentView;
    private static final String ROOT_VIEW = "root_view";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flexlayout_ziru);
        mRootView = (LinearLayout) findViewById(R.id.root);
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
        Pair<Integer, View> pair = getParentViewWidth(tvTitle);
        Log.e(TAG, "calculateChildViewWidth: " + pair.first+" "+pair.second.getTag());
        int height = getParentViewHeight(tvTitle);
    }


    // TODO: 2018/11/21 通过百分比计算子控件的宽度
    private Pair<Integer, View> getParentViewWidth(View view) {
        ViewGroup parentView = (ViewGroup) view.getParent();
        ViewGroup.LayoutParams layoutParams = parentView.getLayoutParams();
        int width = layoutParams.width;
        Log.e(TAG, "getParentViewWidth: " + width + " tag " + parentView.getTag());
        if (width == ViewGroup.LayoutParams.MATCH_PARENT) {
            if (ROOT_VIEW.equals(parentView.getTag())) {
                parentView.setTag(ROOT_VIEW);
                return new Pair<>(width, parentView);
            } else {
                getParentViewWidth(parentView);
            }
        } else if (width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            return new Pair<>(width, parentView);
        }
        return new Pair<>(width, parentView);
    }

    private int getParentViewHeight(View view) {
        ViewGroup parentView = (ViewGroup) view.getParent();
        ViewGroup.LayoutParams layoutParams = parentView.getLayoutParams();
        int height = layoutParams.height;
        Log.e(TAG, "getParentViewHeight: " + height + " tag " + parentView.getTag());
        if (height == ViewGroup.LayoutParams.MATCH_PARENT) {
            if (ROOT_VIEW.equals(parentView.getTag())) {
                return height;
            } else {
                getParentViewWidth(parentView);
            }
        } else if (height == ViewGroup.LayoutParams.WRAP_CONTENT) {
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
