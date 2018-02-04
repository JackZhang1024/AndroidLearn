package com.lucky.androidlearn.mvc.model.bean;

public class AirQuality{
	private City city;
	private Object stations;

	public void setCity(City city){
		this.city = city;
	}

	public City getCity(){
		return city;
	}

	public void setStations(Object stations){
		this.stations = stations;
	}

	public Object getStations(){
		return stations;
	}
}
