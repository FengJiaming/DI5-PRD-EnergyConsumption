package model.scheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import model.resources.StandardResourceType;
import model.resources.computing.description.ExecutingResourceDescription;
import model.resources.providers.ResourceProvider;
import model.resources.units.ResourceUnit;
import model.resources.units.ResourceUnitName;
import model.scheduling.queue.QueueDescription;

public class SchedulerDescription extends ExecutingResourceDescription{

	protected ResourceProvider provider;
	protected List<QueueDescription> accesQueues;

	public SchedulerDescription(ResourceProvider provider){
		super(StandardResourceType.ResourceProvider);
		this.id = provider.getProviderId();
		this.provider = provider;
		this.resUnits = new HashMap<ResourceUnitName, List<ResourceUnit>>();
		this.accesQueues = new ArrayList<QueueDescription>(1);
	}
	
	public SchedulerDescription(String id){
		super(StandardResourceType.ResourceProvider);
		this.id = id;
		this.provider = new ResourceProvider(id, null, null, null);
		this.resUnits = new HashMap<ResourceUnitName, List<ResourceUnit>>();
		this.accesQueues = new ArrayList<QueueDescription>(1);
	}
	
	public void addQueueDescription(QueueDescription queue){
		accesQueues.add(queue);
	}
	
	public void addQueuesDescription(List<QueueDescription> queue){
		accesQueues.addAll(queue);
	}
	
	public List<QueueDescription> getAvailableQueues(){
		return this.accesQueues;
	}
	
	public void addResourceUnit(Map<ResourceUnitName, ResourceUnit> allUnits){
		Iterator<ResourceUnit> itr = allUnits.values().iterator();
		while(itr.hasNext()){
			addResourceUnit(itr.next());
		}
	}
	
	public void addResourceUnitList(Map<ResourceUnitName, List<ResourceUnit>> allUnits){
		this.resUnits.putAll(allUnits);
	}

	public ResourceProvider getProvider() {
		return provider;
	}
}
