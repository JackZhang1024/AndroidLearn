package com.lucky.androidlearn.widget.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.loadmore.PullRefreshLoadMoreActivity;
import com.lucky.androidlearn.loadmore.PullRefreshLoadMoreNewActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfz on 2017/12/18.
 */

public class RecyclerViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_google_refresh)
    public void onGoogleRefreshClick(View view) {
        startActivity(new Intent(this, GoogleSwipeRefreshRVActivity.class));
    }

    @OnClick(R.id.btn_smart_refresh)
    public void onSmartRefreshClick(View view) {
        startActivity(new Intent(this, SmartRefreshActivity.class));
    }

    @OnClick(R.id.btn_expand_recycleview)
    public void onExpandListViewClick(View view) {
        startActivity(new Intent(this, ExpandRecycleViewActivity.class));
    }

    @OnClick(R.id.btn_yoga_expand_recycleview)
    public void onYogaExpandListViewClick(View view) {
        startActivity(new Intent(this, ExpandRecycleViewNodeActivity.class));
    }

    @OnClick(R.id.btn_load_more)
    public void onLoadMoreClick() {
        startActivity(new Intent(this, PullRefreshLoadMoreActivity.class));
    }

    @OnClick(R.id.btn_load_more_new)
    public void onLoadMoreNewClick() {
        startActivity(new Intent(this, PullRefreshLoadMoreNewActivity.class));
    }




}
