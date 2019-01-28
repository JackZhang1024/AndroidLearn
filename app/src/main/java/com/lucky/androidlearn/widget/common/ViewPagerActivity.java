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
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;

/**
 * Created by zfz on 2017/12/1.
 */

public class ViewPagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private Button mBtnCreatePages;
    private ZiRuViewPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        mViewPager = findViewById(R.id.viewpager);
        mBtnCreatePages = findViewById(R.id.create_dynamic_pages);
        adapter = new ZiRuViewPagerAdapter();
        adapter.setPageTitles(new ArrayList<String>());
        ArrayList<ViewGroup> pages = new ArrayList<>();
        adapter.setPages(pages);
        mViewPager.setAdapter(adapter);
        mBtnCreatePages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createViewPages();
            }
        });
    }


    //  pagerAdapter.notifyPageChanges(yogaLayout);
    private void createViewPages() {
        for (int i = 0; i < 4; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.layout_view_page, null, false);
            LinearLayout linearLayout = view.findViewById(R.id.ll_content);
            IndexSwipeRefreshLayout smartRefreshLayout = view.findViewById(R.id.smart_refresh);
            smartRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
            smartRefreshLayout.setRefreshFooter(new ClassicsFooter(this));
            smartRefreshLayout.setEnableLoadmore(true);
            smartRefreshLayout.setEnableAutoLoadmore(true);
            TextView tvTitle = view.findViewById(R.id.tv_title);
            tvTitle.setText("我是 "+i);
            adapter.notifyPageChanges(linearLayout);
        }
    }

}
