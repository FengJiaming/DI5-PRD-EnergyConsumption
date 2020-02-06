package model.resources.computing.properties;

import java.util.Properties;

import model.resources.computing.ComputingResource;

public class PropertiesDirector {
	PropertiesBuilder propBuilder;

	public void setPropertiesBuilder(PropertiesBuilder pb) {
		propBuilder = pb;
	}

	public Properties getProperties() {
		return propBuilder.getProperties();
	}

	public void constructProperties(ComputingResource resource) {
		propBuilder.createProperties(resource);
		propBuilder.buildBasicProperties();
		propBuilder.buildCharacteristicsProperties();
		propBuilder.buildDescriptionProperties();
	}
}
