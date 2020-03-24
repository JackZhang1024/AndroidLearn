package com.lucky.news.main.square

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.flyco.tablayout.SlidingTabLayout
import com.lucky.androidlearn.R
import com.lucky.androidlearn.jindong.util.StatusBarUtil
import com.lucky.news.core.NewsConstant
import com.lucky.news.core.adapter.CommonFragmentPagerAdapter
import java.util.*

// https://github.com/huchenme/github-trending-api
class SquareFragment : Fragment() {

    private val TAG: String by lazy {
        SquareFragment::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_square, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addMarginTop(view)
        initViewData(view)
    }

    private lateinit var mSlidingTabLayout: SlidingTabLayout
    private lateinit var mViewPager: ViewPager
    private lateinit var pagerAdapter: CommonFragmentPagerAdapter
    private val mTabTitles = listOf("Java", "Android", "Kotlin", "RxJava", "架构", "杂谈", "GitHub")

    private fun initViewData(view: View) {
        mSlidingTabLayout = view.findViewById(R.id.tablayout)
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
        val javaFragment = ChannelFragment()
        javaFragment.arguments = Bundle().apply {
            putString(NewsConstant.CHANNEL_ARGUMENTS, NewsConstant.CHANNEL_TYPE_JAVA)
        }
        fragments.add(javaFragment)

        val androidFragment = ChannelFragment()
        androidFragment.arguments = Bundle().apply {
            putString(NewsConstant.CHANNEL_ARGUMENTS, NewsConstant.CHANNEL_TYPE_ANDROID)
        }
        fragments.add(androidFragment)


        val kotlinFragment = ChannelFragment()
        kotlinFragment.arguments = Bundle().apply {
            putString(NewsConstant.CHANNEL_ARGUMENTS, NewsConstant.CHANNEL_TYPE_KOTLIN)
        }
        fragments.add(kotlinFragment)


        val rxJavaFragment = ChannelFragment()
        rxJavaFragment.arguments = Bundle().apply {
            putString(NewsConstant.CHANNEL_ARGUMENTS, NewsConstant.CHANNEL_TYPE_RXJAVA)
        }
        fragments.add(rxJavaFragment)

        val frameFragment = ChannelFragment()
        frameFragment.arguments = Bundle().apply {
            putString(NewsConstant.CHANNEL_ARGUMENTS, NewsConstant.CHANNEL_TYPE_FRAME)
        }
        fragments.add(frameFragment)

        val discussionFragment = ChannelFragment()
        discussionFragment.arguments = Bundle().apply {
            putString(NewsConstant.CHANNEL_ARGUMENTS, NewsConstant.CHANNEL_TYPE_DISCUSSION)
        }
        fragments.add(discussionFragment)

        val githubFragment = ChannelFragment()
        githubFragment.arguments = Bundle().apply {
            putString(NewsConstant.CHANNEL_ARGUMENTS, NewsConstant.CHANNEL_TYPE_GITHUB)
        }
        fragments.add(githubFragment)

        return fragments
    }

    private fun addMarginTop(view: View?) {
        // 重新计算 slidingTabLayout的高度
        val child: View? = view?.findViewById(R.id.tablayout)
        val params: LinearLayout.LayoutParams = child?.layoutParams as LinearLayout.LayoutParams
        val topMargin = StatusBarUtil.getStatusBarHeight(context)
        params.height = params.height + topMargin
        child.run {
            setPadding(paddingLeft, paddingTop+topMargin, paddingRight, paddingBottom)
            params.topMargin = topMargin
            layoutParams = params
        }
    }



}