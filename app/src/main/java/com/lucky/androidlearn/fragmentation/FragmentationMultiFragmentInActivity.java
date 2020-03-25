package com.lucky.androidlearn.fragmentation;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.fragmentation.fragment.FragmentCircle;
import com.lucky.androidlearn.fragmentation.fragment.FragmentHome;
import com.lucky.androidlearn.fragmentation.fragment.FragmentMessage;
import com.lucky.androidlearn.fragmentation.fragment.FragmentMine;
import com.lucky.androidlearn.fragmentation.view.BottomBar;
import com.lucky.androidlearn.fragmentation.view.BottomBarTab;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;

// 这里的样式通常是作为一个App的首页
// 底部固定几个Tab 然后在需要加载页面的时候才去加载页面所需数据
public class FragmentationMultiFragmentInActivity extends SupportActivity implements BaseFragment.OnBackToFirstListener {


    public static final int HOME = 0;
    public static final int CIRCLE = 1;
    public static final int MESSAGE = 2;
    public static final int MINE = 3;

    private SupportFragment[] mFragments = new SupportFragment[4];

    private BottomBar mBottomBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_fragments);

        SupportFragment homeFragment = findFragment(FragmentHome.class);
        if (homeFragment == null) {
            mFragments[HOME] = FragmentHome.newInstance();
            mFragments[CIRCLE] = FragmentCircle.newInstance();
            mFragments[MESSAGE] = FragmentMessage.newInstance();
            mFragments[MINE] = FragmentMine.newInstance();

            loadMultipleRootFragment(R.id.fl_container, HOME,
                    mFragments[HOME],
                    mFragments[CIRCLE],
                    mFragments[MESSAGE],
                    mFragments[MINE]
            );
        } else {
            // 这里库已经做了Fragment恢复 所以不需要额外的处理的
            // 不会出现重叠现象
            mFragments[HOME] = homeFragment;
            mFragments[CIRCLE] = findFragment(FragmentCircle.class);
            mFragments[MESSAGE] = findFragment(FragmentMessage.class);
            mFragments[MINE] = findFragment(FragmentMine.class);
        }
        initView();
    }

    private void initView() {
        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);

        mBottomBar.addItem(new BottomBarTab(this, R.drawable.ic_home_white_24dp))
                .addItem(new BottomBarTab(this, R.drawable.ic_discover_white_24dp))
                .addItem(new BottomBarTab(this, R.drawable.ic_message_white_24dp))
                .addItem(new BottomBarTab(this, R.drawable.ic_account_circle_white_24dp));


        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            ActivityCompat.finishAfterTransition(this);
        }
    }

    @Override
    public void onBackToFirstFragment() {
        mBottomBar.setCurrentItem(0);
    }


}
