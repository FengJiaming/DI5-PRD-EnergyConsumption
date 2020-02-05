package schedframe.scheduling.manager.resources;

import java.util.List;
import java.util.Map;

import schedframe.resources.computing.ComputingResource;
import schedframe.resources.units.ResourceUnit;
import schedframe.resources.units.ResourceUnitName;

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
