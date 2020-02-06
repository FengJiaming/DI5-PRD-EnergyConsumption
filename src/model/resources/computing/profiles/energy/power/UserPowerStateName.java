package model.resources.computing.profiles.energy.power;

public class UserPowerStateName implements PowerStateName {

	protected String name;

	public UserPowerStateName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}


}
