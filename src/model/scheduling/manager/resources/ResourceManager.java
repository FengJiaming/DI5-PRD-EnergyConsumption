package model.scheduling.manager.resources;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import model.exceptions.ResourceException;
import model.resources.ResourceStatus;
import model.resources.ResourceType;
import model.resources.computing.ComputingResource;
import model.resources.units.ResourceUnit;
import model.resources.units.ResourceUnitName;
import model.scheduling.Scheduler;

public interface ResourceManager {

	public boolean areResourcesAchievable(ResourceType type);
	
	public List<? extends ComputingResource> getResourcesOfType(ResourceType type) throws ResourceException;

	public ComputingResource getResourceByName(String resourceName) throws ResourceException;

	public List<? extends ComputingResource> getResourcesByTypeWithStatus(ResourceType type, ResourceStatus status) throws ResourceException;
	
	public Map<ResourceUnitName, List<ResourceUnit>> getSharedResourceUnits();
	
	public List<ResourceUnit> getDistributedResourceUnits(ResourceUnitName unitName);
	
	public List<? extends ComputingResource> filterResources(Properties properties);
	
	public List<Scheduler> getSchedulers();

	public String getSchedulerName(String resourceName);
}
