package com.lucky.androidlearn.okhttplearn;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 1. okhttp http://blog.csdn.net/hello_1s/article/details/76641527
 * 2. https https://blog.csdn.net/lmj623565791/article/details/48129405
 * Created by zfz on 2018/2/4.
 */

public class OkHttpLearnActivity extends AppCompatActivity {

    private static final String TAG = "OkHttpLearnActivity";
    @BindView(R.id.iv_image_show)
    ImageView imageViewShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp_learn);
        ButterKnife.bind(this);


    }

    // https://kyfw.12306.cn/otn/
    // https://www.baidu.com/
    // https://urllib3.readth/
    @OnClick(R.id.btn_okhttp_get)
    public void onHttpGetClick() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://kyfw.12306.cn/otn/").build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                try {
                    if (responseBody != null) {
                        String body = responseBody.string();
                        Log.e(TAG, "onResponse: " + body);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Post请求
     * RequestBody的数据格式都要指定Content-Type, 常见的有三种
     * 1. application/x-www-form-urlencoded 数据是个普通表单
     * FormBody是继承自RequestBody, 用于发普通的键值对，FormBody
     * 已经指定了数据类型为 application/x-www-form-urlencoded
     * <p>
     * 2. application/json 数据格式是json格式的
     * <p>
     * 创建RequestBody时就不能用FormBody,要用RequestBody.Create()
     * <p>
     * MediaType mediaType = MediaType.parse("application/json");
     * RequestBody body = RequestBody.Create(mediaType, "要发送的json字符串");
     * <p>
     * 3. multipart/form-data 数据里有文件
     * <p>
     * 这个时候还有新的东西 MultipartBody
     * 如下面的例子中的 image/png 还可以换成其他的如
     * 一种是 multipart/form-data,   一种是text/plain;charset=utf-8等
     * RequetBody requestBody = new MultipartBody.Builder()
     * .setType(MultipartBody.FORM)
     * .addFormDataPart("file", file.getName(),
     * RequestBody.create(MediaType.parse("image/png"), file))
     * .build();
     * <p>
     * 如果一次上传多个文件，可重复利用addFormDataPart方法
     * <p>
     * RequetBody requestBody = new MultipartBody.Builder()
     * .setType(MultipartBody.FORM)
     * .addFormDataPart("file", file.getName(),
     * RequestBody.create(MediaType.parse("image/png"), file))
     * .addFormDataPart("file", file.getName(),
     * RequestBody.create(MediaType.parse("image/png"), file))
     * .build();
     */
    @OnClick(R.id.btn_okhttp_post)
    public void onHttpPostClick() {
        String url = "http://ip.taobao.com/service/getIpInfo.php";
        String ip = "123.139.19.151";
        OkHttpClient client = new OkHttpClient();
        // 请求参数 参数
        RequestBody requestBody = new FormBody.Builder()
                .add("ip", ip)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: "+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().toString();
                Log.e(TAG, "onResponse: " + result);
            }
        });
    }

    @OnClick(R.id.btn_okhttp_post_json)
    public void onHttpPostJsonClick() {
        OkHttpClient client = new OkHttpClient();
        // 请求参数 参数
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, "json字符串");
        Request request = new Request.Builder()
                .url("")
                .post(requestBody)
                .addHeader("", "")
                .addHeader("", "")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: POST-JSON " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }


    // 上传文件
    @OnClick(R.id.btn_okhttp_upload_file)
    public void onHttpUploadFileClick() {
        File file = new File("", "");
        OkHttpClient client = new OkHttpClient();
        // 请求参数 参数
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("phone", "128983923")
                .addFormDataPart("headImage", "imageName", RequestBody.create(MediaType.parse("image/png"), file))
                .build();
        Request request = new Request.Builder()
                .url("")
                .post(requestBody)
                .addHeader("", "")
                .addHeader("", "")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: POST-JSON " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }


    //btn_okhttp_download_files
    @OnClick(R.id.btn_okhttp_download_files)
    public void onDownloadFileClick() {
        String imageUrl = "http://dimg04.c-ctrip.com/images/vacations/images2/146/10272/10272_1_s43365_C_500_280.jpg";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(imageUrl).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                OkHttpLearnActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageViewShow.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }

    // btn_okhttp_cookie_manage
    @OnClick(R.id.btn_okhttp_cookie_manage)
    public void onCookieMangeClick() {
        final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
        String imageUrl = "http://dimg04.c-ctrip.com/images/vacations/images2/146/10272/10272_1_s43365_C_500_280.jpg";
        OkHttpClient client = new OkHttpClient.Builder().cookieJar(new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                Log.e(TAG, "saveFromResponse: cookieManage  " + url.host());
                for (Cookie cookie : cookies) {
                    Log.e(TAG, "saveFromResponse: CookieManage " + cookie.domain() + " name " + cookie.name());
                }
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies == null ? new ArrayList<>() : cookies;
            }
        }).build();
        Request request = new Request.Builder().url(imageUrl).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.e(TAG, "onResponse: cookie xxxx ");
            }
        });
    }


    /**
     * 1、在设置 HTTP 头时：
     * 使用 header(name, value) 方法来设置 HTTP 头的唯一值。对同一个 HTTP 头，多次调用该方法会覆盖之前设置的值。
     * 使用 addHeader(name, value) 方法来为 HTTP 头添加新的值。
     */
    @OnClick(R.id.btn_okhttp_add_header)
    public void onAddHeaderClick() {
        final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
        String imageUrl = "http://dimg04.c-ctrip.com/images/vacations/images2/146/10272/10272_1_s43365_C_500_280.jpg";
        CookieJar cookieJar = new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                Log.e(TAG, "saveFromResponse: cookieManage  " + url.host());
                for (Cookie cookie : cookies) {
                    Log.e(TAG, "saveFromResponse: CookieManage " + cookie.domain() + " name " + cookie.name());
                }
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies == null ? new ArrayList<>() : cookies;
            }
        };
        long cacheSize = 1024 * 1000 * 1024;
        OkHttpClient client = new OkHttpClient.Builder().cookieJar(cookieJar)
                .connectTimeout(1000, TimeUnit.MILLISECONDS)
                .readTimeout(500, TimeUnit.MILLISECONDS)
                .writeTimeout(500, TimeUnit.MILLISECONDS)
                .cache(new Cache(getExternalCacheDir().getAbsoluteFile(), cacheSize))
                .build();
        Request request = new Request.Builder().url(imageUrl).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {

            }
        });
    }

    @OnClick(R.id.btn_okhttp_https)
    public void onHttpsClick() {
        Intent intent = new Intent(this, OkHttpsLearnActivity.class);
        startActivity(intent);
    }
}
