package com.lucky.androidlearn.retrofit;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.GsonBuilder;
import com.lucky.androidlearn.R;
import com.lucky.androidlearn.mvp.RetrofitService;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// 上传文件
public class RetrofitUploadActivity extends AppCompatActivity {

    private static final String TAG = "RetrofitUploadActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_upload);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_upload_interceptor)
    public void onUploadFileClick() {
        RetrofitCallBack<HttpResult<OSSFile>> callBack = new RetrofitCallBack<HttpResult<OSSFile>>() {
            @Override
            void onSuccess(Call<HttpResult<OSSFile>> call, Response<HttpResult<OSSFile>> response) {
                Log.e(TAG, "onSuccess: " + " url "+response.body().data.data.ossUrl);
            }

            @Override
            public void onFailure(Call<HttpResult<OSSFile>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + Thread.currentThread().getName() + " error " + t.getMessage());
            }

            @Override
            public void onUploading(long totalLength, long currentLength) {
                super.onUploading(totalLength, currentLength);
                Log.e(TAG, "onUploading: " + (100 * currentLength / totalLength));
            }
        };
        File file = new File(getFilesDir(), "hello.mp4");
        MultipartBody uploadBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .build();
        getRetrofitService().uploadFile(new FileRequestBody<>(uploadBody, callBack)).enqueue(callBack);

//        RequestBody body1 = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
//        //通过该行代码将RequestBody转换成特定的FileRequestBody
//        FileRequestBody body = new FileRequestBody(body1, callBack);
//        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), body);
//        getRetrofitService().uploadFileNew(part).enqueue(callBack);

    }

    public static class HttpResult<T> {
        public String status;
        private String message;
        private HttpResultInner<T> data;
    }

    public static class HttpResultInner<T> {
        public T data;
    }

    // 上传返回结果
    public static class OSSFile {
        public String ossUrl;

        public void setOssUrl(String ossUrl) {
            this.ossUrl = ossUrl;
        }

        public String getOssUrl() {
            return ossUrl;
        }
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

        // 回调上传进度
        public void onUploading(long totalLength, long currentLength) {

        }
    }

    final static class FileRequestBody<T> extends RequestBody {

        private RequestBody mRequestBody;
        private RetrofitCallBack<T> mCallBack;
        private BufferedSink mBufferedSink;

        public FileRequestBody(RequestBody requestBody, RetrofitCallBack<T> callBack) {
            super();
            this.mRequestBody = requestBody;
            this.mCallBack = callBack;
        }

        @Override
        public MediaType contentType() {
            return mRequestBody.contentType();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            if (mBufferedSink == null) {
                mBufferedSink = Okio.buffer(sink(sink));
            }
            mRequestBody.writeTo(mBufferedSink);
            // 必须flush 否则最后一部分数据写不进去
            mBufferedSink.flush();
        }

        @Override
        public long contentLength() throws IOException {
            return mRequestBody.contentLength();
        }

        private Sink sink(Sink sink) {
            return new ForwardingSink(sink) {
                //当前写入字节数
                long bytesWritten = 0L;
                //总字节长度，避免多次调用contentLength()方法
                long contentLength = 0L;

                @Override
                public void write(@NotNull Buffer source, long byteCount) throws IOException {
                    super.write(source, byteCount);
                    if (contentLength == 0) {
                        contentLength = contentLength();
                    }
                    bytesWritten += byteCount;
                    mCallBack.onUploading(contentLength, bytesWritten);
                }
            };
        }
    }

    private <T> RetrofitService getRetrofitService() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("http://122.51.191.103:8080/serverdemo/")
                .baseUrl("http://192.168.104.9:8080/serverdemo/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()))
                .client(builder.build())
                .build();
        return retrofit.create(RetrofitService.class);
    }

}
