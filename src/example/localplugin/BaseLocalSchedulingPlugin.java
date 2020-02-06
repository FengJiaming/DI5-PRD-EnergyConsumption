package example.localplugin;

import java.util.HashMap;
import java.util.Map;

import schedframe.Parameters;
import schedframe.PluginConfiguration;
import schedframe.events.scheduling.SchedulingEvent;
import schedframe.events.scheduling.SchedulingEventType;
import schedframe.events.scheduling.SchedulingResponseType;
import schedframe.resources.units.ResourceUnit;
import schedframe.resources.units.ResourceUnitName;
import schedframe.scheduling.TaskList;
import schedframe.scheduling.manager.resources.ResourceManager;
import schedframe.scheduling.manager.tasks.JobRegistry;
import schedframe.scheduling.plan.impl.Allocation;
import schedframe.scheduling.plan.impl.ScheduledTask;
import schedframe.scheduling.plan.impl.SchedulingPlan;
import schedframe.scheduling.plugin.SchedulingPluginConfiguration;
import schedframe.scheduling.plugin.grid.ModuleList;
import schedframe.scheduling.plugin.local.LocalSchedulingPlugin;
import schedframe.scheduling.queue.TaskQueue;
import schedframe.scheduling.queue.TaskQueueList;
import schedframe.scheduling.tasks.TaskInterface;
import schemas.StringValueWithUnit;

public abstract class BaseLocalSchedulingPlugin implements LocalSchedulingPlugin {

	SchedulingPluginConfiguration plugConf;
	
	public PluginConfiguration getConfiguration() {
		return plugConf;
	}
	
	public SchedulingResponseType handleResourceAllocationViolation(SchedulingEvent event,
			TaskQueueList queues, 
			JobRegistry jobRegistry, 
					ResourceManager resourceManager, ModuleList modules){
		SchedulingResponseType responeEvent = null;
		switch(event.getType()){
			case TASK_REQUESTED_TIME_EXPIRED: 
				responeEvent = SchedulingResponseType.KILL_TASK;
				break;
		}
		return responeEvent;
	}
	
	public boolean placeTasksInQueues(TaskList newTasks,
			TaskQueueList queues, 
			ResourceManager resourceManager, ModuleList moduleList) {
		
		// get the first queue from all available queues.
		TaskQueue queue = queues.get(0);

		for(int i = 0; i < newTasks.size(); i++){
			TaskInterface<?> task = newTasks.get(i);
			queue.add(task);
		}
		
		return true;
	}
	
	public void addToSchedulingPlan(SchedulingPlan plan, TaskInterface<?> task, Map<ResourceUnitName, ResourceUnit> choosenResources ){
		
		Allocation allocation = new Allocation();
		allocation.setProcessesCount(1);
		allocation.setRequestedResources(choosenResources);
		
		ScheduledTask scheduledTask = new ScheduledTask(task);
		scheduledTask.setTaskId(task.getId());
		scheduledTask.setJobId(task.getJobId());
		scheduledTask.addAllocation(allocation);		
		
		plan.addTask(scheduledTask);
	}
	
	public void addToSchedulingPlan(SchedulingPlan plan, TaskInterface<?> task, String providerName){
		
		Allocation allocation = new Allocation();
		allocation.setProcessesCount(1);
		allocation.setProviderName(providerName);
		allocation.setRequestedResources(new HashMap<ResourceUnitName, ResourceUnit>());
		
		ScheduledTask scheduledTask = new ScheduledTask(task);
		scheduledTask.setTaskId(task.getId());
		scheduledTask.setJobId(task.getJobId());
		scheduledTask.addAllocation(allocation);		
		
		plan.addTask(scheduledTask);
	}
	
	public void addToSchedulingPlan(SchedulingPlan plan, TaskInterface<?> task){
		
		Allocation allocation = new Allocation();
		allocation.setProcessesCount(1);
		allocation.setProviderName(null);
		allocation.setRequestedResources(new HashMap<ResourceUnitName, ResourceUnit>());
		
		ScheduledTask scheduledTask = new ScheduledTask(task);
		scheduledTask.setTaskId(task.getId());
		scheduledTask.setJobId(task.getJobId());
		scheduledTask.addAllocation(allocation);		
		
		plan.addTask(scheduledTask);
	}

	public void init(Parameters parameters) {
		plugConf = new SchedulingPluginConfiguration();
		try{
			StringValueWithUnit freq = parameters.get("frequency").get(0);
			plugConf.addServedEvent(SchedulingEventType.TIMER, Double.valueOf(freq.getContent()).intValue());
		} catch(Exception e){
			
		}
	}

	public String getName() {
		return getClass().getName();
	}

}
