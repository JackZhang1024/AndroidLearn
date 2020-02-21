package com.lucky.news.main.square

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.flyco.tablayout.SlidingTabLayout
import com.lucky.androidlearn.R
import com.lucky.androidlearn.jindong.util.StatusBarUtil
import com.lucky.news.core.adapter.CommonFragmentPagerAdapter
import java.util.*

class SquareFragment : Fragment() {

    private val TAG: String by lazy {
        SquareFragment::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_square, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addMarginTop(view)
        initViewData(view)
    }

    private lateinit var mSlidingTabLayout: SlidingTabLayout
    private lateinit var mViewPager: ViewPager
    private lateinit var pagerAdapter: CommonFragmentPagerAdapter
    private val mTabTitles = listOf("Java", "Android", "Kotlin", "RxJava", "架构", "杂谈", "GitHub")

    private fun initViewData(view: View?) {
        mSlidingTabLayout = view!!.findViewById(R.id.tablayout)
        mViewPager = view.findViewById(R.id.main_vp_container)
        pagerAdapter = CommonFragmentPagerAdapter(childFragmentManager)
        pagerAdapter.setFragments(getFragments())
        pagerAdapter.setPageTitles(mTabTitles)
        mViewPager.setAdapter(pagerAdapter)
        mViewPager.setOffscreenPageLimit(mTabTitles.size)
        mSlidingTabLayout.setViewPager(mViewPager)
    }

    private fun getFragments(): List<Fragment> {
        val fragments = ArrayList<Fragment>()
        fragments.add(ChannelFragment())
        fragments.add(ChannelFragment())
        fragments.add(ChannelFragment())
        fragments.add(ChannelFragment())
        fragments.add(ChannelFragment())
        fragments.add(ChannelFragment())
        fragments.add(ChannelFragment())
        return fragments
    }

    private fun addMarginTop(view: View?) {
        // 重新计算 slidingTabLayout的高度
        val child: View? = view?.findViewById(R.id.tablayout)
        val params: LinearLayout.LayoutParams = child?.layoutParams as LinearLayout.LayoutParams
        Log.e(TAG ,"params "+params.topMargin+" height  "+params.height)
        val topMargin = StatusBarUtil.getStatusBarHeight(context)
        params.height = params.height + topMargin
        child.run {
            setPadding(paddingLeft, paddingTop+topMargin, paddingRight, paddingBottom)
            params.topMargin = topMargin
            layoutParams = params
        }
    }



}