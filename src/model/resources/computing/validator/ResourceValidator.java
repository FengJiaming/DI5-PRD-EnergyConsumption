package model.resources.computing.validator;

import model.resources.computing.ComputingResource;

public interface ResourceValidator {
	boolean validate(ComputingResource resource);
}
