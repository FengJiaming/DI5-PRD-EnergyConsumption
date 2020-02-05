package model.scheduling;

import java.util.Map;

import org.joda.time.DateTime;

import model.resources.units.ResourceUnit;
import model.resources.units.ResourceUnitName;


public class ResourceHistoryItem {

	protected Map<ResourceUnitName, ResourceUnit> usedResources;
	protected DateTime timeStamp;
	
	/**
	 * 
	 * @param map hash map of resource units, which should be remembered
	 * @param time the moment in time when this resource configuration was created  
	 */
	public ResourceHistoryItem(Map<ResourceUnitName, ResourceUnit> map, DateTime time){
		this.usedResources = map;
		this.timeStamp = time;
	}
	
	public Map<ResourceUnitName, ResourceUnit> getResourceUnits(){
		return usedResources;
	}
	
	public DateTime getTimeStamp(){
		return timeStamp;
	}
	
}
