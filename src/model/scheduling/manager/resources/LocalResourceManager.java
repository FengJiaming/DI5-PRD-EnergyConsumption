package model.scheduling.manager.resources;

import gridsim.GridSim;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import model.exceptions.ResourceException;
import model.resources.ResourceStatus;
import model.resources.ResourceType;
import model.resources.StandardResourceType;
import model.resources.computing.ComputingResource;
import model.resources.computing.ResourceCharacteristics;
import model.resources.computing.validator.ResourcePropertiesValidator;
import model.resources.computing.validator.ResourceValidator;
import model.resources.units.PEUnit;
import model.resources.units.ProcessingElements;
import model.resources.units.ResourceUnit;
import model.resources.units.ResourceUnitName;
import model.resources.units.ResourceUnitState;
import model.resources.units.StandardResourceUnitName;
import model.scheduling.Scheduler;
import model.scheduling.plugin.local.ResourceAllocation;


public class LocalResourceManager implements ResourceAllocation, ResourceManager {

	//private Log log = LogFactory.getLog(ResourceManager.class);

	protected List<ComputingResource> computingResources;
	protected List<Scheduler> schedulers;
	protected Map<ResourceUnitName, List<ResourceUnit>> resourceUnits;

	public LocalResourceManager(List<ComputingResource> resources, List<Scheduler> schedulers, Map<ResourceUnitName, List<ResourceUnit>> resourceUnits) {
		this.computingResources = resources;
		this.schedulers = schedulers;
		this.resourceUnits = resourceUnits;

		//initSharedResourceUnits(computingResources);
	}

	public LocalResourceManager(ComputingResource compResource) {
		this.computingResources = compResource.getChildren();

		initSharedResourceUnits(compResource);
	}

	public void initSharedResourceUnits(ComputingResource compResource){
		this.resourceUnits = new HashMap<ResourceUnitName, List<ResourceUnit>>();
		List<ResourceUnit> list;

		ComputingResource parent = compResource.getParent();
		while(parent != null){
			Map<ResourceUnitName, List<ResourceUnit>> resUnits = parent.getResourceCharacteristic().getResourceUnits();
			for(ResourceUnitName run : resUnits.keySet()){
				for(ResourceUnit resUnit : resUnits.get(run)){
					if((resourceUnits.get(run) == null)){
						list = new ArrayList<ResourceUnit>(1);
						resourceUnits.put(resUnit.getName(), list);
						list.add(resUnit);
					} else if(!resourceUnits.get(run).contains(resUnit)){
						list = resourceUnits.get(resUnit.getName());
						list.add(resUnit);
					} 
				}
			}
			parent = parent.getParent();
		}
	}
	
	public List<ComputingResource> getResources() {
		return computingResources;
	}

	public boolean areResourcesAchievable(ResourceType type) {
		if (computingResources != null) {
			Deque<ComputingResource> toExamine = new ArrayDeque<ComputingResource>();
			for (ComputingResource resource : computingResources){
				toExamine.push(resource);	
			}
			
			while (!toExamine.isEmpty()) {
				ComputingResource resource = toExamine.pop();
				if(resource.getType() == type)
					return true;
				List<ComputingResource> resources = resource.getChildren();
				/*if (resources == null)
					continue;*/
				int numberOfResComp = resources.size();
				for (int i = 0; i < numberOfResComp; i++) {
					ComputingResource resourceChild = resources.get(i);
					if (resourceChild.getType() == type) {
						return true;
					} else
						toExamine.push(resourceChild);
				}
			}
		}
		return false;
	}

	public List<? extends ComputingResource> getResourcesOfType(ResourceType type) throws ResourceException {
		List<ComputingResource> resourcesOfType = new ArrayList<ComputingResource>();
		for (ComputingResource resource : computingResources) {
			if (resource.getType().getName().equals(type.getName())) {
				resourcesOfType.add(resource);
			} else
				resourcesOfType.addAll(resource.getDescendantsByType(type));
		}
		return resourcesOfType;
	}

	public List<? extends ComputingResource> getResourcesByTypeWithStatus(ResourceType type, ResourceStatus status)
			throws ResourceException {

		List<ComputingResource> resourcesOfType = new ArrayList<ComputingResource>();
		for (ComputingResource resource : computingResources) {
			if (resource.getType().getName().equals(type.getName())) {
				if (resource.getStatus() == status) {
					resourcesOfType.add(resource);
				}
			} else
				resourcesOfType.addAll(resource.getDescendantsByTypeAndStatus(type, status));
		}
		return resourcesOfType;
	}

	public ComputingResource getResourceByName(String resourceName) throws ResourceException {
		ComputingResource resourceWithName = null;
		for (int i = 0; i < computingResources.size() && resourceWithName == null; i++) {
			ComputingResource resource = computingResources.get(i);
			if (resource.getName().equals(resourceName))
				resourceWithName = resource;
			else
				resourceWithName = resource.getDescendantByName(resourceName);
		}
		return resourceWithName;
	}

