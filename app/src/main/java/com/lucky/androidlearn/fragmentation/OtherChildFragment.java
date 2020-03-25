package com.lucky.androidlearn.fragmentation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lucky.androidlearn.R;

import me.yokeyword.fragmentation.SupportFragment;

public class OtherChildFragment extends SupportFragment {
    private static final String TAG = "OtherChildFragment";

    private String mTitle;
    private TextView mTvMsg;

    public static final String TITLE ="title";

    public static OtherChildFragment newInstance(String title) {
        OtherChildFragment fragment = new OtherChildFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View childView =  inflater.inflate(R.layout.fragment_otherchild, container, false);
        initView(childView);
        return childView;
    }


    private void initView(View view) {
        mTitle = getArguments().getString(TITLE);
        mTvMsg = view.findViewById(R.id.tv_msg);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        Log.e(TAG, "onLazyInitView: "+mTitle);
        mTvMsg.setText("OtherFragment "+mTitle);
    }


}
