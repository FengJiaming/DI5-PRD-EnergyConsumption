package model.resources.computing.validator;

import model.resources.ResourceType;
import model.resources.computing.ComputingResource;

public class ResourceTypeValidator implements ResourceValidator {

	private ResourceType type;
	public ResourceTypeValidator(ResourceType type) {
		super();
		this.type = type;
	}	

	@Override
	public boolean validate(ComputingResource resource) {
		return resource.getType().getName().equals(type.getName());
	}

}
