package com.lucky.androidlearn.mvc.model;

import com.lucky.androidlearn.domain.model.TaobaoBean;
import com.lucky.androidlearn.mvc.model.bean.WeatherModelBean;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Retrofit使用教程
 * http://square.github.io/retrofit/
 * Created by zfz on 2018/2/4.
 */


public interface WeatherModelService {

    @GET("http://tj.nineton.cn/Heart/index/all")
    Call<WeatherModelBean> getWeatherInfo(@Query("city") String city, @Query("language") String language, @Query("unit") String unit,
                                          @Query("aqi") String aqi, @Query("alarm") int alarm, @Query("key") String key
    );

    @GET("Heart/index/all")
    Call<WeatherModelBean> getWeatherDetailInfo(@QueryMap Map<String, String> params);

    @GET("Heart/index/all")
    Call<String> getWeatherDetailMessage(@QueryMap Map<String, String> params);

    @GET("Heart/index/all")
    Call<ResponseBody> getWeatherDetailMessageResponse(@QueryMap Map<String, String> params);

    // http://www.xxx.com/api/rest/products?id= 65
    @GET("http://www.xxx.com/api/rest/products")
    Call<ResponseBody> getProductDetail(@Query("id") int id);


    // http://www.xxx.com/api/rest/products/{product_id}//将product_id替换为65
    @GET("http://www.xxx.com/api/rest/products/{product_id}")
    Call<ResponseBody> getProductDetailByPath(@Path("product_id") int product_id);

    /**
     * 非表单的参数使用@Body
     * 请求头我们可以添加多个
     * post请求参数类型是json
     *
     * @param requestBody
     * @return
     */
    @POST("cart?from=cart")
    @Headers({"X-Oc-Merchant-Id:1qaz2wsx", "Content-Type:application/json"})
    Call<String> postBody(@Body RequestBody requestBody);

    /**
     * Post请求，参数为Map及使用@FieldMap
     * 及post的请求参数是map类型
     * post请求FormUrlEncoded和FieldMap配合使用
     *
     * @param id
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("healthplan/plans/{id}")
    Call<String> postFieldMap(@Path("id") String id, @FieldMap Map<String, String> params);



}
