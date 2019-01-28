package com.lucky.androidlearn.widget.common;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ZiRuViewPagerAdapter extends PagerAdapter {
    private static final String TAG = "ZiRuViewPagerAdapter";
    private List<ViewGroup> mPageList = new ArrayList<>();
    private List<String> mPageTitles = new ArrayList<>();

    public ZiRuViewPagerAdapter() {

    }

    public void setPages(List<ViewGroup> pages) {
        this.mPageList = pages;
    }

    public void setPageTitles(List<String> pageTitles) {
        this.mPageTitles = pageTitles;
    }

    public void setPagesWithTitles(List<ViewGroup> pages, List<String> pageTitles) {
        this.mPageList = pages;
        this.mPageTitles = pageTitles;
        notifyDataSetChanged();
    }

    public void notifyPageChanges(ViewGroup page) {
        this.mPageList.add(page);
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    // viewPager yogalayout-listview
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewGroup mItemView = mPageList.get(position);
        mItemView.setTag(position);
        container.addView(mItemView);
        return mItemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (!mPageTitles.isEmpty()) {
            return mPageTitles.get(position);
        }
        return "";
    }

    @Override
    public int getCount() {
        return mPageList.isEmpty() ? 0 : mPageList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        ViewGroup viewGroup = (ViewGroup) object;
        int position = (int) viewGroup.getTag();
        if (position >= 0) {
            return position;
        }
        return POSITION_NONE;
    }
}
