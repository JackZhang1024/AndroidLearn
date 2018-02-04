package com.lucky.androidlearn.mvc.model.bean;

import java.util.List;

public class WeatherModelBean{
	private List<WeatherItem> weather;
	private String status;

	public void setWeather(List<WeatherItem> weather){
		this.weather = weather;
	}

	public List<WeatherItem> getWeather(){
		return weather;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}