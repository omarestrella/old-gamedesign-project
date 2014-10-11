package edu.ncsu.csc481.models;

public class Timeline {
	public static enum SPEED {
		HALF,
		NORMAL,
		DOUBLE
	}
	
	private SPEED speed;
	
	public Timeline(SPEED speed) {
		this.speed = speed;
	}
	
	public int getFPS() {
		if(speed == SPEED.HALF) {
			return 30;
		} else if(speed == SPEED.DOUBLE) {
			return 120;
		} else {
			return 60;
		}
	}
	
	public SPEED getSpeed() {
		return speed;
	}
	
	public void setSpeed(SPEED speed) {
		this.speed = speed;
	}
}
