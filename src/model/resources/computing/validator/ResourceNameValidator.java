package model.resources.computing.validator;

import model.resources.computing.ComputingResource;

public class ResourceNameValidator implements ResourceValidator {

	private String name;
	public ResourceNameValidator(String name) {
		super();
		this.name = name;
	}	

	@Override
	public boolean validate(ComputingResource resource) {
		return resource.getName().equals(name);
	}

}
