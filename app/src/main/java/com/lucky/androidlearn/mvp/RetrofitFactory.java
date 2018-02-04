package com.lucky.androidlearn.mvp;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zfz on 2018/2/4.
 */

public class RetrofitFactory {
    private static final String TAG = "RetrofitFactory";
    // 访问超时
    private static final int TIME_OUT = 10*1000;

    // Retrofit是基于OkHttpClient的, 可以创建一个OkHttpClient进行一些配置
    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            // 打印接口信息 方便调试接口
            .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.e(TAG, "log: "+message);
                }
            }).setLevel(HttpLoggingInterceptor.Level.BASIC))
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .build();


    private static RetrofitService retrofitService = new Retrofit.Builder()
            .baseUrl(RetrofitService.BASE_URL)
            // 添加Gson转换器
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                    .setLenient()
                    .create()))
            // 添加Retrofi到RxJava的转换器
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient)
            .build()
            .create(RetrofitService.class);



    public static RetrofitService getInstance(){
        return retrofitService;
    }
}














