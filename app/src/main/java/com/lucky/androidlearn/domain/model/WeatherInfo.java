package com.lucky.androidlearn.domain.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 * Created by zfz on 2017/1/2.
 */

@Root(name = "w", strict = false)
@Namespace(reference = "urn:sun:params:xml:ns:weather")
public class WeatherInfo {

    @Attribute(name = "date", required = true)
    @Path("w")
    private String date;

    @Attribute(name = "weather", required = true)
    @Path("w")
    private String weather;

    @Attribute(name = "low", required = true)
    @Path("w")
    private String low;

    @Attribute(name = "high", required = true)
    @Path("w")
    private String high;

    @Attribute(name = "wind", required = true)
    @Path("w")
    private String wind;

    @Attribute(name = "wd", required = true)
    @Path("w")
    private String wd;

    @Attribute(name = "icon1", required = true)
    @Path("w")
    private String icon1;

    @Attribute(name = "icon2", required = true)
    @Path("w")
    private String icon2;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWd() {
        return wd;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }

    public String getIcon1() {
        return icon1;
    }

    public void setIcon1(String icon1) {
        this.icon1 = icon1;
    }

    public String getIcon2() {
        return icon2;
    }

    public void setIcon2(String icon2) {
        this.icon2 = icon2;
    }
}
