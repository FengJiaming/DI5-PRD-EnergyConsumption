package controller.simulator.stats.implementation;

import controller.simulator.stats.implementation.out.StatsSerializer;
import model.resources.ResourceType;

public class ResourcePowerStats extends ResourceDynamicStats implements StatsInterface{

	protected double sumValue;

	private String[] headers = { "resourceName", "timestamp", "consumption" };
	
	public ResourcePowerStats (String resourceName, ResourceType resourceType, String usageType) {
		super(resourceName, resourceType, usageType);
	}

	public void setSumValue(double sumValue) {
		this.sumValue = sumValue;
	}

	public double getSumValue() {
		return sumValue;
	}

	public Object serialize(StatsSerializer serializer) {
		return serializer.visit(this);
	}

	public String[] getHeaders() {
		return headers;
	}

}
