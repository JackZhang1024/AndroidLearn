package com.lucky.news.main

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.FrameLayout
import com.lucky.androidlearn.R
import com.lucky.androidlearn.jindong.util.StatusBarUtil
import com.lucky.news.core.view.MainBottomTabLayout


// https://blog.csdn.net/aqi00/article/details/79374532?utm_source=distribute.pc_relevant.none-task
// https://www.jianshu.com/p/7392237bc1de
class NewsMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_bottomlayout_main)
        supportFragmentManager.beginTransaction()
                .add(R.id.container, PlaceHolderFragment())
                .commit()
        lightStatusBar()
    }

    class PlaceHolderFragment : Fragment() {

        private var mAdapter: HomeFragmentAdapter? = null
        private var mPager: ViewPager? = null
        private var mTabLayout: MainBottomTabLayout? = null

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view: View? = inflater?.inflate(R.layout.fragment_news_bottomlayout_main, container, false)
            setupViews(view)
            return view
        }


        override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

        }

        private fun setupViews(view: View?) {
            mPager = view?.findViewById(R.id.tab_pager)
            mTabLayout = view?.findViewById(R.id.main_bottom_tablayout)
            mAdapter = HomeFragmentAdapter(fragmentManager)
            mPager!!.adapter = mAdapter
            mTabLayout!!.setViewPager(mPager)
        }

    }

    // http://www.imooc.com/article/292777
    // 修改状态栏显示的内容颜色为黑色
    private fun lightStatusBar() {
        // 沉浸式状态栏 即状态栏悬浮在布局之上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            removeMarginTop()
        }
    }

    // 移除顶部状态栏所占的距离 让内容显示在状态栏之下 或者说让状态栏悬浮在内容布局之上
    private fun removeMarginTop() {
        val contentView: ViewGroup = window.findViewById(Window.ID_ANDROID_CONTENT)
        val child: View = contentView.getChildAt(0)
        val params: FrameLayout.LayoutParams = child.layoutParams as FrameLayout.LayoutParams
        params.topMargin -= StatusBarUtil.getStatusBarHeight(this)
        child.layoutParams = params
    }


}