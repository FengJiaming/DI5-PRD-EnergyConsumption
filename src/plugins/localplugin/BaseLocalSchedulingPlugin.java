package plugins.localplugin;

import java.util.HashMap;
import java.util.Map;

import model.Parameters;
import model.PluginConfiguration;
import model.events.scheduling.SchedulingEvent;
import model.events.scheduling.SchedulingEventType;
import model.events.scheduling.SchedulingResponseType;
import model.resources.units.ResourceUnit;
import model.resources.units.ResourceUnitName;
import model.scheduling.TaskList;
import model.scheduling.manager.resources.ResourceManager;
import model.scheduling.manager.tasks.JobRegistry;
import model.scheduling.plan.impl.Allocation;
import model.scheduling.plan.impl.ScheduledTask;
import model.scheduling.plan.impl.SchedulingPlan;
import model.scheduling.plugin.SchedulingPluginConfiguration;
import model.scheduling.plugin.grid.ModuleList;
import model.scheduling.plugin.local.LocalSchedulingPlugin;
import model.scheduling.queue.TaskQueue;
import model.scheduling.queue.TaskQueueList;
import model.scheduling.tasks.TaskInterface;
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
