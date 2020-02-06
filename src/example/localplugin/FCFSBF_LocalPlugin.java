package example.localplugin;

import gridsim.dcworms.DCWormsTags;
import schedframe.events.scheduling.SchedulingEvent;
import schedframe.scheduling.manager.resources.ResourceManager;
import schedframe.scheduling.manager.tasks.JobRegistry;
import schedframe.scheduling.plan.impl.SchedulingPlan;
import schedframe.scheduling.plugin.grid.ModuleList;
import schedframe.scheduling.queue.TaskQueue;
import schedframe.scheduling.queue.TaskQueueList;
import schedframe.scheduling.tasks.TaskInterface;

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

