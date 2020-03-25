package com.lucky.androidlearn.fragmentation.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.fragmentation.BaseFragment;

public class FragmentCircle extends BaseFragment {

    private static final String TAG = "FragmentCircle";

    public static FragmentCircle newInstance(){
        FragmentCircle fragmentCircle = new FragmentCircle();
        Bundle bundle = new Bundle();
        fragmentCircle.setArguments(bundle);
        return fragmentCircle;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_multi_circle, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        Log.e(TAG, "onLazyInitView: ");
    }
}
