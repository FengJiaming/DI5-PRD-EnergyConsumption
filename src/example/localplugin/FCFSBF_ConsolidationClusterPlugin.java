package example.localplugin;

import gridsim.dcworms.DCWormsTags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import schedframe.events.scheduling.SchedulingEvent;
import schedframe.resources.ResourceStatus;
import schedframe.resources.computing.ComputingNode;
import schedframe.resources.computing.ComputingResource;
import schedframe.resources.units.Memory;
import schedframe.resources.units.ProcessingElements;
import schedframe.resources.units.ResourceUnit;
import schedframe.resources.units.ResourceUnitName;
import schedframe.resources.units.StandardResourceUnitName;
import schedframe.scheduling.manager.resources.ClusterResourceManager;
import schedframe.scheduling.manager.resources.ResourceManager;
import schedframe.scheduling.manager.tasks.JobRegistry;
import schedframe.scheduling.plan.SchedulingPlanInterface;
import schedframe.scheduling.plan.impl.SchedulingPlan;
import schedframe.scheduling.plugin.grid.ModuleList;
import schedframe.scheduling.queue.TaskQueue;
import schedframe.scheduling.queue.TaskQueueList;
import schedframe.scheduling.tasks.TaskInterface;

public class FCFSBF_ConsolidationClusterPlugin extends BaseLocalSchedulingPlugin {

	public FCFSBF_ConsolidationClusterPlugin () {
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
			// our tasks are placed only in first queue (see
			// BaseLocalSchedulingPlugin.placeJobsInQueues() method)
			TaskQueue q = queues.get(0);
			// check all tasks in queue

			for (int i = 0; i < q.size(); i++) {
				TaskInterface<?> task = q.get(i);
				// if status of the tasks in READY
				if (task.getStatus() == DCWormsTags.READY) {

					Map<ResourceUnitName, ResourceUnit> choosenResources = null;
					try {
						choosenResources = chooseResourcesForExecution(resourceManager, task);
					} catch (NoSuchFieldException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (choosenResources  != null) {
						addToSchedulingPlan(plan, task, choosenResources);
					} 
				}
			}

			break;
		}
		return plan;
	}
	
	private Map<ResourceUnitName, ResourceUnit> chooseResourcesForExecution(ClusterResourceManager resourceManager, TaskInterface<?> task) throws NoSuchFieldException {

		List<ComputingNode> nodes = resourceManager.getComputingNodes();
		nodes = findSuitableNodes(task, nodes);
		Collections.sort(nodes, new Comparator<ComputingNode>(){
		    public int compare(ComputingNode node1, ComputingNode node2){   
		        return node1.getResourceCharacteristic().getParameters().get("category").get(0).getContent().compareTo(node2.getResourceCharacteristic().getParameters().get("category").get(0).getContent());
		    }
		});
		if(nodes.size() > 0)
		{
			ComputingNode node = nodes.get(0);
			Map<ResourceUnitName, ResourceUnit> map = new HashMap<ResourceUnitName, ResourceUnit>();
			List<ComputingResource> choosenResources =  new ArrayList<ComputingResource>();
			int cpuRequest;
			try {
				cpuRequest = Double.valueOf(task.getCpuCntRequest()).intValue();
			} catch (NoSuchFieldException e) {
				cpuRequest = 0;
			}
			for (int i = 0; i < node.getProcessors().size() && cpuRequest > 0; i++) {
				if (node.getProcessors().get(i).getStatus() == ResourceStatus.FREE) {
					choosenResources.add(node.getProcessors().get(i));
					cpuRequest--;
				}
			}
			ProcessingElements result = new ProcessingElements(node.getName());
			result.addAll(choosenResources);
			map.put(StandardResourceUnitName.PE, result);
			
			int memoryRequest;
			try {
				memoryRequest = Double.valueOf(task.getMemoryRequest()).intValue();
			} catch (NoSuchFieldException e) {
				memoryRequest = 0;
			}
			if (memoryRequest != 0) {
				Memory memory = new Memory(node.getMemory(), memoryRequest, memoryRequest);
				map.put(StandardResourceUnitName.MEMORY, memory);
			}
			return map;
		} else 
			return null;
	}
	
	private List<ComputingNode> findSuitableNodes(TaskInterface<?> task, List<ComputingNode> nodes) throws NoSuchFieldException{
		int cpuRequest;
		try {
			cpuRequest = Double.valueOf(task.getCpuCntRequest()).intValue();
		} catch (NoSuchFieldException e) {
			cpuRequest = 1;
		}
		int memoryRequest;
		try {
			memoryRequest = Double.valueOf(task.getMemoryRequest()).intValue();
		} catch (NoSuchFieldException e) {
			memoryRequest = 0;
		}
		List<ComputingNode> suitableNodes = new ArrayList<ComputingNode>();
		for(ComputingNode node: nodes){
			if(node.getFreeProcessorsNumber() >= cpuRequest && node.getFreeMemory() >= memoryRequest){
				suitableNodes.add(node);
			}
		}
		return suitableNodes;
	}

}
