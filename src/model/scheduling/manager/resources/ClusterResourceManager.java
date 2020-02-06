package model.scheduling.manager.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import model.exceptions.ResourceException;
import model.resources.StandardResourceType;
import model.resources.computing.ComputingNode;
import model.resources.computing.ComputingResource;
import model.resources.computing.Processor;
import model.scheduling.Scheduler;


public class ClusterResourceManager extends LocalResourceManager {

	public ClusterResourceManager(List<ComputingResource> resources, List<Scheduler> schedulers, ManagedResources managedResources) {
		super(resources, schedulers, managedResources.getResourceUnits());
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public List<ComputingNode> getComputingNodes(){
		try {
			return (List<ComputingNode>) getResourcesOfType(StandardResourceType.ComputingNode);
		} catch (ResourceException e) {
			return new ArrayList<ComputingNode>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Processor> getProcessors(){
		try {
			return (List<Processor>) getResourcesOfType(StandardResourceType.Processor);
		} catch (Exception e) {
			return new ArrayList<Processor>();
		}
	}

	@SuppressWarnings("unchecked")
	public List<ComputingNode> getComputingNodes(Properties properties){
		properties.setProperty("type", StandardResourceType.ComputingNode.toString());
		return (List<ComputingNode>) filterResources(properties);

	}

}
