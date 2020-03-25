package com.lucky.androidlearn.fragmentation;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lucky.androidlearn.R;

import me.yokeyword.fragmentation.SupportActivity;

public class FragmentationViewPagerActivity extends SupportActivity {

    TabLayout mTabLayout;
    ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmentation_viewpager);
        initView();
    }

    private void initView() {
        mTabLayout = findViewById(R.id.tablayout);
        mViewPager = findViewById(R.id.viewpager);
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());
        mViewPager.setAdapter(new PagerFragmentAdapter(
                getSupportFragmentManager(), "首页", "新闻", "视频", "体育", "娱乐"));
        mTabLayout.setupWithViewPager(mViewPager);
        // 如果想要缓存所有页面不需要重新onCreateView 那么就是用 setOffScreenLimits
        //mViewPager.setOffscreenPageLimit(mTabLayout.getTabCount()-1);
    }


}
