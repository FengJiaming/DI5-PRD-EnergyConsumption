package example.globalplugin;


import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import schedframe.events.scheduling.SchedulingEvent;
import schedframe.scheduling.SchedulerDescription;
import schedframe.scheduling.manager.resources.ResourceManager;
import schedframe.scheduling.manager.tasks.JobRegistry;
import schedframe.scheduling.plan.SchedulingPlanInterface;
import schedframe.scheduling.plan.impl.Allocation;
import schedframe.scheduling.plan.impl.ScheduledTask;
import schedframe.scheduling.plan.impl.SchedulingPlan;
import schedframe.scheduling.plugin.grid.Module;
import schedframe.scheduling.plugin.grid.ModuleList;
import schedframe.scheduling.plugin.grid.ResourceDiscovery;
import schedframe.scheduling.queue.QueueDescription;
import schedframe.scheduling.queue.TaskQueue;
import schedframe.scheduling.queue.TaskQueueList;
import schedframe.scheduling.tasks.TaskInterface;
import schedframe.scheduling.tasks.WorkloadUnit;

public class GridFCFSLoadBalancingPlugin extends BaseGlobalPlugin {

	private Log log = LogFactory.getLog(GridFCFSLoadBalancingPlugin.class);
	
	public SchedulingPlanInterface<?> schedule(SchedulingEvent event,
			TaskQueueList queues, 
			JobRegistry jobRegistry,
			ResourceManager resourceManager, ModuleList modules)  {
	
		ResourceDiscovery resources = null;
		for(int i = 0; i < modules.size(); i++){
			Module m = modules.get(i);
			switch(m.getType()){
				case RESOURCE_DISCOVERY: resources = (ResourceDiscovery) m;
					break;
			}
		}
		
		SchedulingPlan plan = new SchedulingPlan();
		
		TaskQueue q = queues.get(0);
		int size = q.size();
		
		// order of the resources on this list is not determined
		List<SchedulerDescription> availableResources = resources.getResources();
		int resourceIdx = -1;
		
		for(int i = 0; i < size; i++) {
			WorkloadUnit job = q.remove(0);
			TaskInterface<?> task = (TaskInterface<?>)job;

			resourceIdx = findLeastLoadedResourceIdx(availableResources);
			SchedulerDescription sd = availableResources.get(resourceIdx);
			
			Allocation allocation = new Allocation();
			allocation.setProcessesCount(1);
			allocation.setProviderName(sd.getProvider().getProviderId());
			
			ScheduledTask scheduledTask = new ScheduledTask(task);
			scheduledTask.setTaskId(task.getId());
			scheduledTask.setJobId(task.getJobId());
			scheduledTask.addAllocation(allocation);		
			
			plan.addTask(scheduledTask);
		}
		return plan;
		
	}

	private int findLeastLoadedResourceIdx(List<SchedulerDescription> availableResources){
		int resourceIdx = -1;
		long minLoad = Long.MAX_VALUE;
		
		for(int i = 0; i < availableResources.size(); i++){
			SchedulerDescription sd = availableResources.get(i);
			long totalLoad = getQueueLoad(sd.getAvailableQueues());
			if(totalLoad < minLoad){
				resourceIdx = i;
				minLoad = totalLoad;
			}
		}
		return resourceIdx;
	}

	private long getQueueLoad(List<QueueDescription> queues){
		long load = 0;
		for(QueueDescription queue : queues){
			load = load + queue.getLoad();
		}
		return load;
	}
	
}
