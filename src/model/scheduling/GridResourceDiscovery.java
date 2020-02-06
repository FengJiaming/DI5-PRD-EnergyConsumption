package model.scheduling;

import eduni.simjava.Sim_event;
import eduni.simjava.Sim_type_p;
import gridsim.DCWormsTags;
import gridsim.GridSimTags;
import gridsim.IO_data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import model.exceptions.ResourceException;
import model.resources.ResourceStatus;
import model.resources.ResourceType;
import model.resources.computing.ComputingResource;
import model.resources.providers.ResourceProvider;
import model.resources.units.ResourceUnit;
import model.resources.units.ResourceUnitName;
import model.scheduling.manager.resources.ResourceManager;
import model.scheduling.plugin.grid.ModuleType;
import model.scheduling.plugin.grid.ResourceDiscovery;
import model.scheduling.tasks.requirements.AbstractResourceRequirements;

public class  GridResourceDiscovery implements ResourceManager, ResourceDiscovery {

	protected Scheduler gridBroker;
	
	public GridResourceDiscovery(Scheduler broker){
		this.gridBroker = broker;
	}
	
	public List<String> getAdministrationDomains(SecurityContext secContext) {
		throw new RuntimeException("Not implemented.");
	}

	public List<ResourceProvider> getProviders(SecurityContext secContext) {
		throw new RuntimeException("Not implemented.");
	}

	public List<ResourceProvider> getProviders(String admDomain, SecurityContext secContext) {
		throw new RuntimeException("Not implemented.");
	}

	public List<ResourceProvider> getProviders(
			AbstractResourceRequirements<?> reqDesc, SecurityContext secContext) {
		throw new RuntimeException("Not implemented.");
	}
	
	public List<SchedulerDescription> getResources(
			AbstractResourceRequirements<?> reqDesc, SecurityContext secContext) {
		throw new RuntimeException("Not implemented.");
	}

	public List<SchedulerDescription> getResources(SecurityContext secContext) {
		List <Scheduler> resourceList = gridBroker.getChildren();
		for(int i = 0; i < resourceList.size(); i++){
			int resourceId = resourceList.get(i).get_id();
			gridBroker.send(resourceId, GridSimTags.SCHEDULE_NOW, DCWormsTags.QUERY_RESOURCE_DESC, null);
		}
		
		//filter only the query response messages
		Sim_type_p pred = new Sim_type_p(DCWormsTags.QUERY_RESOURCE_DESC_RESULT);
		Sim_event ev = new Sim_event();
		
		List<SchedulerDescription> result = new ArrayList<SchedulerDescription>(resourceList.size());
		for (int i = 0; i < resourceList.size(); ++i) {
			gridBroker.sim_get_next(pred , ev);
			IO_data ioData  = (IO_data) ev.get_data();
			SchedulerDescription resDesc = (SchedulerDescription) ioData.getData();
			result.add(resDesc);
		} 
		
		return result;
	}
	
	public List<SchedulerDescription> getResources() {

		for(int i = 0; i < gridBroker.getChildren().size(); i++){
			int resourceId = gridBroker.getChildren().get(i).get_id();
			gridBroker.send(resourceId, GridSimTags.SCHEDULE_NOW, DCWormsTags.QUERY_RESOURCE_DESC, null);
		}
		
		//filter only the query response messages
		Sim_type_p pred = new Sim_type_p(DCWormsTags.QUERY_RESOURCE_DESC_RESULT);
		Sim_event ev = new Sim_event();
		
		List<SchedulerDescription> result = new ArrayList<SchedulerDescription>(gridBroker.getChildren().size());
		for (int i = 0; i < gridBroker.getChildren().size(); ++i) {
			gridBroker.sim_get_next(pred , ev);
			IO_data ioData  = (IO_data) ev.get_data();
			SchedulerDescription resDesc = (SchedulerDescription) ioData.getData();
			result.add(resDesc);
		} 
		
		return result;
	}

	public List<Scheduler> getSchedulers() {
		return gridBroker.getChildren();
	}

	@Override
	public boolean areResourcesAchievable(ResourceType type) {
		throw new UnsupportedOperationException("Not available at Grid level. Please use getResources() method instead to explore available resource providers");
	}

	@Override
	public List<? extends ComputingResource> getResourcesOfType(ResourceType type) throws ResourceException {
		throw new UnsupportedOperationException("Not available at Grid level. Please use getResources() method instead to explore available resource providers");
	}

	@Override
	public ComputingResource getResourceByName(String resourceName) {
		throw new UnsupportedOperationException("Not available at Grid level. Please use getResources() method instead to explore available resource providers");
	}

	@Override
	public List<? extends ComputingResource> getResourcesByTypeWithStatus(ResourceType type, ResourceStatus status)
			throws ResourceException {
		throw new UnsupportedOperationException("Not available at Grid level. Please use getResources() method instead to explore available resource providers");
	}

	/*@Override
	public ResourceCharacteristics getResourceCharacteristic() {
		throw new UnsupportedOperationException("Not available at Grid level. Please use getResources() method instead to explore available resource providers");
	}*/

	@Override
	public Map<ResourceUnitName, List<ResourceUnit>> getSharedResourceUnits() {
		throw new UnsupportedOperationException("Not available at Grid level. Please use getResources() method instead to explore available resource providers");
	}
	
	@Override
	public List<ResourceUnit> getDistributedResourceUnits(ResourceUnitName unitName) {
		throw new UnsupportedOperationException("Not available at Grid level. Please use getResources() method instead to explore available resource providers");
	}
	
	@Override
	public List<ComputingResource> filterResources(Properties properties) {
		throw new UnsupportedOperationException("Not available at Grid level. Please use getResources() method instead to explore available resource providers");
	}

	@Override
	public String getSchedulerName(String resourceName) {
		throw new UnsupportedOperationException("Not available at Grid level. Please use getResources() method instead to explore available resource providers");
	}

	
	public void dispose() {
	}

	public ModuleType getType() {
		return ModuleType.RESOURCE_DISCOVERY;
	}

	public void init(Properties properties) {
	}


}
