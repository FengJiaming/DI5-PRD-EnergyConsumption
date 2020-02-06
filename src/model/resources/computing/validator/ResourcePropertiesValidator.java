package model.resources.computing.validator;

import java.util.Properties;

import model.resources.computing.ComputingResource;

public class ResourcePropertiesValidator implements ResourceValidator{

	private Properties properties;
	public ResourcePropertiesValidator(Properties properties) {
		super();
		this.properties = properties;
	}	

	@Override
	public boolean validate(ComputingResource resource) {
		Properties resProperties = resource.getProperties();
		for(Object key:  properties.keySet()){
			String property = (String) key;
			if(resProperties.getProperty(property) != properties.getProperty(property))
				return false;
		}
		return true;
	}

}
