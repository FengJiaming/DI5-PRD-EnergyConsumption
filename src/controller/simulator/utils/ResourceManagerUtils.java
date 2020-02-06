package controller.simulator.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.resources.ResourceStatus;
import model.resources.computing.ComputingResource;
import model.resources.units.Memory;
import model.resources.units.PEUnit;
import model.resources.units.ProcessingElements;
import model.resources.units.ResourceUnit;
import model.resources.units.ResourceUnitName;
import model.resources.units.ResourceUnitState;
import model.resources.units.StandardResourceUnitName;

public class ResourceManagerUtils {
	
	public static void clearPendingResources(Map<ResourceUnitName, ResourceUnit> choosenResources){

		ProcessingElements processingElements = (ProcessingElements)choosenResources.get(StandardResourceUnitName.PE);
		if(processingElements != null) {
			for (int i = 0; i < processingElements.size(); i++) {
				ComputingResource pe = processingElements.get(i);
				if (pe.getStatus() == ResourceStatus.PENDING) {
					pe.setStatus(ResourceStatus.FREE);
				}
			}
		}
		Memory memory = (Memory)choosenResources.get(StandardResourceUnitName.MEMORY);
		if(memory != null){
			if(memory.getProvisioner().getState()==ResourceUnitState.PENDING)
				memory.getProvisioner().setState(ResourceUnitState.FREE);
		}
	}
	
	public static void setPendingResources(Map<ResourceUnitName, ResourceUnit> choosenResources){

		/*ProcessingElements processingElements = (ProcessingElements)choosenResources.get(StandardResourceUnitName.PE);
		if(processingElements != null) {
			for (int i = 0; i < processingElements.size(); i++) {
				ComputingResource pe = processingElements.get(i);
				if (pe.getStatus() == ResourceStatus.FREE) {
					pe.setStatus(ResourceStatus.PENDING);
				}
			}
		}*/
		
		PEUnit processingElements = (PEUnit)choosenResources.get(StandardResourceUnitName.PE);
		if(processingElements != null) {
			if(processingElements.getProvisioner().getState()==ResourceUnitState.FREE)
				processingElements.getProvisioner().setState(ResourceUnitState.PENDING);
		}
		
		Memory memory = (Memory)choosenResources.get(StandardResourceUnitName.MEMORY);
		if(memory != null){
			if(memory.getProvisioner().getState()==ResourceUnitState.FREE)
				memory.getProvisioner().setState(ResourceUnitState.PENDING);
		}
	}
	
	public static ComputingResource getCommonParent(List<ComputingResource> resources){

		Set<ComputingResource> parents = new HashSet<ComputingResource>(resources);
		while(parents.size() != 1){
			parents = new HashSet<ComputingResource>();
			for(ComputingResource resource: resources){
				parents.add(resource.getParent());
			}
			resources = new ArrayList<ComputingResource>(parents);
		}
		return resources.get(0);
	}

	public static Map<ResourceUnitName, List<ResourceUnit>> getSharedResourceUnits(List<ComputingResource> compResources) {
		
		Map<ResourceUnitName, List<ResourceUnit>> sharedResourceUnits = new HashMap<ResourceUnitName, List<ResourceUnit>>();
		List<ResourceUnit> list;
		for(ComputingResource resource : compResources){
			boolean resourceNotVisited = true;
			ComputingResource parent = resource.getParent();
			while(parent != null && resourceNotVisited){
				Map<ResourceUnitName, List<ResourceUnit>> resUnits = parent.getResourceCharacteristic().getResourceUnits();
				for(ResourceUnitName run : resUnits.keySet()){
					for(ResourceUnit resUnit : resUnits.get(run)){
						if((sharedResourceUnits.get(run) == null)){
							list = new ArrayList<ResourceUnit>(1);
							sharedResourceUnits.put(resUnit.getName(), list);
							list.add(resUnit);
						} else if(!sharedResourceUnits.get(run).contains(resUnit)){
							list = sharedResourceUnits.get(resUnit.getName());
							list.add(resUnit);
						} else {
							resourceNotVisited = false;
						}
					}
				}
				parent = parent.getParent();
			}
		}
		return sharedResourceUnits;
		//return resourceUnits;
	}

}
