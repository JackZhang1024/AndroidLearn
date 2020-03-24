package com.lucky.androidlearn.loadmore;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.yanzhenjie.recyclerview.SwipeRecyclerView;

public class RecyclerViewUtils {

    public DefineLoadMoreView initRecyclerView(Context context, SwipeRecyclerView recyclerView,
                                 SwipeRecyclerView.LoadMoreListener listener){

        DefineLoadMoreView footerView = new DefineLoadMoreView(context);
        recyclerView.addFooterView(footerView);
        recyclerView.setLoadMoreView(footerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        // 加载更多回调
        recyclerView.setLoadMoreListener(listener);
        footerView.setmLoadMoreListener(new SwipeRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                // 设置尾部点击回调
                footerView.onLoading();
                listener.onLoadMore();
            }
        });
        return footerView;
    }

}
