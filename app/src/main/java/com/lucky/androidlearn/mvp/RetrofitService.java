package com.lucky.androidlearn.mvp;

import com.lucky.androidlearn.mvc.model.bean.WeatherModelBean;
import com.lucky.androidlearn.retrofit.RetrofitUploadActivity;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by zfz on 2018/2/4.
 */

public interface RetrofitService {

    String BASE_URL = "http://tj.nineton.cn/";

    @GET("Heart/index/all")
    Observable<WeatherModelBean> getWeatherDetailInfo(@QueryMap Map<String, String> params);


    @GET("http://ip-api.com/json/?lang=zh-CN")
    Observable<IPInfo> getIPInfo();

    // 下载文件
    @GET
    @Streaming
    Call<ResponseBody> downloadFile(@Url String url);

    @GET
    Call<ResponseBody> downloadFileInterceptor(@Url String url);


    // 上传文件
    @POST("fileUploadOSS")
    Call<RetrofitUploadActivity.HttpResult<RetrofitUploadActivity.OSSFile>> uploadFile(@Body RequestBody requestBody);

    @Multipart
    @POST("fileUploadOSS")
    Call<RetrofitUploadActivity.HttpResult<RetrofitUploadActivity.OSSFile>> uploadFileNew(@Part MultipartBody.Part file);

}
