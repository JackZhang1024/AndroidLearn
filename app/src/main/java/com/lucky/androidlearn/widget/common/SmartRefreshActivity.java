package com.lucky.androidlearn.widget.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.lucky.androidlearn.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zfz on 2017/12/18.
 */

public class SmartRefreshActivity extends AppCompatActivity {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartrefresh);
        ButterKnife.bind(this);
        addListeners();
    }

    private void addListeners() {
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mSmartRefreshLayout.finishRefresh();
            }
        });
        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败
            }
        });
        mSmartRefreshLayout.setEnableLoadmore(true);
        mSmartRefreshLayout.setLoadmoreFinished(false);
        mSmartRefreshLayout.setEnableLoadmoreWhenContentNotFull(true);//内容不满屏幕的时候也开启加载更多
    }
}
