package plugins.globalplugin;

import java.util.LinkedList;
import java.util.List;

import model.events.scheduling.SchedulingEvent;
import model.scheduling.SchedulerDescription;
import model.scheduling.manager.resources.ResourceManager;
import model.scheduling.manager.tasks.JobRegistry;
import model.scheduling.plan.SchedulingPlanInterface;
import model.scheduling.plan.impl.Allocation;
import model.scheduling.plan.impl.ScheduledTask;
import model.scheduling.plan.impl.SchedulingPlan;
import model.scheduling.plugin.grid.Module;
import model.scheduling.plugin.grid.ModuleList;
import model.scheduling.plugin.grid.ResourceDiscovery;
import model.scheduling.queue.TaskQueue;
import model.scheduling.queue.TaskQueueList;
import model.scheduling.tasks.TaskInterface;
import model.scheduling.tasks.WorkloadUnit;

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
