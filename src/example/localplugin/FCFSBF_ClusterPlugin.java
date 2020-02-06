package example.localplugin;

import gridsim.dcworms.DCWormsTags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import schedframe.events.scheduling.SchedulingEvent;
import schedframe.resources.ResourceStatus;
import schedframe.resources.StandardResourceType;
import schedframe.resources.computing.ComputingNode;
import schedframe.resources.computing.ComputingResource;
import schedframe.resources.computing.Processor;
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

public class FCFSBF_ClusterPlugin extends BaseLocalSchedulingPlugin {

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
			// our tasks are placed only in first queue (see BaseLocalSchedulingPlugin.placeJobsInQueues() method)
			TaskQueue q = queues.get(0);
			// check all tasks in queue

			for (int i = 0; i < q.size(); i++) {
				TaskInterface<?> task = q.get(i);
				// if status of the tasks in READY
				if (task.getStatus() == DCWormsTags.READY) {
					
					/****************3 ways to schedule task****************/
					
					/****************1. Choosing particular resources to perform execution****************/
					Map<ResourceUnitName, ResourceUnit> choosenResources = chooseResourcesForExecution(resourceManager, task);
					if (choosenResources != null) {
						addToSchedulingPlan(plan, task, choosenResources);
					}
					
					/****************2. Choosing resource scheduler/provider to submit the task. If the given resource doesn't contains/isn't 
					a scheduler, random resources from the given resource will be chosen in order to perform execution****************/
					/*String provName = chooseProviderForExecution(resourceManager);
					if (provName != null) {
						addToSchedulingPlan(plan, task, provName);
					}*/

					/****************3. Scheduler will choose random resources to perform execution****************/
					//addToSchedulingPlan(plan, task);
				}
			}

			break;
		}
		return plan;
	}

	private Map<ResourceUnitName, ResourceUnit> chooseResourcesForExecution(
			ClusterResourceManager resourceManager, TaskInterface<?> task) {

		Map<ResourceUnitName, ResourceUnit> map = new HashMap<ResourceUnitName, ResourceUnit>();
		List<ComputingNode> nodes = resourceManager.getComputingNodes();
		for (ComputingNode node : nodes) {
			int cpuRequest;
			try {
				cpuRequest = Double.valueOf(task.getCpuCntRequest()).intValue();
			} catch (NoSuchFieldException e) {
				cpuRequest = 0;
			}

			if (cpuRequest != 0) {

				List<Processor> processors = node.getProcessors();
				if (processors.size() < cpuRequest) {
					continue;
				}

				List<ComputingResource> choosenResources = new ArrayList<ComputingResource>();				
				for (int i = 0; i < processors.size() && cpuRequest > 0; i++) {
					if (processors.get(i).getStatus() == ResourceStatus.FREE) {
						choosenResources.add(processors.get(i));
						cpuRequest--;
					}
				}
				if (cpuRequest > 0) {
					continue;
				}

				ProcessingElements pe = new ProcessingElements();
				pe.addAll(choosenResources);
				map.put(StandardResourceUnitName.PE, pe);
				
				int memoryRequest;
				try {
					memoryRequest = Double.valueOf(task.getMemoryRequest()).intValue();
				} catch (NoSuchFieldException e) {
					memoryRequest = 0;
				}
				if (memoryRequest != 0) {

					Memory memory = null;
					try{
						if (node.getFreeMemory() >= memoryRequest) {		
							memory = new Memory(node.getMemory(), memoryRequest, memoryRequest);
						}		
					} catch(NoSuchFieldException e){
						continue;
					}
					if(memory == null)
						continue;
					else {
						map.put(StandardResourceUnitName.MEMORY, memory);
						return map;
					}
				} else return map;
			}
		}
	
		return null;
	}

	@SuppressWarnings("unchecked")
	private String chooseProviderForExecution(ResourceManager unitsManager) {
		List<ComputingResource> nodes;
		Properties properties = new Properties();
		properties.setProperty("type", StandardResourceType.ComputingNode.getName());
		// properties.setProperty("status", ResourceStatus.FREE.toString());
		nodes = (List<ComputingResource>) unitsManager.filterResources(properties);
		
		//always return the same node from the list
		return nodes.get(0).getName();
	}

}
