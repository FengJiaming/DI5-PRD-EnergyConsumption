package model.resources.units;

public class UserResourceUnitName implements ResourceUnitName {

	protected String name;

	public UserResourceUnitName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
