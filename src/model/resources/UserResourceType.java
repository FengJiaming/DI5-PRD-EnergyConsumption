package model.resources;


public class UserResourceType implements ResourceType {

	protected String name;

	public UserResourceType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
}
