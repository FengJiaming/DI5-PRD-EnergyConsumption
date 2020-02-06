package model.resources.computing.profiles.energy.power;


public enum StandardPowerStateName implements PowerStateName{

	ON,
	OFF,
	SLEEP,
	SUSPEND,
	HIBERNATE;

	public String getName() {
		return toString();
	}
	
}
