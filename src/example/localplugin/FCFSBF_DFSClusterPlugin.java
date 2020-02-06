package example.localplugin;

import gridsim.dcworms.DCWormsTags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import schedframe.events.scheduling.SchedulingEvent;
import schedframe.resources.ResourceStatus;
import schedframe.resources.computing.ComputingResource;
import schedframe.resources.computing.Processor;
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

public class FCFSBF_DFSClusterPlugin extends BaseLocalSchedulingPlugin {

	public SchedulingPlanInterface<?> schedule(SchedulingEvent event, TaskQueueList queues, JobRegistry jobRegistry,
			ResourceManager resManager, ModuleList modules) {

		ClusterResourceManager resourceManager = (ClusterResourceManager) resManager;
		SchedulingPlan plan = new SchedulingPlan();
		// our tasks are placed only in first queue (see
		// BaseLocalSchedulingPlugin.placeJobsInQueues() method)
		TaskQueue q = queues.get(0);
		// choose the events types to serve.
		// Different actions for different events are possible.
		switch (event.getType()) {
		
		case START_TASK_EXECUTION:
		case TASK_FINISHED:
			// check all tasks in queue
			for (int i = 0; i < q.size(); i++) {
				TaskInterface<?> task = q.get(i);
				// if status of the tasks in READY
				if (task.getStatus() == DCWormsTags.READY) {

					Map<ResourceUnitName, ResourceUnit> choosenResources = chooseResourcesForExecution(resourceManager, task);
					if (choosenResources  != null) {
						addToSchedulingPlan(plan, task, choosenResources);
					} 
				}
			}
			adjustFrequency(resourceManager.getProcessors());
		}
		return plan;
	}
	
	private Map<ResourceUnitName, ResourceUnit> chooseResourcesForExecution(
			ClusterResourceManager resourceManager, TaskInterface<?> task) {

		Map<ResourceUnitName, ResourceUnit> map = new HashMap<ResourceUnitName, ResourceUnit>();

		int cpuRequest;
		try {
			cpuRequest = Double.valueOf(task.getCpuCntRequest()).intValue();
		} catch (NoSuchFieldException e) {
			cpuRequest = 0;
		}

		if (cpuRequest != 0) {
			List<ComputingResource> choosenResources = null;
			List<Processor> processors = resourceManager.getProcessors();
			if (processors.size() < cpuRequest) {
				// log.warn("Task requires more cpus than is availiable in this resource.");
				return null;
			}

			choosenResources = new ArrayList<ComputingResource>();

			for (int i = 0; i < processors.size() && cpuRequest > 0; i++) {
				if (processors.get(i).getStatus() == ResourceStatus.FREE) {
					choosenResources.add(processors.get(i));
					cpuRequest--;
				}
			}
			if (cpuRequest > 0) {
				// log.info("Task " + task.getJobId() + "_" + task.getId() +
				// " requires more cpus than is availiable in this moment.");
				return null;
			}

			ProcessingElements pe = new ProcessingElements();
			pe.addAll(choosenResources);
			map.put(StandardResourceUnitName.PE, pe);
		}
		return map;
	}
	
	private void adjustFrequency(List<Processor> processors){

		for(Processor cpu: processors){
			if(cpu.getStatus() == ResourceStatus.FREE) {
				if(cpu.getPowerInterface().getSupportedPStates().containsKey("P3"))
					cpu.getPowerInterface().setPState("P3");
			}
			else{
				if(cpu.getPowerInterface().getSupportedPStates().containsKey("P0"))
					cpu.getPowerInterface().setPState("P0");
			}
		}
	}

}