package com.lucky.androidlearn.loadmore;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lucky.androidlearn.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

// 下拉刷新和上拉加载更多
// smartRefreshLayout作为下拉刷新布局
// 没有更多数据
public class PullRefreshLoadMoreNewActivity extends AppCompatActivity {

    @BindView(R.id.smartRefreshLayout)
    public SmartRefreshLayout mSmartRefreshLayout;

    @BindView(R.id.swiperecyclerview)
    public SwipeRecyclerView mSwipeRecyclerView;

    private DefineLoadMoreView footerView;

    private DataSource mDataSource;

    private PullRefreshAdapter mAdapter;

    private static final String TAG = "PullRefreshLoadMore";

    private int mPageNo = 1;
    private final int mTotalPageCount = 1;
    private final int mPageSize = 12;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_more_new);
        ButterKnife.bind(this);
        RecyclerViewUtils viewUtils = new RecyclerViewUtils();
        viewUtils.initRecyclerView(this, mSwipeRecyclerView, new SwipeRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                // 加载更多数据操作
                Log.e(TAG, "onLoadMore: 加载更多"+mPageNo);
                loadMoreData();
            }
        });
        mSwipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSwipeRecyclerView.addItemDecoration(new LoadMoreDecoration(this, DividerItemDecoration.VERTICAL));
        mDataSource = new DataSource();
        mAdapter = getAdapter();
        mSwipeRecyclerView.setAdapter(mAdapter);
        mSmartRefreshLayout.setEnableRefresh(true);
        mSmartRefreshLayout.setEnableLoadmore(false);
        mSmartRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
//        mSmartRefreshLayout.setRefreshHeader(new MaterialHeader(this));
//        mSmartRefreshLayout.setRefreshHeader(new WaterDropHeader(this));
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                new Handler().postDelayed(() -> {
                    mPageNo = 1;
                    Log.e(TAG, "onRefresh: 下拉刷洗"+mPageNo);
                    initLoadData();
                    refreshlayout.finishRefresh();
                }, 800);
            }
        });
        initLoadData();
    }

    private void initLoadData() {
        List<ApiResponse> responses = mDataSource.initLoadData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.setData(responses);
                mPageNo++;
                if (mPageNo <= mTotalPageCount) {
                    // 如果下一页的页脚小于等于5 则表示还有更多数据
                    mSwipeRecyclerView.loadMoreFinish(false, true);
                } else {
                    // 说明没有更多数据了
                    mSwipeRecyclerView.postDelayed(() -> {
                        mSwipeRecyclerView.loadMoreFinish(false, false);
                    }, 200);
                }
            }
        }, 1000);
    }

    private void loadMoreData() {
        List<ApiResponse> responses = mDataSource.loadMore();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.appendData(responses);
                mPageNo++;
                if (mPageNo <= mTotalPageCount) {
                    // 如果下一页的页脚小于等于5 则表示还有更多数据
                    mSwipeRecyclerView.loadMoreFinish(false, true);
                } else {
                    // 说明没有更多数据了
                    mSwipeRecyclerView.postDelayed(() -> {
                        mSwipeRecyclerView.loadMoreFinish(false, false);
                    }, 200);
                }
            }
        }, 1000);
    }


    private PullRefreshAdapter getAdapter() {
        return new PullRefreshAdapter(Collections.emptyList());
    }

    private class DataSource {

        List<ApiResponse> initLoadData() {
            List<ApiResponse> responses = new ArrayList<>();
            for (int index = 0; index < mPageSize; index++) {
                ApiResponse response = new ApiResponse();
                response.setData(String.format("我是第%s条数据", index));
                responses.add(response);
            }
            return responses;
        }

        List<ApiResponse> loadMore() {
            List<ApiResponse> responses = new ArrayList<>();
            for (int index = (mPageNo - 1) * mPageSize; index < (mPageNo) * mPageSize; index++) {
                ApiResponse response = new ApiResponse();
                response.setData(String.format("我是第%s条数据", index));
                responses.add(response);
            }
            return responses;
        }
    }


}
