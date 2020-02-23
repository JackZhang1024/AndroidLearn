package com.lucky.news.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.lucky.news.main.index.IndexFragment
import com.lucky.news.main.mine.MineFragment
import com.lucky.news.main.square.SquareFragment
import com.lucky.news.main.video.VideoFragment

class HomeFragmentAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val TAG: String by lazy {
        HomeFragmentAdapter::class.java.simpleName
    }

    var mHomePageFragments = arrayListOf<Fragment>()
    var mHomePageTitles = arrayListOf<String>()

    init {
        mHomePageTitles.add("首页")
        mHomePageTitles.add("视频")
        mHomePageTitles.add("实验")
        mHomePageTitles.add("我的")
        mHomePageFragments.add(IndexFragment())
        mHomePageFragments.add(VideoFragment())
        mHomePageFragments.add(SquareFragment())
        mHomePageFragments.add(MineFragment())
        println(TAG)
    }


    override fun getItem(position: Int): Fragment {
        return mHomePageFragments[position]
    }

    override fun getCount(): Int {
        return mHomePageFragments.size;
    }


    override fun getPageTitle(position: Int): CharSequence {
        return mHomePageTitles[position]
    }


}

