package com.lucky.androidlearn.mvc.model.bean;

import java.util.List;

public class WeatherItem{
	private String city_name;
	private List<FutureItem> future;
	private String last_update;
	private Now now;
	private Today today;
	private String city_id;

	public void setCity_name(String city_name){
		this.city_name = city_name;
	}

	public String getCity_name(){
		return city_name;
	}

	public void setFuture(List<FutureItem> future){
		this.future = future;
	}

	public List<FutureItem> getFuture(){
		return future;
	}

	public void setLast_update(String last_update){
		this.last_update = last_update;
	}

	public String getLast_update(){
		return last_update;
	}

	public void setNow(Now now){
		this.now = now;
	}

	public Now getNow(){
		return now;
	}

	public void setToday(Today today){
		this.today = today;
	}

	public Today getToday(){
		return today;
	}

	public void setCity_id(String city_id){
		this.city_id = city_id;
	}

	public String getCity_id(){
		return city_id;
	}

	@Override
	public String toString() {
		return "WeatherItem{" +
				"city_name='" + city_name + '\'' +
				", future=" + future +
				", last_update='" + last_update + '\'' +
				", now=" + now +
				", today=" + today +
				", city_id='" + city_id + '\'' +
				'}';
	}
}