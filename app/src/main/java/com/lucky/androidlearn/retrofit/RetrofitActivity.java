package com.lucky.androidlearn.retrofit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;
import com.lucky.androidlearn.mvp.RetrofitFactory;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitActivity extends AppCompatActivity {

    private static final String TAG = "RetrofitActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_download);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_download_large_interceptor)
    public void onInterceptorDownloadClick() {
        Intent intent = new Intent(this, RetrofitSecondActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_upload_large_interceptor)
    public void onUploadFileClick(){
        Intent intent = new Intent(this, RetrofitUploadActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_download_large_file)
    public void onDownLoadLargeFileClick() {
        String url = "http://luckyboy.oss-cn-beijing.aliyuncs.com/oss_file/1594377746736.mp4";
        RetrofitFactory.getInstance().downloadFile(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: " + Thread.currentThread().getName());
                downloadFile(new File(getFilesDir(), "hello.mp4"), response, new DownloadListener() {
                    @Override
                    public void onDownloadFail() {

                    }

                    @Override
                    public void onDownloadSuccess() {

                    }

                    @Override
                    public void onProgressChanged(int progress) {
                        Log.e(TAG, "onProgressChanged: " + progress);
                    }
                });
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

            }
        });
    }

    private void downloadFile(File file, Response<ResponseBody> response, DownloadListener listener) {
        // 1. 创建文件
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.onDownloadFail();
            }
        }
        InputStream inputStream = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            inputStream = response.body().byteStream();
            long contentLength = response.body().contentLength();
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len = 0;
            long currentLength = 0;
            while ((len = inputStream.read(buffer, 0, bufferSize)) != -1) {
                bos.write(buffer, 0, len);
                currentLength += len;
                // 计算当下进度
                if (listener != null) {
                    listener.onProgressChanged((int) (100 * currentLength / contentLength));
                }
            }
            if (listener != null) {
                listener.onDownloadSuccess();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.closeQuietly(inputStream);
            CloseUtil.closeQuietly(fos);
            CloseUtil.closeQuietly(bos);
        }
    }

    interface DownloadListener {

        void onDownloadSuccess();

        void onProgressChanged(int progress);

        void onDownloadFail();

    }

    static class CloseUtil {
        public static void closeQuietly(Closeable closeable) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
