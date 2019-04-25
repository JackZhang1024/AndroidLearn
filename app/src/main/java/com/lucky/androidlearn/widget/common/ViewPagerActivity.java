package com.lucky.androidlearn.widget.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.widget.WrapContentViewPager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zfz on 2017/12/1.
 */

public class ViewPagerActivity extends AppCompatActivity {
    private WrapContentViewPager mViewPager;
    private Button mBtnCreatePages;
    private ZiRuViewPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        mViewPager = findViewById(R.id.viewpager);
        mBtnCreatePages = findViewById(R.id.create_dynamic_pages);
        adapter = new ZiRuViewPagerAdapter(createViewPagerViews());
        //adapter.setPageTitles(new ArrayList<String>());
        ArrayList<ViewGroup> pages = new ArrayList<>();
        //adapter.setPages(createViewPagerViews());
        mViewPager.setAdapter(adapter);
        mBtnCreatePages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //createViewPages();
            }
        });
    }

    private List<ViewGroup> createViewPagerViews(){
        List<ViewGroup> viewGroups = new ArrayList<>();
        ViewGroup viewGroupOne = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.layout_viewpager_item_one, null, false);
        ViewGroup viewGroupTwo = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.layout_viewpager_item_two, null, false);
        viewGroups.add(viewGroupOne);
        viewGroups.add(viewGroupTwo);
        return viewGroups;
    }


    //  pagerAdapter.notifyPageChanges(yogaLayout);
    private void createViewPages() {
        for (int i = 0; i < 4; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.layout_view_page, null, false);
            LinearLayout linearLayout = view.findViewById(R.id.ll_content);
            SmartRefreshLayout smartRefreshLayout = view.findViewById(R.id.smart_refresh);
            smartRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
            smartRefreshLayout.setRefreshFooter(new ClassicsFooter(this));
            smartRefreshLayout.setEnableLoadmore(true);
            smartRefreshLayout.setEnableAutoLoadmore(true);
            // 当刷新布局不满的时候也可以上拉加载 setEnableLoadmoreWhenContentNotFull(true)
            smartRefreshLayout.setEnableLoadmoreWhenContentNotFull(true);
//            LinearLayout refreshLayout = view.findViewById(R.id.ll_refresh_content);
//            for (int index = 0; index < 20; index++) {
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
//                View refreshItem = LayoutInflater.from(this).inflate(R.layout.layout_item_refresh, null, false);
//                TextView tvItem = refreshItem.findViewById(R.id.tv_order);
//                tvItem.setText("我是第 "+index+"个");
//                refreshLayout.addView(refreshItem, layoutParams);
//            }
            adapter.notifyPageChanges(linearLayout);
        }
    }

}
