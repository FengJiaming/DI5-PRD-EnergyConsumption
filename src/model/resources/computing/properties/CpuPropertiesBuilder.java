package model.resources.computing.properties;

import java.util.Properties;

import model.resources.computing.ComputingResource;
import model.resources.computing.Processor;

public class CpuPropertiesBuilder implements PropertiesBuilder{
	protected Properties properties;
	private Processor cpu;
	
	public Properties getProperties() {
		return properties;
	}

	public void createProperties(ComputingResource resource) {
		this.properties = new Properties();
		this.cpu = (Processor)resource;
	}

	public void buildBasicProperties() {
		properties.setProperty("name", cpu.getName());
		properties.setProperty("type", cpu.getType().toString());
		properties.setProperty("status", cpu.getStatus().toString());
	}

	public void buildCharacteristicsProperties() {
		properties.setProperty("speed", String.valueOf(cpu.getMIPS()));
	}

	public void buildDescriptionProperties() {


	}



}
