package model.resources.computing.validator;

import model.resources.ResourceStatus;
import model.resources.computing.ComputingResource;

public class ResourceStatusValidator implements ResourceValidator{
	
	private ResourceStatus status;
	
	public ResourceStatusValidator(ResourceStatus status) {
		super();
		this.status = status;
	}	

	@Override
	public boolean validate(ComputingResource resource) {
		return resource.getStatus() == status;
	}

}
