package example.globalplugin;

import java.util.LinkedList;
import java.util.List;

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
import schedframe.scheduling.queue.TaskQueue;
import schedframe.scheduling.queue.TaskQueueList;
import schedframe.scheduling.tasks.TaskInterface;
import schedframe.scheduling.tasks.WorkloadUnit;

public class GridFCFSRoundRobinPlugin extends BaseGlobalPlugin {

	private LinkedList<String> lastUsedResources = new LinkedList<String>();

	public SchedulingPlanInterface schedule(SchedulingEvent event,
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
		
		for(int i = 0; i < size; i++) {
			WorkloadUnit job = q.remove(0);
			TaskInterface<?> task = (TaskInterface<?>)job;
			
			SchedulerDescription sd = availableResources.get(availableResources.size() - 1);
			for(SchedulerDescription schedDesc: availableResources){
				if(!lastUsedResources.contains(schedDesc.getProvider().getProviderId())){
					if(lastUsedResources.size() + 1 >= availableResources.size()){
						lastUsedResources.poll();
					}
					lastUsedResources.add(schedDesc.getProvider().getProviderId());
					sd = schedDesc;
					break;
				}
			}
		
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

}
