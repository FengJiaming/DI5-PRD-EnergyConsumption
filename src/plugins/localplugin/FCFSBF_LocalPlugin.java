package plugins.localplugin;

import gridsim.DCWormsTags;
import model.events.scheduling.SchedulingEvent;
import model.scheduling.manager.resources.ResourceManager;
import model.scheduling.manager.tasks.JobRegistry;
import model.scheduling.plan.impl.SchedulingPlan;
import model.scheduling.plugin.grid.ModuleList;
import model.scheduling.queue.TaskQueue;
import model.scheduling.queue.TaskQueueList;
import model.scheduling.tasks.TaskInterface;

public class FCFSBF_LocalPlugin extends BaseLocalSchedulingPlugin {

	public SchedulingPlan schedule(SchedulingEvent event, TaskQueueList queues, JobRegistry jobRegistry,
			 ResourceManager resManager, ModuleList modules) {

		SchedulingPlan plan = new SchedulingPlan();
		// Choose the events types to serve. Different actions for different events are possible.
		switch (event.getType()) {
		case START_TASK_EXECUTION:
		case TASK_FINISHED:
			
			// our tasks are placed only in first queue (see BaseLocalSchedulingPlugin.placeJobsInQueues() method)
			TaskQueue q = queues.get(0);
			
			// check all tasks in queue
			for (int i = 0; i < q.size(); i++) {
				TaskInterface<?> task = q.get(i);
				
				// if status of the tasks in READY
				if (task.getStatus() == DCWormsTags.READY) {
					addToSchedulingPlan(plan, task);
				}
			}
			break;
		}
		return plan;
	}

}

