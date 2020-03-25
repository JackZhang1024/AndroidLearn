package com.lucky.androidlearn.fragmentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lucky.androidlearn.R;

import me.yokeyword.fragmentation.SupportFragment;

public class FirstChildFragment extends SupportFragment {

    private static final String TAG = "FirstChildFragment";

    private TextView mTvMsg;

    public static FirstChildFragment newInstance() {
        Bundle args = new Bundle();
        FirstChildFragment fragment = new FirstChildFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_firstchild, container, false);
        initView(view);
        return view;
    }


    private void initView(View view){
        mTvMsg = view.findViewById(R.id.tv_msg);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mTvMsg.setText("ChildOne");
    }

}
