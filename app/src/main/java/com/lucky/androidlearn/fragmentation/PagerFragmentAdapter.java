package com.lucky.androidlearn.fragmentation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerFragmentAdapter extends FragmentPagerAdapter {

    private String[] mTitles;

    public PagerFragmentAdapter(@NonNull FragmentManager fm, String... titles) {
        super(fm);
        mTitles = titles;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return FirstChildFragment.newInstance();
        } else {
            return OtherChildFragment.newInstance(mTitles[position]);
        }
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }


}
