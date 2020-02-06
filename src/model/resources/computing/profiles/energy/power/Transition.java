package model.resources.computing.profiles.energy.power;

import model.Parameters;

public class Transition {
	protected PowerStateName to;
	protected double powerUsage;
	protected double time;
	
	public Transition(PowerStateName to, double powerUsage, double time) {
		super();
		this.to = to;
		this.powerUsage = powerUsage;
		this.time = time;
	}
	public PowerStateName getTo() {
		return to;
	}
	public double getPowerUsage() {
		return powerUsage;
	}
	public double getTime() {
		return time;
	}
	
	public void init(Parameters parameters){
		
	}
}