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
import schedframe.resources.computing.profiles.energy.power.StandardPowerStateName;
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

public class FCFSBF_NodePowerManagementClusterPlugin extends BaseLocalSchedulingPlugin {

	public FCFSBF_NodePowerManagementClusterPlugin () {
	}

	public SchedulingPlanInterface<?> schedule(SchedulingEvent event, TaskQueueList queues, JobRegistry jobRegistry,
			ResourceManager resManager, ModuleList modules) {

		ClusterResourceManager resourceManager = (ClusterResourceManager) resManager;
		SchedulingPlan plan = new SchedulingPlan();

		switch (event.getType()) {
		case START_TASK_EXECUTION:
		case TASK_FINISHED:

			TaskQueue q = queues.get(0);

			for (int i = 0; i < q.size(); i++) {
				TaskInterface<?> task = q.get(i);
				if (task.getStatus() == DCWormsTags.READY) {

					Map<ResourceUnitName, ResourceUnit> choosenResources = chooseResourcesForExecution(resourceManager, task);
					if (choosenResources  != null) {
						addToSchedulingPlan(plan, task, choosenResources);
					} else {
						if(harnessIdleNodesToWork(task, resourceManager.getComputingNodes()))
							i--;
					}
				}
			}
			turnOffIdleNodes(resourceManager.getComputingNodes());
			break;
		}
		return plan;
	}
	
	private Map<ResourceUnitName, ResourceUnit> chooseResourcesForExecution(ClusterResourceManager resourceManager, TaskInterface<?> task) {

		List<ComputingNode> nodes = resourceManager.getComputingNodes();
		nodes = findSuitableNodes(task, nodes);
		Collections.sort(nodes, new Comparator<ComputingNode>(){
		    public int compare(ComputingNode node1, ComputingNode node2){    
		        return node1.getResourceCharacteristic().getParameters().get("category").get(0).getContent().compareTo(node2.getResourceCharacteristic().getParameters().get("category").get(0).getContent());
		    }
		});
		if(nodes.size() > 0)
		{
			Map<ResourceUnitName, ResourceUnit> map = new HashMap<ResourceUnitName, ResourceUnit>();
			List<ComputingResource> choosenResources =  new ArrayList<ComputingResource>();
			int cpuRequest;
			try {
				cpuRequest = Double.valueOf(task.getCpuCntRequest()).intValue();
			} catch (NoSuchFieldException e) {
				cpuRequest = 0;
			}
			for (int i = 0; i < nodes.get(0).getProcessors().size() && cpuRequest > 0; i++) {
				if (nodes.get(0).getProcessors().get(i).getStatus() == ResourceStatus.FREE) {
					choosenResources.add(nodes.get(0).getProcessors().get(i));
					cpuRequest--;
				}
			}
			ProcessingElements result = new ProcessingElements(nodes.get(0).getName());
			result.addAll(choosenResources);
			map.put(StandardResourceUnitName.PE, result);
			return map;
		} else 
			return null;
	}
	
	private List<ComputingNode> findSuitableNodes(TaskInterface<?> task, List<ComputingNode> nodes){
		int cpuRequest;
		try {
			cpuRequest = Double.valueOf(task.getCpuCntRequest()).intValue();
		} catch (NoSuchFieldException e) {
			cpuRequest = 1;
		}
		List<ComputingNode> avNodes = new ArrayList<ComputingNode>();
		for(ComputingNode node: nodes){
			if(node.getFreeProcessorsNumber() >= cpuRequest){
				avNodes.add(node);
			}
		}
		return avNodes;
	}

	private void turnOffIdleNodes(List<ComputingNode> nodes){
		for(ComputingNode node : nodes){
			if(node.getFreeProcessorsNumber() == node.getProcessorsNumber()){
				node.getPowerInterface().setPowerState(StandardPowerStateName.OFF);
			}
		}
	}
	
	private boolean harnessIdleNodesToWork(TaskInterface<?> task, List<ComputingNode> nodes){
		int cpuRequest;
		try {
			cpuRequest = Double.valueOf(task.getCpuCntRequest()).intValue();
		} catch (NoSuchFieldException e) {
			cpuRequest = 1;
		}
		Collections.sort(nodes, new Comparator<ComputingNode>(){
		    public int compare(ComputingNode node1, ComputingNode node2){    
		        return node1.getResourceCharacteristic().getParameters().get("category").get(0).getContent().compareTo(node2.getResourceCharacteristic().getParameters().get("category").get(0).getContent());
		    }
		});
		for (int i = 0; i < nodes.size() && cpuRequest > 0; i++) {
			ComputingNode node = nodes.get(i);
			if(node.getPowerInterface().getPowerState() == StandardPowerStateName.OFF){
				node.getPowerInterface().setPowerState(StandardPowerStateName.ON);
				cpuRequest -= node.getProcessorsNumber();
			}
		}
		return cpuRequest > 0 ? false : true;
	}

}