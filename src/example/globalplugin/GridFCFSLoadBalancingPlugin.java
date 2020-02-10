package example.globalplugin;

import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import model.events.scheduling.SchedulingEvent;
import model.scheduling.SchedulerDescription;
import model.scheduling.manager.resources.ResourceManager;
import model.scheduling.manager.tasks.JobRegistry;
import model.scheduling.plan.SchedulingPlanInterface;
import model.scheduling.plan.impl.Allocation;
import model.scheduling.plan.impl.ScheduledTask;
import model.scheduling.plan.impl.SchedulingPlan;
import model.scheduling.plugin.grid.Module;
import model.scheduling.plugin.grid.ModuleList;
import model.scheduling.plugin.grid.ResourceDiscovery;
import model.scheduling.queue.QueueDescription;
import model.scheduling.queue.TaskQueue;
import model.scheduling.queue.TaskQueueList;
import model.scheduling.tasks.TaskInterface;
import model.scheduling.tasks.WorkloadUnit;

public class GridFCFSLoadBalancingPlugin extends BaseGlobalPlugin {

	private Log log = LogFactory.getLog(GridFCFSLoadBalancingPlugin.class);

	public SchedulingPlanInterface<?> schedule(SchedulingEvent event, TaskQueueList queues, JobRegistry jobRegistry,
			ResourceManager resourceManager, ModuleList modules) {

		ResourceDiscovery resources = null;
		for (int i = 0; i < modules.size(); i++) {
			Module m = modules.get(i);
			switch (m.getType()) {
			case RESOURCE_DISCOVERY:
				resources = (ResourceDiscovery) m;
				break;
			}
		}

		SchedulingPlan plan = new SchedulingPlan();

		TaskQueue q = queues.get(0);
		int size = q.size();

		// order of the resources on this list is not determined
		List<SchedulerDescription> availableResources = resources.getResources();
		int resourceIdx = -1;

		for (int i = 0; i < size; i++) {
			WorkloadUnit job = q.remove(0);
			TaskInterface<?> task = (TaskInterface<?>) job;

			resourceIdx = findLeastLoadedResourceIdx(availableResources);
			SchedulerDescription sd = availableResources.get(resourceIdx);

			Allocation allocation = new Allocation();
			allocation.setProcessesCount(1);
			allocation.setProviderName(sd.getProvider().getProviderId());

			ScheduledTask scheduledTask = new ScheduledTask(task);
			scheduledTask.setTaskId(task.getId());
			scheduledTask.setJobId(task.getJobId());
			scheduledTask.addAllocation(allocation);

			plan.addTask(scheduledTask);
		}
		return plan;

	}

	private int findLeastLoadedResourceIdx(List<SchedulerDescription> availableResources) {
		int resourceIdx = -1;
		long minLoad = Long.MAX_VALUE;

		for (int i = 0; i < availableResources.size(); i++) {
			SchedulerDescription sd = availableResources.get(i);
			long totalLoad = getQueueLoad(sd.getAvailableQueues());
			if (totalLoad < minLoad) {
				resourceIdx = i;
				minLoad = totalLoad;
			}
		}
		return resourceIdx;
	}

	private long getQueueLoad(List<QueueDescription> queues) {
		long load = 0;
		for (QueueDescription queue : queues) {
			load = load + queue.getLoad();
		}
		return load;
	}

}