	public List<ResourceUnit> getDistributedResourceUnits(ResourceUnitName unitName)  {
		List<ResourceUnit> resourceUnit = new ArrayList<ResourceUnit>();
		if (computingResources != null) {
			Deque<ComputingResource> toExamine = new ArrayDeque<ComputingResource>();
			for (ComputingResource resource : computingResources)
				toExamine.push(resource);
			while (!toExamine.isEmpty()) {
				ComputingResource resource = toExamine.pop();
				ResourceCharacteristics resourceCharacteristic = resource.getResourceCharacteristic();
				List<ResourceUnit> units = null;
				units = resourceCharacteristic.getResourceUnits().get(unitName);
				if (units != null)
					resourceUnit.addAll(units);
				// else {
				List<ComputingResource> resources = resource.getChildren();
				/*if (resources == null)
					continue;*/
				int numberOfResComp = resources.size();
				for (int i = 0; i < numberOfResComp; i++) {
					ComputingResource resourceChild = resources.get(i);
					toExamine.push(resourceChild);
				}
				// }
			}
		}
		return resourceUnit;
	}

	public List<? extends ComputingResource> filterResources(Properties properties) {
		List<ComputingResource> descendants = new ArrayList<ComputingResource>();
		for (ComputingResource resource : computingResources) {
			ResourceValidator resourceValidator =  new ResourcePropertiesValidator(properties);
			if (resourceValidator.validate(resource))
				descendants.add(resource);
			else
				descendants.addAll(resource.filterDescendants(properties));
		}
		return descendants;
	}

	public Map<ResourceUnitName, List<ResourceUnit>> getSharedResourceUnits() {
		
		/*Map<ResourceUnitName, List<ResourceUnit>> sharedResourceUnits = new HashMap<ResourceUnitName, List<ResourceUnit>>();
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
		return sharedResourceUnits;*/
		return resourceUnits;
	}
		
	public List<Scheduler> getSchedulers() {
		return schedulers;
	}
	
	@SuppressWarnings("unchecked")
	public List<ResourceUnit> getPE() throws ResourceException{
		
		List<ResourceUnit> peUnits = null;
		List<ComputingResource> computingResources = null;
		if(areResourcesAchievable(StandardResourceType.Core)){
			try {
				computingResources = (List<ComputingResource>) getResourcesOfType(StandardResourceType.Core);
			} catch (ResourceException e) {
				throw new RuntimeException("DCWorms internal error");
			}
			PEUnit peUnit = new ProcessingElements(computingResources);
			peUnits = new ArrayList<ResourceUnit>();
			peUnits.add(peUnit);			
		}

		else if(areResourcesAchievable(StandardResourceType.Processor)){
			try {
				computingResources = (List<ComputingResource>) getResourcesOfType(StandardResourceType.Processor);
			} catch (ResourceException e) {
				throw new RuntimeException("DCWorms internal error");
			}
			PEUnit peUnit = new ProcessingElements(computingResources);
			peUnits = new ArrayList<ResourceUnit>();
			peUnits.add(peUnit);			
		}

		else if (getDistributedResourceUnits(StandardResourceUnitName.PE).size() > 0){
			peUnits = getDistributedResourceUnits(StandardResourceUnitName.PE);
		} 
		
		else if(getSharedResourceUnits().get(StandardResourceUnitName.PE) != null){
				peUnits = getSharedResourceUnits().get(StandardResourceUnitName.PE);
		}
		if(peUnits == null)
			throw new ResourceException("Processing Elements are not defined");
		return peUnits;
	}
	
	public String getSchedulerName(String resName){
		if(GridSim.getEntityId(resName) != -1){
			return resName;
		}
		ComputingResource resourceWithName = null;
		for(int i = 0 ; i < computingResources.size() && resourceWithName == null; i++){
			ComputingResource resource = computingResources.get(i);
			if(resource.getName().equals(resName))
				resourceWithName = resource;
			else
				try {
					resourceWithName = resource.getDescendantByName(resName);
				} catch (ResourceException e) {
					return null;
				}
		}
		if(resourceWithName == null)
			return null;
		List<ComputingResource> children = resourceWithName.getChildren();
		Set<Scheduler> childrenSchedulers = new HashSet<Scheduler>();
		if(children.isEmpty())
			return null;
		for(ComputingResource child: children) {
			childrenSchedulers.add(child.getScheduler());
		}	

		Set<Scheduler> tempChildrenSchedulers = new HashSet<Scheduler>(childrenSchedulers);
		while(childrenSchedulers.size() > 1){
			childrenSchedulers = new HashSet<Scheduler>();
			for(Scheduler s: tempChildrenSchedulers){
				childrenSchedulers.add(s.getParent());
			}
			tempChildrenSchedulers = new HashSet<Scheduler>(childrenSchedulers);
		}
		Iterator<Scheduler> it = childrenSchedulers.iterator();
		Scheduler potentialScheduler = it.next();
		if(potentialScheduler.getCompResources().containsAll(children))
			return potentialScheduler.get_name();
		return null;
	}
	
	public boolean allocateResources(Map<ResourceUnitName, ResourceUnit> resources) {
		
		if (resources == null) {
			return false;
		}
		
		for(ResourceUnitName resUnitName: resources.keySet()){
			ResourceUnit resUnit = resources.get(resUnitName);
			resUnit.getProvisioner().setState(ResourceUnitState.BUSY);
		}
		return true;
	}


	public void freeResources(Map<ResourceUnitName, ResourceUnit> resources) {
		
		for(ResourceUnitName resUnitName: resources.keySet()){
			ResourceUnit resUnit = resources.get(resUnitName);
			resUnit.getProvisioner().setState(ResourceUnitState.FREE);
		}
	}

}
