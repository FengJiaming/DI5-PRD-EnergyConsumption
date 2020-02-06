package example.localplugin;

import gridsim.dcworms.DCWormsTags;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import schedframe.events.scheduling.SchedulingEvent;
import schedframe.resources.computing.ComputingNode;
import schedframe.resources.computing.profiles.energy.airthroughput.StandardAirThroughputStateName;
import schedframe.resources.computing.profiles.energy.airthroughput.UserAirThroughputStateName;
import schedframe.scheduling.manager.resources.ClusterResourceManager;
import schedframe.scheduling.manager.resources.ResourceManager;
import schedframe.scheduling.manager.tasks.JobRegistry;
import schedframe.scheduling.manager.tasks.JobRegistryImpl;
import schedframe.scheduling.plan.SchedulingPlanInterface;
import schedframe.scheduling.plan.impl.SchedulingPlan;
import schedframe.scheduling.plugin.grid.ModuleList;
import schedframe.scheduling.queue.TaskQueue;
import schedframe.scheduling.queue.TaskQueueList;
import schedframe.scheduling.tasks.TaskInterface;

public class FCFSBF_FanManagementClusterPlugin extends BaseLocalSchedulingPlugin {

	private Random rand;
	
	public FCFSBF_FanManagementClusterPlugin () {
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
			List<ComputingNode> notSelectedNodes = resourceManager.getComputingNodes();
			// check all tasks in queue
			for (int i = 0; i < q.size(); i++) {
				TaskInterface<?> task = q.get(i);
				// if status of the tasks in READY
				if (task.getStatus() == DCWormsTags.READY) {

					ComputingNode node = chooseRandomProvider(resourceManager, task);
					if (node != null) {
						//if there are two or more tasks ( running on the given node then
						if(new JobRegistryImpl(node.getName()).getRunningTasks().size() > 0)
							node.getAirThroughputInterface().setAirThroughputState(new UserAirThroughputStateName("FAN_ON_TURBO"));
						else 
							node.getAirThroughputInterface().setAirThroughputState(StandardAirThroughputStateName.FAN_ON);
						notSelectedNodes.remove(node);
						addToSchedulingPlan(plan, task, node.getName());
					}
				}
			}
			adjustOtherFans(notSelectedNodes);
			break;
		}
		return plan;
	}

	private ComputingNode chooseRandomProvider(ClusterResourceManager resourceManager, TaskInterface<?> task) {
		List<ComputingNode> nodes = filterNodes(resourceManager.getComputingNodes(), task);
		return randomNode(nodes);
	}
	
	private List<ComputingNode> filterNodes(List<ComputingNode> nodes, TaskInterface<?> task){
		List<ComputingNode> filteredNodes = new ArrayList<ComputingNode>();
		for (ComputingNode node : nodes) {
			int cpuRequest;
			try {
				cpuRequest = Double.valueOf(task.getCpuCntRequest()).intValue();
			} catch (NoSuchFieldException e) {
				cpuRequest = 0;
			}
			if (cpuRequest != 0) {
				if(node.getFreeProcessorsNumber() > cpuRequest)
					filteredNodes.add(node);
			}
		}
		
		return filteredNodes;
	}
	
	private ComputingNode randomNode(List<ComputingNode> nodes){
		return nodes.get(rand.nextInt(nodes.size()));
	}
	
	private void adjustOtherFans(List<ComputingNode> nodes){
		for(ComputingNode node : nodes){
			if(node.getFreeProcessorsNumber() == node.getProcessorsNumber()){
				node.getAirThroughputInterface().setAirThroughputState(StandardAirThroughputStateName.FAN_OFF);
			} else if(new JobRegistryImpl(node.getName()).getRunningTasks().size() > 1)
				node.getAirThroughputInterface().setAirThroughputState(new UserAirThroughputStateName("FAN_ON_TURBO"));
			else 
				node.getAirThroughputInterface().setAirThroughputState(StandardAirThroughputStateName.FAN_ON);
		}
	}

	
}