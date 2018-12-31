package com.lucky.androidlearn.widget.common;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lucky.androidlearn.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zfz on 2017/12/18.
 */

public class GoogleSwipeRefreshRVActivity extends AppCompatActivity {
    private static final String TAG = "RecycleViewActivity";
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    private List<String> dataList = new ArrayList<>();
    private MyRecycleViewAdapter myRecycleViewAdapter;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            myRecycleViewAdapter.notifyDataChange(dataList);
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiperv);
        ButterKnife.bind(this);
        initView();
        addListeners();
    }

    private void addListeners() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doSomething();
            }
        });
    }

    private void doSomething() {
        new MyTask(mHandler);
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        swipeRefreshLayout.setColorSchemeResources(R.color.blue);
        //initData();
        myRecycleViewAdapter = new MyRecycleViewAdapter(dataList);
        recyclerView.setAdapter(myRecycleViewAdapter);
    }

    private void initData() {
        for (int index = 0; index < 50; index++) {
            dataList.add(String.format("第%s数据 ", index));
        }
    }

    class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.MyViewHolder> {
        private List<String> myDataList = null;

        public MyRecycleViewAdapter(List<String> dataList) {
            this.myDataList = dataList;
        }

        public void notifyDataChange(List<String> dataList) {
            this.myDataList = dataList;
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.setItemContent(myDataList.get(position));
        }

        @Override
        public int getItemCount() {
            return myDataList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_msg)
            TextView tvMessage;

            public MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public void setItemContent(String data) {
                tvMessage.setText(data);
            }
        }
    }

    class MyTask implements Runnable {
        private Handler mHandler;

        public MyTask(Handler handler) {
            new Thread(this).start();
            this.mHandler = handler;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(3 * 1000);
                dataList.add("下拉刷新");
                dataList.add("下拉刷新");
                dataList.add("下拉刷新");
                dataList.add("下拉刷新");
                dataList.add("下拉刷新");
                mHandler.sendEmptyMessage(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
