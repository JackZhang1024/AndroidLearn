package com.lucky.androidlearn.mvc.model.bean;

import java.util.List;

public class Now{
	private String code;
	private String visibility;
	private List<Object> alarms;
	private String wind_direction;
	private AirQuality air_quality;
	private String pressure;
	private String feels_like;
	private String pressure_rising;
	private String wind_scale;
	private String temperature;
	private String humidity;
	private String wind_speed;
	private String text;

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return code;
	}

	public void setVisibility(String visibility){
		this.visibility = visibility;
	}

	public String getVisibility(){
		return visibility;
	}

	public void setAlarms(List<Object> alarms){
		this.alarms = alarms;
	}

	public List<Object> getAlarms(){
		return alarms;
	}

	public void setWind_direction(String wind_direction){
		this.wind_direction = wind_direction;
	}

	public String getWind_direction(){
		return wind_direction;
	}

	public void setAir_quality(AirQuality air_quality){
		this.air_quality = air_quality;
	}

	public AirQuality getAir_quality(){
		return air_quality;
	}

	public void setPressure(String pressure){
		this.pressure = pressure;
	}

	public String getPressure(){
		return pressure;
	}

	public void setFeels_like(String feels_like){
		this.feels_like = feels_like;
	}

	public String getFeels_like(){
		return feels_like;
	}

	public void setPressure_rising(String pressure_rising){
		this.pressure_rising = pressure_rising;
	}

	public String getPressure_rising(){
		return pressure_rising;
	}

	public void setWind_scale(String wind_scale){
		this.wind_scale = wind_scale;
	}

	public String getWind_scale(){
		return wind_scale;
	}

	public void setTemperature(String temperature){
		this.temperature = temperature;
	}

	public String getTemperature(){
		return temperature;
	}

	public void setHumidity(String humidity){
		this.humidity = humidity;
	}

	public String getHumidity(){
		return humidity;
	}

	public void setWind_speed(String wind_speed){
		this.wind_speed = wind_speed;
	}

	public String getWind_speed(){
		return wind_speed;
	}

	public void setText(String text){
		this.text = text;
	}

	public String getText(){
		return text;
	}
}