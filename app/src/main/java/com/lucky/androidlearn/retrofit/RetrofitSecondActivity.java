package com.lucky.androidlearn.retrofit;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.mvp.RetrofitService;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSecondActivity extends AppCompatActivity {

    private static final String TAG = "RetrofitSecondActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_second);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_interceptor_download)
    public void onInterceptorDownloadClick() {
        RetrofitCallBack<ResponseBody> callBack = new RetrofitCallBack<ResponseBody>() {
            @Override
            public void onLoading(long totalLength, long currentLength) {
                super.onLoading(totalLength, currentLength);
                int progress = (int) (100 * currentLength / totalLength);
                Log.e(TAG, "onLoading: progress " + progress);
            }

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
            }

            @Override
            void onSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "onSuccess: ");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: ");
            }
        };
        String url = "http://luckyboy.oss-cn-beijing.aliyuncs.com/oss_file/1594377746736.mp4";
        getRetrofitService(callBack).downloadFileInterceptor(url).enqueue(callBack);
    }

    abstract static class RetrofitCallBack<T> implements Callback<T> {

        @Override
        public void onResponse(@NotNull Call<T> call, Response<T> response) {
            if (response.isSuccessful()) {
                onSuccess(call, response);
            } else {
                onFailure(call, new Throwable(response.message()));
            }
        }

        abstract void onSuccess(Call<T> call, Response<T> response);

        public void onLoading(long totalLength, long currentLength) {

        }
    }

    // 扩展ResponseBody 用于处理下载显示进度
    static final class FileResponseBody<T> extends ResponseBody {

        private ResponseBody mResponseBody;
        private RetrofitCallBack<T> mCallBack;

        public FileResponseBody(ResponseBody responseBody, RetrofitCallBack<T> retrofitCallBack) {
            super();
            mResponseBody = responseBody;
            mCallBack = retrofitCallBack;
        }

        @Override
        public MediaType contentType() {
            return mResponseBody.contentType();
        }

        @Override
        public long contentLength() {
            return mResponseBody.contentLength();
        }

        private BufferedSource mBufferedSource;

        @Override
        public BufferedSource source() {
            if (mBufferedSource == null) {
                mBufferedSource = Okio.buffer(source(mResponseBody.source()));
            }
            return mBufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0;

                @Override
                public long read(@NotNull Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    mCallBack.onLoading(mResponseBody.contentLength(), totalBytesRead);
                    return bytesRead;
                }
            };
        }
    }

    private <T> RetrofitService getRetrofitService(RetrofitCallBack<T> callBack) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                okhttp3.Response response = chain.proceed(chain.request());
                // 将ResponseBody转化成我们需要的FileResponseBody
                return response.newBuilder().body(new FileResponseBody<T>(response.body(), callBack)).build();
            }
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.baidu.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();
        return retrofit.create(RetrofitService.class);
    }


}
