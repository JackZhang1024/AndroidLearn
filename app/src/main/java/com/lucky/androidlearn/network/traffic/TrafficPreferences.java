package com.lucky.androidlearn.network.traffic;

import android.content.Context;
import android.content.SharedPreferences;

public class TrafficPreferences {

    // 应用下载流量
    public static final String TRAFFIC_DOWN = "traffic_down";

    // 应用上传流量
    public static final String TRAFFIC_UP = "traffic_up";

    // 应用流量记录时间
    public static final String TRAFFIC_RECORD = "traffic_record";

    private static final String FILE_NAME = "network_traffic";

    private Context mContext;

    public TrafficPreferences(Context context) {
        this.mContext = context;
    }

    public void save(String key, String value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void remove(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }


}
