package controller;

import gridsim.GridSim;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import model.Initializable;
import model.exceptions.ResourceException;
import model.resources.computing.ComputingResource;
import model.scheduling.Scheduler;

public class ResourceController {

	protected static Scheduler scheduler;
	protected static List<ComputingResource> computingResources;
	protected List<Initializable> toInit;
	protected Set<String> compResLayers;
	
	public ResourceController(Scheduler logicalStructure, List<ComputingResource> compResources){
		scheduler = logicalStructure;
		computingResources = compResources;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public List<ComputingResource> getComputingResources() {
		return computingResources;
	}

	public static ComputingResource getComputingResourceByName(String resourceName) throws ResourceException {
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

	private static Scheduler getSchedulerByName(String schedulerName) {
		Scheduler schedulerWithName = null;

		if (scheduler.getName().compareTo(schedulerName) == 0)
			schedulerWithName = scheduler;
		else if (scheduler.getChildren() != null) {
			LinkedList<Scheduler> toExamine = new LinkedList<Scheduler>();
			toExamine.push(scheduler);

			while (!toExamine.isEmpty() && schedulerWithName != null) {
				Scheduler scheduler = toExamine.pop();
				List<Scheduler> schedulers = scheduler.getChildren();

				int numberOfSched = schedulers.size();
				for (int i = 0; i < numberOfSched; i++) {
					Scheduler schedulerChild = schedulers.get(i);
					if(scheduler.getName().equals(schedulerName)){
						schedulerWithName = schedulerChild;
						break;
					} else
						toExamine.addLast(schedulerChild);
				}
			}
		}
		return schedulerWithName;
	}
	
	public static Scheduler getScheduler(String resName){
		if(GridSim.getEntityId(resName) != -1){
			return getSchedulerByName(resName);
		}
		ComputingResource resourceWithName = null;
		try {
			resourceWithName = getComputingResourceByName(resName);
		} catch (ResourceException e) {
		}
		/*for(int i = 0 ; i < computingResources.size() && resourceWithName == null; i++){
			ComputingResource resource = computingResources.get(i);
			if(resource.getName().equals(resName))
				resourceWithName = resource;
			else
				try {
					resourceWithName = resource.getDescendantByName(resName);
				} catch (ResourceException e) {
					return null;
				}
		}*/
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
			return potentialScheduler;
		return null;
	}
	
	public static ComputingResource getCommonComputingResourceParent(List<ComputingResource> compResources){
		if(compResources.size() == 0)
			return null;
		Set<ComputingResource> candidates = new HashSet<ComputingResource>(compResources);
		while(candidates.size() > 1){
			Set<ComputingResource> parents = new HashSet<ComputingResource>();
			Iterator<ComputingResource> it = candidates.iterator();
			while(it.hasNext()){
				ComputingResource compRes = it.next();
				if(compRes.getParent() != null){
					parents.add(compRes.getParent());
				}
				it.remove();
			}
			candidates.addAll(parents);
		}
		return candidates.toArray(new ComputingResource[0])[0];
	}

	public void setInitList(List<Initializable> toI) {
		toInit = toI;
	}

	public List<Initializable> getToInit() {
		return toInit;
	}

	public Set<String> getComputingResourceLayers() {
		return compResLayers;
	}

	public void setCompResLayers(Set<String> compResLayers) {
		this.compResLayers = compResLayers;
	}

}
