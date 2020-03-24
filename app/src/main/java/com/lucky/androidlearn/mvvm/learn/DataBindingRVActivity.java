package com.lucky.androidlearn.mvvm.learn;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jingewenku.abrahamcaijin.commonutil.AppToastMgr;
import com.lucky.androidlearn.R;
import com.lucky.androidlearn.databinding.ActivityDatabindingRecycleviewBinding;

/**
 * Created by zfz on 2018/2/6.
 */

public class DataBindingRVActivity extends AppCompatActivity {
    ActivityDatabindingRecycleviewBinding activityDatabingInclueBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        activityDatabingInclueBinding = DataBindingUtil.setContentView(this, R.layout.activity_databinding_recycleview);
        Content con = new Content("Title", "SubTitle");
        activityDatabingInclueBinding.setContent(con);
        //这个测试没有效果，不会显示toolbar的title/subTitle
        activityDatabingInclueBinding.toolbar.setContent(con);
        activityDatabingInclueBinding.toolbar.toolbar.setTitle("Title");
        activityDatabingInclueBinding.toolbar.toolbar.setSubtitle("SubTitle");
        //下面的代码也可以通过DataBinding绑定数据
        activityDatabingInclueBinding.toolbar.toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        setSupportActionBar(activityDatabingInclueBinding.toolbar.toolbar);
        activityDatabingInclueBinding.toolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        activityDatabingInclueBinding.recycler.setLayoutManager(layoutManager);
        activityDatabingInclueBinding.recycler.addItemDecoration(dividerItemDecoration);
        String[] data = new String[]{"双节棍", "东风破", "夜曲", "甜甜的", "告白气球"};
        DBRecycleAdapter adapter = new DBRecycleAdapter(data);
        activityDatabingInclueBinding.recycler.setAdapter(adapter);
        adapter.setOnItemClickListener((position, item)->{
            AppToastMgr.show(DataBindingRVActivity.this, "position "+position+" item "+item);
        });
    }
}
