package com.lucky.androidlearn.domain.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by zfz on 2017/1/2.
 *注解解释
 ElementList：申明该元素是一个集合。
 Element:申明该元素只是一个普通属性
 inline=true：ElementList的一个属性，由于ElementList包了一层，如果为false将不能解析。
 required = false： 实体类中有，xml中没有，且声明为@Element的，在@Element中加上required = false 即可。
 @Root(strict = false)：xml中有的元素，而实体类中没有，在实体类的@(Root)中加上strict = false 如 @Root(strict = false)即可。
 */
@Root(name = "ws",strict = false)
@Namespace(reference = "urn:sun:params:xml:ns:weather")
public class WeatherInfos {

    @Attribute(required = true)
    private String city;
    @Attribute(required = true)
    private String aqi;

    @ElementList(inline=true,required = false)
    public List<WeatherInfo> weatherInfoList;

    public List<WeatherInfo> getWeatherInfoList() {
        return weatherInfoList;
    }

    public void setWeatherInfoList(List<WeatherInfo> weatherInfoList) {
        this.weatherInfoList = weatherInfoList;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
//
//    public String getAqi() {
//        return aqi;
//    }
//
//    public void setAqi(String aqi) {
//        this.aqi = aqi;
//    }
}
