package model.scheduling.policy;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTimeUtilsExt;


import model.PluginConfiguration;
import model.events.scheduling.SchedulingEventType;
import model.scheduling.Scheduler;import model.scheduling.WorkloadUnitHandler;
import model.scheduling.manager.resources.ManagedResources;
import model.scheduling.manager.resources.ResourceManager;
import model.scheduling.manager.resources.ResourceManagerFactory;
import model.scheduling.manager.tasks.JobRegistry;
import model.scheduling.manager.tasks.JobRegistryImpl;
import model.scheduling.plan.AllocationInterface;
import model.scheduling.plan.SchedulingPlanInterface;
import model.scheduling.plugin.SchedulingPlugin;
import model.scheduling.plugin.estimation.ExecutionTimeEstimationPlugin;
import model.scheduling.plugin.grid.ModuleList;
import model.scheduling.plugin.local.ResourceAllocation;
import model.scheduling.queue.TaskQueue;
import model.scheduling.queue.TaskQueueList;
import model.scheduling.tasks.Job;
import model.scheduling.tasks.TaskInterface;
import model.scheduling.tasks.WorkloadUnit;
import eduni.simjava.Sim_event;
import gridsim.DCWormsConstants;
import gridsim.DCWormsTags;
import gridsim.GridSim;
import gridsim.GridSimTags;
import gridsim.IO_data;
import gridsim.schedframe.ExecTask;
import gridsim.schedframe.Executable;
import gridsim.schedframe.queues.AbstractStatsSupportingQueue;

public abstract class AbstractManagementSystem {

	private Log log = LogFactory.getLog(AbstractManagementSystem.class);
	
	protected String name;

	protected TaskQueueList queues;
	protected ResourceManager resourceManager;
	protected JobRegistryImpl jobRegistry;
	protected ModuleList moduleList;
	
	protected SchedulingPlugin schedulingPlugin;
	
	protected ExecutionTimeEstimationPlugin execTimeEstimationPlugin;

	protected Scheduler scheduler;
	

	public AbstractManagementSystem(String providerId, String entityName,
			ExecutionTimeEstimationPlugin execTimeEstPlugin, TaskQueueList queues) {
		
		//this.name = entityName + "@" + providerId;
		this.name = providerId;
		this.queues = queues;
		this.jobRegistry = new JobRegistryImpl(name);
		this.execTimeEstimationPlugin = execTimeEstPlugin;
	}
	
	public void init(Scheduler sched, ManagedResources managedResources) {
		scheduler = sched;
		resourceManager = ResourceManagerFactory.createResourceManager(scheduler, managedResources);
		scheduler.set_stat(DCWormsConstants.getResourcesStatisticsObject(queues.size()));
		for(int i = 0; i < queues.size(); i++){
			TaskQueue q = queues.get(i);
			if(q instanceof AbstractStatsSupportingQueue<?>){
				AbstractStatsSupportingQueue<?> queue = (AbstractStatsSupportingQueue<?>) q;
				queue.setStats(scheduler.get_stat(), DCWormsConstants.TASKS_QUEUE_LENGTH_MEASURE_NAME + "_" + Integer.toString(i));
			}
		}
	}
	
	public void processEvent(Sim_event ev) {
		processOtherEvent(ev);
	}

	protected void processOtherEvent(Sim_event ev) {
		if (ev == null) {
			System.out.println(name + ".processOtherEvent(): " + "Error - an event is null.");
			return;
		}

		log.error(name + ".processOtherEvent(): Unable to "
				+ "handle request from an event with a tag number " + ev.get_tag());
	}
	
	
	public String getName() {
		return name;
	}
	
	public ResourceManager getResourceManager() {
		if (resourceManager instanceof ResourceManager)
			return (ResourceManager) resourceManager;
		else
			return null;
	}

	public ResourceAllocation getAllocationManager() {
		if (resourceManager instanceof ResourceAllocation)
			return (ResourceAllocation) resourceManager;
		else
			return null;
	}

