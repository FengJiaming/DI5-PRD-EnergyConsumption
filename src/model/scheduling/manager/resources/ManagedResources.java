package model.scheduling.manager.resources;

import java.util.List;
import java.util.Map;

import model.resources.computing.ComputingResource;
import model.resources.units.ResourceUnit;
import model.resources.units.ResourceUnitName;

public class ManagedResources {
	
	protected List<ComputingResource> computingResources;
	protected Map<ResourceUnitName, List<ResourceUnit>> resourceUnits;
	
	public ManagedResources(List<ComputingResource> computingResources,
			Map<ResourceUnitName, List<ResourceUnit>> resourceUnits) {
		this.computingResources = computingResources;
		this.resourceUnits = resourceUnits;
	}
	
	public List<ComputingResource> getComputingResources() {
		return computingResources;
	}
	
	public Map<ResourceUnitName, List<ResourceUnit>> getResourceUnits() {
		return resourceUnits;
	}
}
