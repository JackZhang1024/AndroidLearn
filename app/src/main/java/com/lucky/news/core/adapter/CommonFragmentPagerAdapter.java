package com.lucky.news.core.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CommonFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mPageFragments = new ArrayList<>();
    private List<String> mPageTitles = new ArrayList<>();

    public CommonFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFragments(List<Fragment> fragments) {
        this.mPageFragments = fragments;
        //notifyDataSetChanged();
    }

    public void setPageTitles(List<String> pageTitles) {
        this.mPageTitles = pageTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return mPageFragments.get(position);
    }

    @Override
    public int getCount() {
        return mPageFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (!mPageTitles.isEmpty()) {
            return mPageTitles.get(position);
        }
        return "";
    }


}