	protected JobRegistry getJobRegistry(){
		return jobRegistry;
	}
	
	public Scheduler getScheduler() {
		return scheduler;
	}
	
	public PluginConfiguration getSchedulingPluginConfiguration() {
		return schedulingPlugin.getConfiguration();
	}
	
	public boolean pluginSupportsEvent(int eventType){
		return true;
	}

	public TaskQueueList getQueues(){
		return queues;
	}
	
	public Map<String, Integer> getQueuesSize() {
		Map<String, Integer> queue_size = new HashMap<String, Integer>();
		for (TaskQueue queue : queues) {
			queue_size.put(queue.getName(), queue.size());
		}
		return queue_size;
	}
	
	//POPRAWIC  (ale co? bo teraz chyba jest ok)
	protected void submitTask(TaskInterface<?> task, AllocationInterface<?> allocation) {
		String providerName = allocation.getProviderName();
		if (providerName == null) {
			return;
		}
		removeFromQueue(task);
		scheduler.send(providerName, GridSimTags.SCHEDULE_NOW, GridSimTags.GRIDLET_SUBMIT, task);	
	}

	protected boolean sendFinishedWorkloadUnit(WorkloadUnit wu) {
		
		Executable exec = (Executable) wu;
		if(scheduler.getParent() == null)
		{
			Job job = jobRegistry.getJob(exec.getJobId());

			if(job.isFinished()){
				scheduler.send(job.getSenderId(), GridSimTags.SCHEDULE_NOW, GridSimTags.GRIDLET_RETURN, job);
				return true;
			}
			else return true;
		}
	
        IO_data obj = new IO_data(exec, 0, /*task.getGridletOutputSize(),*/ GridSim.getEntityId(scheduler.getParent().get_name()));
        scheduler.send(scheduler.getOutputPort(), 0.0, GridSimTags.GRIDLET_RETURN, obj);
		return true;
	}

	protected void sendExecutableReadyEvent(ExecTask exec) {

		long delay = 0;
		try {
			long expectedStartTime = exec.getExecutionStartTime().getMillis() / 1000;
			long currentTime = DateTimeUtilsExt.currentTimeMillis() / 1000;
			delay = expectedStartTime - currentTime;
			if (delay < 0)
				delay = 0;
		} catch (NoSuchFieldException e) {
			delay = 0;
		}

		scheduler.sendInternal(Long.valueOf(delay).doubleValue(), DCWormsTags.TASK_READY_FOR_EXECUTION,
				exec);
	}
	
	protected void sendTimerEvent() {
		PluginConfiguration pluginConfig = schedulingPlugin.getConfiguration();
		if (pluginConfig != null) {
			Map<SchedulingEventType, Object> events = pluginConfig.getServedEvents();
			if (events != null) {
				Object obj = events.get(SchedulingEventType.TIMER);
				if (obj != null) {
					int delay = (Integer) obj;
					scheduler.sendInternal(delay, DCWormsTags.TIMER, null);
				}
			}
		}
	}
	
	protected boolean removeFromQueue(TaskInterface<?> task) {
		for(TaskQueue queue : queues){
			if(queue.contains(task)){
				queue.remove(task);
				return true;
			}
		}
		return false;
	}
	
	public SchedulingPlugin getSchedulingPlugin() {
		return schedulingPlugin;
	}

	public void setSchedulingPlugin(SchedulingPlugin schedulingPlugin) {
		this.schedulingPlugin = schedulingPlugin;
	}
	
	public abstract WorkloadUnitHandler getWorkloadUnitHandler();
	
	public abstract void notifySubmittedWorkloadUnit(WorkloadUnit wu, boolean ack);

	public abstract void notifyReturnedWorkloadUnit(WorkloadUnit wu);

	protected abstract void executeSchedulingPlan(SchedulingPlanInterface<?> decision);

}
