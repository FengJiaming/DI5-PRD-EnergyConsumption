package controller.simulator.stats.implementation;

import java.util.Map;
import java.util.TreeMap;

import model.resources.ResourceType;


public abstract class ResourceDynamicStats {
	
	protected Map<Long, Double> history;
	protected String resourceName;
	protected String usageType;
	protected ResourceType resourceType;
	protected double meanValue;

	private String[] headers = { "resourceName", "timestamp", "usage" };

	public ResourceDynamicStats(String resourceName, ResourceType resourceType, String usageType) {
		this.resourceName = resourceName;
		this.resourceType = resourceType;
		this.usageType = usageType;
		this.history = new TreeMap<Long, Double>();
		this.meanValue = 0;
	}

	public void setMeanValue(double meanValue) {
		this.meanValue = meanValue;
	}
	
	public double getMeanValue() {
		return meanValue;
	}

	public String getResourceName() {
		return this.resourceName;
	}
	
	public ResourceType getResourceType() {
		return resourceType;
	}
	
	public String getUsageType() {
		return this.usageType;
	}	

	public Map<Long, Double> getHistory() {
		return this.history;
	}

	public String[] getHeaders() {
		return headers;
	}
}
