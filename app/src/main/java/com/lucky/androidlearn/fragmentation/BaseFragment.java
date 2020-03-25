package com.lucky.androidlearn.fragmentation;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lucky.androidlearn.fragmentation.fragment.FragmentHome;

import me.yokeyword.fragmentation.SupportFragment;


public abstract class BaseFragment extends SupportFragment {

    protected OnBackToFirstListener mBackToFirstListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnBackToFirstListener) {
            mBackToFirstListener = (OnBackToFirstListener) context;
        } else {
            throw new RuntimeException("must implement OnBackToFirstListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mBackToFirstListener = null;
    }

    @Override
    public boolean onBackPressedSupport() {
        if (getChildFragmentManager().getBackStackEntryCount() > 1) {
            popChild();
        } else {
            if (this instanceof FragmentHome) {
                // 如果是第一个Activity 则退出当前页面Activity
                _mActivity.finish();
            } else {
                mBackToFirstListener.onBackToFirstFragment();
            }
        }
        return true;
    }

    public interface OnBackToFirstListener {

        void onBackToFirstFragment();
    }


}
