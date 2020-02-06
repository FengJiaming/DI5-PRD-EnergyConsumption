package model.resources.computing.properties;

import java.util.Properties;

import model.resources.computing.ComputingNode;
import model.resources.computing.ComputingResource;

public class ComputingNodePropertiesBuilder implements PropertiesBuilder{
	protected Properties properties;
	private ComputingNode node;
	
	public Properties getProperties() {
		return properties;
	}

	public void createProperties(ComputingResource resource) {
		this.properties = new Properties();
		this.node = (ComputingNode)resource;
	}

	public void buildBasicProperties() {
		properties.setProperty("name", node.getName());
		properties.setProperty("type", node.getType().toString());
		properties.setProperty("status", node.getStatus().toString());
	}

	public void buildCharacteristicsProperties() {
		try {
			properties.setProperty("totalmemory", String.valueOf(node.getTotalMemory()));
			properties.setProperty("freememory", String.valueOf(node.getFreeMemory()));
			properties.setProperty("cost", String.valueOf(node.getProcessingCost()));
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	public void buildDescriptionProperties() {

	}
}
