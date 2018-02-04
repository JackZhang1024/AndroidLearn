package com.lucky.androidlearn.mvc.model.bean;

public class Suggestion{
	private Uv uv;
	private Dressing dressing;
	private CarWashing carWashing;
	private Travel travel;
	private Sport sport;
	private Flu flu;

	public void setUv(Uv uv){
		this.uv = uv;
	}

	public Uv getUv(){
		return uv;
	}

	public void setDressing(Dressing dressing){
		this.dressing = dressing;
	}

	public Dressing getDressing(){
		return dressing;
	}

	public void setCarWashing(CarWashing carWashing){
		this.carWashing = carWashing;
	}

	public CarWashing getCarWashing(){
		return carWashing;
	}

	public void setTravel(Travel travel){
		this.travel = travel;
	}

	public Travel getTravel(){
		return travel;
	}

	public void setSport(Sport sport){
		this.sport = sport;
	}

	public Sport getSport(){
		return sport;
	}

	public void setFlu(Flu flu){
		this.flu = flu;
	}

	public Flu getFlu(){
		return flu;
	}
}
