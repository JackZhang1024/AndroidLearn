package com.lucky.androidlearn.mvc.model.bean;

public class Today{
	private String sunrise;
	private String sunset;
	private Suggestion suggestion;

	public void setSunrise(String sunrise){
		this.sunrise = sunrise;
	}

	public String getSunrise(){
		return sunrise;
	}

	public void setSunset(String sunset){
		this.sunset = sunset;
	}

	public String getSunset(){
		return sunset;
	}

	public void setSuggestion(Suggestion suggestion){
		this.suggestion = suggestion;
	}

	public Suggestion getSuggestion(){
		return suggestion;
	}
}
