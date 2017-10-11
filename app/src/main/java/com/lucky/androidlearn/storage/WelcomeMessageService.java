package com.lucky.androidlearn.storage;

import com.lucky.androidlearn.domain.model.TaobaoBean;
import com.lucky.androidlearn.domain.model.WeatherInfos;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by zfz on 2017/1/2.
 */


public interface WelcomeMessageService {

    @GET("http://ip.taobao.com/service/getIpInfo.php")
    Call<TaobaoBean> getWelcomeMessage(@Query("ip") String ipInfo);

    //http://www.apifree.net/weather/101010100.xml
    @GET("http://www.apifree.net/weather/{address}.xml")
    Call<WeatherInfos> getWeatherInfo(@Path("address") String address);


    //http://www.apifree.net/weather/101010100.xml
    @GET("http://www.apifree.net/weather/{address}.xml")
    Call<Response> getWeatherInfoBody(@Path("address") String address);

}
