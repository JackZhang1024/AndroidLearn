package com.lucky.androidlearn.okhttplearn;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jingewenku.abrahamcaijin.commonutil.AppLogMessageMgr;
import com.lucky.androidlearn.R;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpCacheActivity extends AppCompatActivity {
    private static final String TAG = "OkHttpCacheActivity";
    private OkHttpClient mOkHttpClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp_cache);
        ButterKnife.bind(this);
        initOkHttp();
    }

    // 如果不想要缓存 在可以在Request请求创建的时候添加header 然后在interceptor里获取header对请求进行强制网络请求
    // 有网的时候缓存
    final Interceptor NetCacheInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            int onlineCacheTime = 30;//在线的时候的缓存过期时间，如果想要不缓存，直接时间设置为0 30秒内 不会重复请求同一个接口 不会请求网络 出了30秒，则会请求网络
            return response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + onlineCacheTime)
                    .removeHeader("Pragma")
                    .build();
        }
    };

    // 无网的时候缓存
    final Interceptor OfflineCacheInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!isNetworkConnected(OkHttpCacheActivity.this)) {
                int offlineCacheTime = 600;//离线的时候的缓存的过期时间  没有网络的情况下，600秒内缓存是有效的，出了600秒，就会出现504错误
                request = request.newBuilder()
//                        .cacheControl(new CacheControl
//                                .Builder()
//                                .maxStale(60,TimeUnit.SECONDS)
//                                .onlyIfCached()
//                                .build()
//                        ) 两种方式结果是一样的，写法不同
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + offlineCacheTime)
                        .build();
            }
            return chain.proceed(request);
        }
    };

    private void initOkHttp() {
        File httpCacheDirectory = new File(getCacheDir(), "okhttpCache");
        int cacheSize = 50 * 1024 * 1024; // 50M
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .cache(new Cache(httpCacheDirectory, cacheSize))
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(OfflineCacheInterceptor) // 添加App缓存拦截器
                .addNetworkInterceptor(NetCacheInterceptor) // 添加网络缓存拦截器
                .build();
    }

    @OnClick(R.id.btn_fetch_cache)
    public void onFetchNetDataClick() {
        Request request = new Request.Builder().url("http://ip-api.com/json/?lang=zh-CN").build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                if (body != null) {
                    String result = body.string();
                    Log.e(TAG, "onResponse: result " + result);
                }
            }
        });
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            //获取连接对象
            if (mNetworkInfo != null) {
                //判断是TYPE_MOBILE网络
                if (ConnectivityManager.TYPE_MOBILE == mNetworkInfo.getType()) {
                    AppLogMessageMgr.i("AppNetworkMgr", "网络连接类型为：TYPE_MOBILE");
                    //判断移动网络连接状态
                    NetworkInfo.State STATE_MOBILE = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
                    if (STATE_MOBILE == NetworkInfo.State.CONNECTED) {
                        AppLogMessageMgr.i("AppNetworkMgrd", "网络连接类型为：TYPE_MOBILE, 网络连接状态CONNECTED成功！");
                        return mNetworkInfo.isAvailable();
                    }
                }
                //判断是TYPE_WIFI网络
                if (ConnectivityManager.TYPE_WIFI == mNetworkInfo.getType()) {
                    AppLogMessageMgr.i("AppNetworkMgr", "网络连接类型为：TYPE_WIFI");
                    //判断WIFI网络状态
                    NetworkInfo.State STATE_WIFI = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
                    if (STATE_WIFI == NetworkInfo.State.CONNECTED) {
                        AppLogMessageMgr.i("AppNetworkMgr", "网络连接类型为：TYPE_WIFI, 网络连接状态CONNECTED成功！");
                        return mNetworkInfo.isAvailable();
                    }
                }
            }
        }
        return false;
    }

}
