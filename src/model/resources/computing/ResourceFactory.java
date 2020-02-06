package model.resources.computing;

import gridsim.DCWormsConstants;
import model.resources.StandardResourceType;
import model.resources.UserResourceType;
import model.resources.computing.description.ComputingResourceDescription;
import model.resources.computing.recs.ComputeBox1;
import model.resources.computing.recs.Node;
import model.resources.computing.recs.NodeGroup;
import model.scheduling.Scheduler;
import model.scheduling.manager.resources.ManagedResources;
import model.scheduling.plugin.SchedulingPlugin;
import model.scheduling.plugin.estimation.ExecutionTimeEstimationPlugin;
import model.scheduling.policy.AbstractManagementSystem;
import model.scheduling.policy.global.GridBroker;
import model.scheduling.policy.local.LocalManagementSystem;
import model.scheduling.queue.TaskQueueList;


public class ResourceFactory {

	public static ComputingResource createResource(ComputingResourceDescription resDesc){
		
		if (resDesc.getType().equals(StandardResourceType.DataCenter))
			return new DataCenter(resDesc);
		else if (resDesc.getType().equals(StandardResourceType.Rack))
			return new Rack(resDesc);
		else if (resDesc.getType().equals(StandardResourceType.ComputingNode))
			return new ComputingNode(resDesc);
		else if (resDesc.getType().equals(StandardResourceType.Processor))
			return new Processor(resDesc);
		else if (resDesc.getType().equals(StandardResourceType.Core))
			return new Core(resDesc);
		else if (resDesc.getType().getName().equals(new UserResourceType("ComputeBox1").getName()))
			return new ComputeBox1(resDesc);
		else if (resDesc.getType().getName().equals(new UserResourceType("NodeGroup").getName()))
			return new NodeGroup(resDesc);
		else if (resDesc.getType().getName().equals(new UserResourceType("Node").getName()))
			return new Node(resDesc);
		else
			return new ComputingResource(resDesc);
	
		/*switch(resDesc.getType()){
			case Grid: return new Grid(resDesc);
			case DataCenter: return new DataCenter(resDesc);
			case ComputingNode: return new ComputingNode(resDesc);
			case Processor: return new Processor(resDesc);
		default:
			return new ComputingResource(resDesc);
		}*/
	}

	public static Scheduler createScheduler(StandardResourceType type, String id, SchedulingPlugin schedulingPlugin, ExecutionTimeEstimationPlugin execTimeEstimationPlugin, TaskQueueList queues, ManagedResources managedResources) throws Exception{
		AbstractManagementSystem ms;
		switch(type){
			case GS: {
				ms = new GridBroker("grid",
						schedulingPlugin, execTimeEstimationPlugin, queues);
				return new Scheduler(ms, type, managedResources);
			}
			case LS: {
				ms = new LocalManagementSystem(id, DCWormsConstants.MANAGEMENT_SYSTEM,
						schedulingPlugin, execTimeEstimationPlugin, queues);
				return new Scheduler(ms, type, managedResources);
			}

		default:{
				ms = new LocalManagementSystem(id, DCWormsConstants.MANAGEMENT_SYSTEM,
						schedulingPlugin, execTimeEstimationPlugin, queues);
				return new Scheduler(ms, type, managedResources);
			}
		}
	}
}

