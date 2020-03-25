package com.lucky.androidlearn.fragmentation.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.fragmentation.BaseFragment;


// 首页Fragment
// 正常情况下 我们一般都是这么写的
// 将数据的请求写在onLazyInitView中
// 但是还有一种写法就是 在onLazyInitView中加载孙子Fragment
// 然后在孙子fragment中进行数据的请求
public class FragmentHome extends BaseFragment {

    private static final String TAG = "FragmentHome";

    private TextView mTvTitle;

    public static FragmentHome newInstance() {
        FragmentHome fragmentHome = new FragmentHome();
        Bundle bundle = new Bundle();
        fragmentHome.setArguments(bundle);
        return fragmentHome;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_multi_home, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mTvTitle = view.findViewById(R.id.tv_title);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        Log.e(TAG, "onLazyInitView: ");
    }


}
