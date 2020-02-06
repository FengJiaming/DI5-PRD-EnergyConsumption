package model.scheduling.manager.resources;

import model.scheduling.GridResourceDiscovery;
import model.scheduling.Scheduler;

public class ResourceManagerFactory {

	public static ResourceManager createResourceManager(Scheduler scheduler, ManagedResources managedResources){

		ManagedComputingResources managedResource = scheduler.getCompResources();
		
		if(managedResources == null || managedResource.getType().getName().equals("DataCenter"))
			return new GridResourceDiscovery(scheduler);
		else if (managedResource.getType().getName().equals("Rack"))
			return new ClusterResourceManager(scheduler.getCompResources(), scheduler.getChildren(), managedResources);
		else if (managedResource.getType().getName().equals("ComputingNode"))
			return new ClusterResourceManager(scheduler.getCompResources(), scheduler.getChildren(), managedResources);
		else if (managedResource.getType().getName().equals("Processor"))
			return new ClusterResourceManager(scheduler.getCompResources(), scheduler.getChildren(), managedResources);
		else return new ClusterResourceManager(scheduler.getCompResources(), scheduler.getChildren(), managedResources);
		//throw new IllegalArgumentException("ResourceType " + managedResource.getType() + " is not supported.");
		
		/*switch(managedResource.getType()){
			case DataCenter: return new GridResourceDiscovery(scheduler);
			case Rack: return new ClusterResourceManager(scheduler.getResources(), scheduler.getChildren());
			case ComputingNode: return new ClusterResourceManager(scheduler.getResources(), scheduler.getChildren());
			case Processor: return new ClusterResourceManager(scheduler.getResources(), scheduler.getChildren());
		default:
			throw new IllegalArgumentException("ResourceType " + managedResource.getType() + " is not supported.");
		}*/
	}
}
