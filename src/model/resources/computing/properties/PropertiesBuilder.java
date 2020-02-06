package model.resources.computing.properties;

import java.util.Properties;

import model.resources.computing.ComputingResource;

public interface PropertiesBuilder {

	public Properties getProperties();

	public void createProperties(ComputingResource resource);

	public void buildBasicProperties();

	public void buildCharacteristicsProperties();

	public void buildDescriptionProperties();
}
