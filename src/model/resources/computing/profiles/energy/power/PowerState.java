package model.resources.computing.profiles.energy.power;

import java.util.List;

import model.Parameters;

public class PowerState {
	
	protected PowerStateName name;
	protected double powerUsage;
	protected List<Transition> transitions;
	
	public PowerState(PowerStateName name, double powerUsage, List<Transition> transitions) {
		super();
		this.name = name;
		this.powerUsage = powerUsage;
		this.transitions = transitions;
	}
	public PowerStateName getName() {
		return name;
	}
	public double getPowerUsage() {
		return powerUsage;
	}
	public List<Transition> getTransitions() {
		return transitions;
	}
	
	public void init(Parameters parameters){
		
	}
	
}
