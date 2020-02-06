package controller.simulator.stats.implementation;

import controller.simulator.stats.implementation.out.StatsSerializer;
import model.resources.ResourceType;

public class ResourceUsageStats extends ResourceDynamicStats implements StatsInterface {

	private String[] headers = { "resourceName", "timestamp", "utilization" };

	public ResourceUsageStats(String resourceName, ResourceType resourceType, String usageType) {
		super(resourceName, resourceType, usageType);
	}

	public Object serialize(StatsSerializer serializer) {
		return serializer.visit(this);
	}

	public String[] getHeaders() {
		return headers;
	}
}
