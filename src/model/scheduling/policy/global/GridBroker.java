package model.scheduling.policy.global;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import model.scheduling.GridResourceDiscovery;
import model.scheduling.Scheduler;
import model.scheduling.manager.resources.ManagedResources;
import model.scheduling.plugin.SchedulingPlugin;
import model.scheduling.plugin.estimation.ExecutionTimeEstimationPlugin;
import model.scheduling.plugin.grid.ModuleListImpl;
import model.scheduling.queue.TaskQueueList;

public class GridBroker extends GlobalManagementSystem { 

	private static Log log = LogFactory.getLog(GridBroker.class);

	protected Set<Integer> otherGridSchedulersIds;


	public GridBroker(String name, SchedulingPlugin schedulingPlugin, ExecutionTimeEstimationPlugin execTimeEstimationPlugin, TaskQueueList queues) throws Exception {
		super(name, "BROKER",  schedulingPlugin, execTimeEstimationPlugin, queues);

		otherGridSchedulersIds = new HashSet<Integer>();
		moduleList = new ModuleListImpl(2);
	}

	public void init(Scheduler scheduler, ManagedResources managedResources) {
		super.init(scheduler, managedResources);
		this.moduleList.add((GridResourceDiscovery)resourceManager);
	}

	public List<Integer> getMyGridResources() {
		List<Integer> providerIds = new ArrayList<Integer>();
		for(Scheduler sched: scheduler.getChildren()){
			providerIds.add(sched.get_id());
		}
		return providerIds;
	}

	public void addOtherGridSchedulerId(int id){
		otherGridSchedulersIds.add(id);
	}
	

}
