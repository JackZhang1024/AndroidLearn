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

public class FragmentMine extends BaseFragment {

    private static final String TAG = "FragmentMine";

    public static FragmentMine newInstance() {
        FragmentMine fragmentMine = new FragmentMine();
        Bundle bundle = new Bundle();
        fragmentMine.setArguments(bundle);
        return fragmentMine;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_multi_mine, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        Log.e(TAG, "onLazyInitView: ");
    }



}
