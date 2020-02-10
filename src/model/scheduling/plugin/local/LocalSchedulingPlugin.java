package model.scheduling.plugin.local;

import model.events.scheduling.SchedulingEvent;
import model.events.scheduling.SchedulingResponseType;
import model.scheduling.manager.resources.ResourceManager;
import model.scheduling.manager.tasks.JobRegistry;
import model.scheduling.plugin.SchedulingPlugin;
import model.scheduling.plugin.grid.ModuleList;
import model.scheduling.queue.TaskQueueList;


public interface LocalSchedulingPlugin extends SchedulingPlugin {

	public SchedulingResponseType handleResourceAllocationViolation(SchedulingEvent event,
					TaskQueueList queues, 
					JobRegistry jobRegistry, 
					ResourceManager resourceManager, ModuleList modules);

}
