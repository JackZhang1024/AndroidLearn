package com.lucky.androidlearn.mvc.model;

import android.util.Log;

import com.google.gson.Gson;
import com.lucky.androidlearn.mvc.model.bean.WeatherModelBean;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 注解说明：
 * <p>
 * 标记：
 * <p>
 * 1、FormUrlEncoded 注解：表示请求体是一个Form表单；
 * <p>
 * 2、Multipart 注解：表示请求体是一个支持文件上传的Form表单；
 * <p>
 * 3、Streaming 注解：表示响应体的数据用流的形式返回，如果，没有用该注解，默认会吧数据存在内存，之后通过流读取数据也是读内存中的数据，如果数据比较大，建议使用该注解。
 * <p>
 * 参数：
 * <p>
 * 1、Headers：用于添加请求头
 * <p>
 * 2、Header：用于添加不固定值的Header
 * <p>
 * 3、Body：用于非表单请求体
 * <p>
 * 4、Field、FieldMap、Part、PartMap：用于表单字段
 * <p>
 * Field、FieldMap与FormUrlEncoded配合使用；
 * Part、PartMap与Multipart配合使用，适合有文件上传的情况;
 * <p>
 * FieldMap：接受的类型为Map<String,String>;
 * PartMap默认接受的类型为Map<String,RequestBody>.
 * <p>
 * 5、Path、Query、QueryMap、Url：用于URL
 * <p>
 * Query、QueryMap中的数据体现在URL上，Field、FieldMap的数据是请求体，但是两者生成的数据形式是一样的。
 *
 * @Get("")中一定要有参数，否则会报错 Created by zfz on 2018/2/4.
 */


public class WeatherModelImpl implements WeatherModel {

    private static final String TAG = "WeatherModelImpl";
    private OnRequestCallBack mOnRequestCallBack;

    @Override
    public void getWeather(String cityID, OnRequestCallBack callBack) {
        mOnRequestCallBack = callBack;
        // getWeatherMessage();
        getWeatherInfo();
    }

    @Override
    public void postWeather(String cityID, OnRequestCallBack callBack) {

    }

    // 异步请求操作
    private void getWeatherInfo() {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(BaseModel.BASE_URL);
        builder.addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        WeatherModelService service = retrofit.create(WeatherModelService.class);
        Map<String, String> params = new HashMap<>();
        params.put("city", "CHSH000000");
        Call<WeatherModelBean> call = service.getWeatherDetailInfo(params);
        call.enqueue(new Callback<WeatherModelBean>() {
            @Override
            public void onResponse(Call<WeatherModelBean> call, Response<WeatherModelBean> response) {
                int resultCode = response.code();
                WeatherModelBean bean = response.body();
            }

            @Override
            public void onFailure(Call<WeatherModelBean> call, Throwable t) {

            }
        });
    }

    private void getWeatherDetail() {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(BaseModel.BASE_URL);
        builder.addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        WeatherModelService service = retrofit.create(WeatherModelService.class);
        Call<WeatherModelBean> call = service.getWeatherInfo("CHSH000000", "zh-chs", "c",
                "city", 1, "78928e706123c1a8f1766f062bc8676b");
        call.enqueue(new Callback<WeatherModelBean>() {
            @Override
            public void onResponse(Call<WeatherModelBean> call, Response<WeatherModelBean> response) {
                int resultCode = response.code();
                WeatherModelBean bean = response.body();
                Log.e(TAG, "onResponse: " + bean.getStatus());
            }

            @Override
            public void onFailure(Call<WeatherModelBean> call, Throwable t) {

            }
        });

    }

    private void getWeatherMessage() {
        try {
            Retrofit.Builder builder = new Retrofit.Builder();
            builder.baseUrl(BaseModel.BASE_URL);
            builder.addConverterFactory(ScalarsConverterFactory.create());
            Retrofit retrofit = builder.build();
            WeatherModelService service = retrofit.create(WeatherModelService.class);
            Map<String, String> params = new HashMap<>();
            params.put("city", "CHSH000000");
            Call<String> call = service.getWeatherDetailMessage(params);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    int resultCode = response.code();
                    Log.e(TAG, "onResponse: Message " + response.body());
                    mOnRequestCallBack.onRequestSuccess(response.body());
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getWeatherMessageResponse() {
        try {
            Retrofit.Builder builder = new Retrofit.Builder();
            builder.baseUrl(BaseModel.BASE_URL);
            Retrofit retrofit = builder.build();
            WeatherModelService service = retrofit.create(WeatherModelService.class);
            Map<String, String> params = new HashMap<>();
            params.put("city", "CHSH000000");
            Call<ResponseBody> call = service.getWeatherDetailMessageResponse(params);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    int resultCode = response.code();
                    try {
                        Log.e(TAG, "onResponse: MessageResponse " + response.body().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * post请求，参数是json类型的时候
     */
    private void retrofitPostJson() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.xxx.com/api/rest/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        WeatherModelService weatherModelService = retrofit.create(WeatherModelService.class);
        Map<String, String> params = new HashMap<>();
        params.put("product_id", "65");
        params.put("quantity", "2");
        params.put("option", "");
        params.put("recurring_id", "0");
        //使用Gson将map转为json字符串
        String json = new Gson().toJson(params);
        Log.e("Post上传参数 ", "json" + json);
        //使用okHttp中的MediaType创建RequestBody
        Call<String> call = weatherModelService.postBody(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("Post请求返回 ", response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    /**
     * post请求使用@FieldMap,参数是Map类型的
     */
    private void retrofitPostFieldMap() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("http://app.xxx.com/v1.0.0/")
                .build();
        Map<String, String> map = new HashMap<>();
        map.put("nickName", "单线程");
        map.put("reResourceUrl", "");
        map.put("reResourceId", "");
        retrofit.create(WeatherModelService.class).postFieldMap("169", map).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("------------>", "response" + response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("------------>", "t" + t);
            }
        });
    }


}
