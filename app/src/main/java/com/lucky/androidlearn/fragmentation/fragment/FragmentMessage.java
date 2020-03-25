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

public class FragmentMessage extends BaseFragment {

    private static final String TAG = "FragmentMessage";

    public static FragmentMessage newInstance(){
        FragmentMessage fragmentMessage = new FragmentMessage();
        Bundle bundle = new Bundle();
        fragmentMessage.setArguments(bundle);
        return fragmentMessage;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_multi_message, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        Log.e(TAG, "onLazyInitView: ");
        loadRootFragment(R.id.fl_message_container, InnerMessageFragment.newInstance());
    }


}
