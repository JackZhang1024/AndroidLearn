package com.lucky.androidlearn.storage;

import com.lucky.androidlearn.domain.model.TaobaoBean;
import com.lucky.androidlearn.domain.model.WeatherInfos;
import com.lucky.androidlearn.domain.repository.MessageRepository;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import timber.log.Timber;

/**
 * Created by zfz on 2017/1/1.
 */

public class WelcomeMessageRepository implements MessageRepository {

    private String BASE_URL = "http://ip.taobao.com/service/getIpInfo.php/";

    @Override
    public String getWelcomeMessage() {
        String message = "hello python";
        try {
            //在这块工作线程中模拟处理网络请求
            //在Activity退出之后 该线程一直还在执行
            //必须想办法在退出的时候停止所有的线程活动
            //for(int index=0;index<100;index++){
            //    Thread.sleep(1000);
            //    Timber.e("currentIndex  "+index);
            //}
            getIpInfo();
            //getWeatherInfo();
            //getWeatherInfoBody();
        } catch (Exception e) {
            e.printStackTrace();
            Timber.e("Error " + e.getMessage());
        }
        return message;
    }

    private String getIpInfo() {
        String area = "";
        String ipInfo = "http://ip.taobao.com/service/getIpInfo.php";
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(BASE_URL);
        builder.addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        WelcomeMessageService service = retrofit.create(WelcomeMessageService.class);
        Call<TaobaoBean> call = service.getWelcomeMessage(ipInfo);
        //异步调用
//        call.enqueue(new Callback<TaobaoBean>() {
//            @Override
//            public void onResponse(Call<TaobaoBean> call, retrofit2.Response<TaobaoBean> response) {
//                TaobaoBean bean=response.body();
//                Timber.e("city "+bean.getData().getCity());
//            }
//
//            @Override
//            public void onFailure(Call<TaobaoBean> call, Throwable t) {
//                Timber.e(t.getMessage());
//            }
//        });
        //同步调用
        try {
            retrofit2.Response<TaobaoBean> bean = call.execute();
            area = bean.body().getData().getArea();
            Timber.e("area " + area);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return area;
    }

    //这个接口暂时走不通 xml解析有问题
    private String getWeatherInfo() {
        try {
            Retrofit.Builder builder = new Retrofit.Builder();
            builder.baseUrl(BASE_URL);
            //builder.addConverterFactory(GsonConverterFactory.create());
            builder.addConverterFactory(SimpleXmlConverterFactory.create());
            Retrofit retrofit = builder.build();
            WelcomeMessageService service = retrofit.create(WelcomeMessageService.class);
            Call<WeatherInfos> call = service.getWeatherInfo("101010100");
            WeatherInfos weatherInfos= call.execute().body();
            String city=weatherInfos.getCity();
            //String aqi=weatherInfos.getAqi();
            Timber.e("city "+city);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
