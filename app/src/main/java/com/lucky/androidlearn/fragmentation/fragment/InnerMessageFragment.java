package com.lucky.androidlearn.fragmentation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.fragmentation.BaseFragment;

import me.yokeyword.fragmentation.SupportFragment;


// 在Message中fragment
// 这样的好处就是在我们需要的时候（就是Fragment可见的）才会添加并初始化我们需要的视图
// 而不是一上来就加载视图 如果某些视图比较复杂那么就以上来就干了很多内存 而有时候我们甚至不打开该Fragment 就白白浪费了这些内存
// 所以这样做的好处就是 减少内存 提高加载的速度
// 有些需要在这个Fragment中填充多个Fragment
public class InnerMessageFragment extends BaseFragment {

    private static final String TAG = "InnerMessageFragment";

    public static InnerMessageFragment newInstance() {
        InnerMessageFragment fragment = new InnerMessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inner_mssage, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

    }


}
