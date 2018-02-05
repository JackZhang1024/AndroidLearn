package com.lucky.androidlearn.dagger2learn.lesson04;

import android.content.Context;
import android.util.Log;

public class SharePreferenceManager {
    private static final String TAG = "PreferenceManager";
    private Context mContext;

    public SharePreferenceManager(Context context) {
        this.mContext = context;
    }

    public void commit(){
        Log.e(TAG, "commit: hello world");
    }


}
