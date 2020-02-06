package example.localplugin;

import gridsim.dcworms.DCWormsTags;

import java.util.List;
import java.util.Random;

import schedframe.events.scheduling.SchedulingEvent;
import schedframe.resources.computing.ComputingNode;
import schedframe.scheduling.manager.resources.ClusterResourceManager;
import schedframe.scheduling.manager.resources.ResourceManager;
import schedframe.scheduling.manager.tasks.JobRegistry;
import schedframe.scheduling.plan.SchedulingPlanInterface;
import schedframe.scheduling.plan.impl.SchedulingPlan;
import schedframe.scheduling.plugin.grid.ModuleList;
import schedframe.scheduling.queue.TaskQueue;
import schedframe.scheduling.queue.TaskQueueList;
import schedframe.scheduling.tasks.TaskInterface;

public class FCFSBF_RandomClusterPlugin extends BaseLocalSchedulingPlugin {

	private Random rand;
	
	public FCFSBF_RandomClusterPlugin() {
		rand = new Random(5);
	}

	public SchedulingPlanInterface<?> schedule(SchedulingEvent event, TaskQueueList queues, JobRegistry jobRegistry,
			 ResourceManager resManager, ModuleList modules) {

		ClusterResourceManager resourceManager = (ClusterResourceManager) resManager;
		SchedulingPlan plan = new SchedulingPlan();
		// choose the events types to serve.
		// Different actions for different events are possible.
		switch (event.getType()) {
		case START_TASK_EXECUTION:
		case TASK_FINISHED:
		//case TIMER:
			// our tasks are placed only in first queue (see
			// BaseLocalSchedulingPlugin.placeJobsInQueues() method)
			TaskQueue q = queues.get(0);
			
			// check all tasks in queue
			for (int i = 0; i < q.size(); i++) {
				TaskInterface<?> task = q.get(i);
				// if status of the tasks in READY
				if (task.getStatus() == DCWormsTags.READY) {

					String nodeName = chooseRandomProvider(resourceManager);
					if (nodeName != null) {
						addToSchedulingPlan(plan, task, nodeName);
					}
				}
			}
			break;
		}
		return plan;
	}

	private String chooseRandomProvider(ClusterResourceManager resourceManager) {
		List<ComputingNode> nodes = resourceManager.getComputingNodes();
		int nodeIdx = rand.nextInt(nodes.size());
		return nodes.get(nodeIdx).getName();
	}

}

