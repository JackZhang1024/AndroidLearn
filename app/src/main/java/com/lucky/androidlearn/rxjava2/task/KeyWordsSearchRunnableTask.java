package com.lucky.androidlearn.rxjava2.task;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class KeyWordsSearchRunnableTask implements Runnable {
    private static final String TAG = "KeyWordsSearchRunnable";

    private String mKeyWords;
    private String[] mKeyWordList = new String[]{"A1", "AB", "ABB", "AA", "AC", "A2", "AB2", "ABB2", "AA2", "AC2"};

    private SearchCallBack mSearchCallBack;
    private long mCreateTime;
    private boolean mAbortDisplay;

    public KeyWordsSearchRunnableTask(String keyWords, SearchCallBack searchCallBack) {
        this.mKeyWords = keyWords;
        this.mSearchCallBack = searchCallBack;
        mCreateTime = System.currentTimeMillis();
        mAbortDisplay = false;
    }

    @Override
    public void run() {
        try {
            Log.e(TAG, "call: " + Thread.currentThread().getName());
            Thread.sleep(3000);
            List<String> results = new ArrayList<>();
            for (String str : mKeyWordList) {
                if (str.contains(mKeyWords)) {
                    results.add(str);
                }
            }
            mSearchCallBack.onCallBack(results, mAbortDisplay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getCreateTime() {
        return mCreateTime;
    }

    public void setAbortDisplay(boolean mAbortDisplay) {
        this.mAbortDisplay = mAbortDisplay;
    }
}
