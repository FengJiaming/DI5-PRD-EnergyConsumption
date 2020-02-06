package model.scheduling.plugin.local;

import java.util.Map;

import model.resources.units.ResourceUnit;
import model.resources.units.ResourceUnitName;

public interface ResourceAllocation {
	
	/**
	 * Allocates resource units( marks as used) on the basis of given resource
	 * units.
	 * 
	 * @param freeRes
	 *            resource units to be consumed
	 */
	public boolean allocateResources(Map<ResourceUnitName, ResourceUnit> freeRes);

	/**
	 * Frees given resource units.
	 * 
	 * @param lastUsedResources
	 *            resource units to be free
	 */
	public void freeResources(Map<ResourceUnitName, ResourceUnit> lastUsedResources);
	
}
