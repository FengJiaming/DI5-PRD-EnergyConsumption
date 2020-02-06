package model.resources.computing.properties;

import java.util.Properties;

import model.resources.computing.ComputingResource;

public class DefaultPropertiesBuilder implements PropertiesBuilder{

	protected Properties properties;
	private ComputingResource resource;
	
	public Properties getProperties() {
		return properties;
	}

	public void createProperties(ComputingResource resource) {
		this.properties = new Properties();
		this.resource = resource;
	}

	public void buildBasicProperties() {
		properties.setProperty("name", resource.getName());
		properties.setProperty("type", resource.getType().toString());
		properties.setProperty("status", resource.getStatus().toString());
	}

	public void buildCharacteristicsProperties() {
		
	}

	public void buildDescriptionProperties() {

	}

}
