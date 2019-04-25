package com.lucky.androidlearn.hybrid;

import android.util.Log;
import android.webkit.JavascriptInterface;

/* 取消或者收藏功能*/
public class FavorModel {
    private static final String TAG = "FavorModel";

    private String mNewsId;
    // "0" 没有收藏 "1" 已收藏
    private String mIsFavored;

    public FavorModel(String newsId, String mIsFavored) {
        this.mNewsId = newsId;
        this.mIsFavored = mIsFavored;
    }

    @JavascriptInterface
    public void cancelFavor(String newsId) {
        Log.e(TAG, "cancelFavor: "+newsId);
    }

    @JavascriptInterface
    public void doFavor(String newsId) {
        Log.e(TAG, "doFavor: "+newsId);
    }

    @JavascriptInterface
    public String getNewsId() {
        return mNewsId;
    }

    @JavascriptInterface
    public String getIsFavored() {
        return mIsFavored;
    }

    @JavascriptInterface
    public void setIsFavored(String mIsFavored) {
        this.mIsFavored = mIsFavored;
        Log.e(TAG, "setIsFavored: "+mIsFavored);
    }
}
