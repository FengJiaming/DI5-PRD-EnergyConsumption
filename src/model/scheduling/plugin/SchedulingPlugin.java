package model.scheduling.plugin;

import model.Plugin;
import model.events.scheduling.SchedulingEvent;
import model.scheduling.TaskList;
import model.scheduling.manager.resources.ResourceManager;
import model.scheduling.manager.tasks.JobRegistry;
import model.scheduling.plan.SchedulingPlanInterface;
import model.scheduling.plugin.grid.ModuleList;
import model.scheduling.queue.TaskQueueList;

public interface SchedulingPlugin extends Plugin{

	public boolean placeTasksInQueues(TaskList newTasks,
			TaskQueueList queues, 
			ResourceManager resourceManager, ModuleList modules);
	
	public SchedulingPlanInterface<?> schedule(SchedulingEvent event, 
			TaskQueueList queues, 
			JobRegistry jobRegistry, 
			ResourceManager resourceManager, ModuleList modules);
	
}

