package model.scheduling.policy.local;

import eduni.simjava.Sim_event;
import eduni.simjava.Sim_system;
import gridsim.Accumulator;
import gridsim.DCWormsConstants;
import gridsim.DCWormsTags;
import gridsim.ExecTaskFilter;
import gridsim.ResourceCalendar;
import gridsim.schedframe.ExecTask;
import gridsim.schedframe.Executable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtilsExt;
import org.qcg.broker.schemas.schedulingplan.types.AllocationStatus;

import qcg.shared.constants.BrokerConstants;
import controller.ResourceController;
import controller.simulator.utils.DoubleMath;
import model.events.scheduling.SchedulingEvent;
import model.events.scheduling.SchedulingEventType;
import model.events.scheduling.StartTaskExecutionEvent;
import model.events.scheduling.TaskFinishedEvent;
import model.events.scheduling.TaskRequestedTimeExpiredEvent;
import model.exceptions.ResourceException;
import model.resources.computing.ComputingResource;
import model.resources.computing.profiles.energy.EnergyEvent;
import model.resources.computing.profiles.energy.EnergyEventType;
import model.resources.units.PEUnit;
import model.resources.units.ProcessingElements;
import model.resources.units.ResourceUnit;
import model.resources.units.ResourceUnitName;
import model.resources.units.StandardResourceUnitName;
import model.scheduling.ResourceHistoryItem;
import model.scheduling.Scheduler;
import model.scheduling.TaskList;
import model.scheduling.TaskListImpl;
import model.scheduling.UsedResourcesList;
import model.scheduling.WorkloadUnitHandler;
import model.scheduling.manager.resources.LocalResourceManager;
import model.scheduling.manager.resources.ManagedResources;
import model.scheduling.manager.resources.ResourceManager;
import model.scheduling.plan.AllocationInterface;
import model.scheduling.plan.ScheduledTaskInterface;
import model.scheduling.plan.SchedulingPlanInterface;
import model.scheduling.plugin.SchedulingPlugin;
import model.scheduling.plugin.estimation.ExecutionTimeEstimationPlugin;
import model.scheduling.plugin.grid.ModuleListImpl;
import model.scheduling.plugin.grid.ModuleType;
import model.scheduling.policy.AbstractManagementSystem;
import model.scheduling.queue.TaskQueueList;
import model.scheduling.tasks.AbstractProcesses;
import model.scheduling.tasks.Job;
import model.scheduling.tasks.JobInterface;
import model.scheduling.tasks.Task;
import model.scheduling.tasks.TaskInterface;
import model.scheduling.tasks.WorkloadUnit;

public class LocalManagementSystem extends AbstractManagementSystem {

	private Log log = LogFactory.getLog(LocalManagementSystem.class);

	protected double lastUpdateTime;

	protected Accumulator accTotalLoad;

	public LocalManagementSystem(String providerId, String entityName, SchedulingPlugin schedPlugin,
			ExecutionTimeEstimationPlugin execTimeEstimationPlugin, TaskQueueList queues)
			throws Exception {

		super(providerId, entityName, execTimeEstimationPlugin, queues);
		
		if (schedPlugin == null) {
			throw new Exception("Can not create local scheduling plugin instance.");
		}
		this.schedulingPlugin =  schedPlugin;
		this.moduleList = new ModuleListImpl(1);
		
		this.accTotalLoad = new Accumulator();
	}

	public void init(Scheduler sched, ManagedResources managedResources) {
		super.init(sched, managedResources);
		double load = 0;
		accTotalLoad.add(load);
	}

	public void processEvent(Sim_event ev) {

		updateProcessingProgress();

		int tag = ev.get_tag();

		switch (tag) {

		case DCWormsTags.TIMER:
			if (pluginSupportsEvent(tag)) {
				SchedulingEvent event = new SchedulingEvent(SchedulingEventType.TIMER);
				SchedulingPlanInterface<?> decision =  schedulingPlugin.schedule(event, 
						queues,  getJobRegistry(), getResourceManager(), moduleList);
				executeSchedulingPlan(decision);
			}
			sendTimerEvent();
			break;

		case DCWormsTags.TASK_READY_FOR_EXECUTION:
			ExecTask execTask = (ExecTask) ev.get_data();
			try {
				execTask.setStatus(DCWormsTags.READY);
				if (pluginSupportsEvent(tag)) {
					SchedulingEvent event = new StartTaskExecutionEvent(execTask.getJobId(), execTask.getId());
					SchedulingPlanInterface<?> decision =  schedulingPlugin.schedule(event,
							queues,  getJobRegistry(), getResourceManager(), moduleList);
					executeSchedulingPlan(decision);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case DCWormsTags.TASK_EXECUTION_FINISHED:
			execTask = (ExecTask) ev.get_data();
			if (execTask.getStatus() == DCWormsTags.INEXEC) {
				
				finalizeExecutable(execTask);
				sendFinishedWorkloadUnit(execTask);
				log.debug(execTask.getJobId() + "_" + execTask.getId() + " finished execution on " + new DateTime());
//				log.info(DCWormsConstants.USAGE_MEASURE_NAME + ": " + calculateTotalLoad(jobRegistry.getRunningTasks().size()));
//				System.out.println(DCWormsConstants.USAGE_MEASURE_NAME + ": " + calculateTotalLoad(jobRegistry.getRunningTasks().size()));
				if (pluginSupportsEvent(tag)) {
					SchedulingEvent event = new TaskFinishedEvent(execTask.getJobId(), execTask.getId());
					SchedulingPlanInterface<?> decision = schedulingPlugin.schedule(event,
							queues, getJobRegistry(), getResourceManager(), moduleList);
					executeSchedulingPlan(decision);
				}
			}

			Job job = jobRegistry.getJob(execTask.getJobId());
			if(!job.isFinished()){
				getWorkloadUnitHandler().handleJob(job);
			}
			break;
			
		case DCWormsTags.TASK_REQUESTED_TIME_EXPIRED:
			execTask = (Executable) ev.get_data();
			if (pluginSupportsEvent(tag)) {
				SchedulingEvent event = new TaskRequestedTimeExpiredEvent(execTask.getJobId(), execTask.getId());
				SchedulingPlanInterface<?> decision = schedulingPlugin.schedule(event,
						queues, getJobRegistry(), getResourceManager(), moduleList);
				executeSchedulingPlan(decision);
			}
			break;
			
		case DCWormsTags.UPDATE_PROCESSING:
			updateProcessingTimes(ev);
			break;
			
		case DCWormsTags.PHASE_CHANGED:
			execTask = (ExecTask) ev.get_data();
			//updatePhases(execTask);
			break;
		}
	}

	public void notifyReturnedWorkloadUnit(WorkloadUnit wu) {
		if (pluginSupportsEvent(DCWormsTags.TASK_EXECUTION_FINISHED)) {
			SchedulingEvent event = new SchedulingEvent(SchedulingEventType.TASK_FINISHED);
			SchedulingPlanInterface<?> decision =  schedulingPlugin.schedule(event,
					queues, getJobRegistry(), getResourceManager(), moduleList);
			executeSchedulingPlan(decision);
		}
		//if(scheduler.getParent() != null){
			sendFinishedWorkloadUnit(wu);
		//}
	}
	
	protected void executeSchedulingPlan(SchedulingPlanInterface<?> decision) {

		ArrayList<ScheduledTaskInterface<?>> taskSchedulingDecisions = decision.getTasks();
		for (int i = 0; i < taskSchedulingDecisions.size(); i++) {
			ScheduledTaskInterface<?> taskDecision = taskSchedulingDecisions.get(i);

			if (taskDecision.getStatus() == AllocationStatus.REJECTED) {
				continue;
			}

			ArrayList<AllocationInterface<?>> allocations = taskDecision.getAllocations();

			TaskInterface<?> task = taskDecision.getTask();
			for (int j = 0; j < allocations.size(); j++) {

				AllocationInterface<?> allocation = allocations.get(j);
				if (allocation.getRequestedResources() == null || allocation.getRequestedResources().size() > 0) {
					ExecTask exec = (ExecTask) task;					
					executeTask(exec, allocation.getRequestedResources());
				} else if(resourceManager.getSchedulerName(allocation.getProviderName()) != null){
					allocation.setProviderName(resourceManager.getSchedulerName(allocation.getProviderName()));
					submitTask(task, allocation);
				} else {
					ExecTask exec = (ExecTask) task;
					executeTask(exec, chooseResourcesForExecution(allocation.getProviderName(), exec));
				}
			}
		}
	}

	protected void executeTask(ExecTask task, Map<ResourceUnitName, ResourceUnit> choosenResources) {

		Executable exec = (Executable)task;
		boolean allocationStatus = getAllocationManager().allocateResources(choosenResources);
		if(allocationStatus == false){
			log.info("Task " + task.getJobId() + "_" + task.getId() + " requires more resources than is available at this moment.");
			return;
		}

		removeFromQueue(task);

		SchedulingEvent event = new SchedulingEvent(SchedulingEventType.START_TASK_EXECUTION);
		int time = Double.valueOf(
				execTimeEstimationPlugin.execTimeEstimation(event, task, choosenResources, exec.getCompletionPercentage())).intValue();
		log.debug(task.getJobId() + "_" + task.getId() + " starts executing on " + new DateTime()
				+ " will finish after " + time);
		System.out.println(task.getJobId() + "_" + task.getId() + " starts executing on " + new DateTime()
				+ " will finish after " + time);
		if (time < 0.0)
			return;

		exec.setEstimatedDuration(time);
		DateTime currentTime = new DateTime();
		ResourceHistoryItem resHistItem = new ResourceHistoryItem(choosenResources, currentTime);
		exec.addUsedResources(resHistItem);
		try {
			exec.setStatus(DCWormsTags.INEXEC);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//updatePhases(exec);
		scheduler.sendInternal(time, DCWormsTags.TASK_EXECUTION_FINISHED, exec);

		try {
			long expectedDuration = exec.getExpectedDuration().getMillis() / 1000;
			scheduler.sendInternal(expectedDuration, DCWormsTags.TASK_REQUESTED_TIME_EXPIRED, exec);
		} catch (NoSuchFieldException e) {
			double t = exec.getEstimatedDuration();
			scheduler.sendInternal(t, DCWormsTags.TASK_REQUESTED_TIME_EXPIRED, exec);
		}

		log.info(DCWormsConstants.USAGE_MEASURE_NAME + ": " + calculateTotalLoad(jobRegistry.getRunningTasks().size()));
		
		PEUnit peUnit = (PEUnit)choosenResources.get(StandardResourceUnitName.PE);
		
		notifyComputingResources(peUnit, EnergyEventType.TASK_STARTED, exec);
		
		/*if(peUnit instanceof ProcessingElements){
			ProcessingElements pes = (ProcessingElements) peUnit;
			for (ComputingResource resource : pes) {
				resource.handleEvent(new EnergyEvent(EnergyEventType.TASK_STARTED, exec));
			}
		} else {
			ComputingResource resource = null;
			try {
				resource = ResourceController.getComputingResourceByName(peUnit.getResourceId());
			} catch (ResourceException e) {
				return;
			}
			resource.handleEvent(new EnergyEvent(EnergyEventType.TASK_STARTED, exec));
		}
*/

		/*for(ExecTaskInterface etask : jobRegistry.getRunningTasks()){
			System.out.println(etask.getJobId());
			for(String taskId: etask.getVisitedResources())
				System.out.println("====="+taskId);
		}*/
	}
	
	public void finalizeExecutable(ExecTask execTask){
		
		Executable exec = (Executable)execTask;
		exec.finalizeExecutable();
		
		ExecTaskFilter filter = new ExecTaskFilter(exec.getUniqueId(), DCWormsTags.TASK_REQUESTED_TIME_EXPIRED);
		scheduler.sim_cancel(filter, null);
		
		Task task;
		Job job = jobRegistry.getJob(exec.getJobId());
		try {
			task = job.getTask(exec.getTaskId());
		} catch (NoSuchFieldException e) {
			return;
		}
		if(exec.getProcessesId() == null){
			try {
				task.setStatus(exec.getStatus());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			List<AbstractProcesses> processesList = task.getProcesses();
			for(int i = 0; i < processesList.size(); i++){
				AbstractProcesses processes = processesList.get(i);
				if(processes.getId().equals(exec.getProcessesId())){
					processes.setStatus(exec.getStatus());
					break;
				}
			}
		}
		
		UsedResourcesList lastUsedList = exec.getUsedResources();
		Map<ResourceUnitName, ResourceUnit> lastUsed = lastUsedList.getLast()
				.getResourceUnits();
		getAllocationManager().freeResources(lastUsed);
		
		PEUnit peUnit = (PEUnit)lastUsed.get(StandardResourceUnitName.PE);
		notifyComputingResources(peUnit, EnergyEventType.TASK_FINISHED, exec);
		/*if(peUnit instanceof ProcessingElements){
			ProcessingElements pes = (ProcessingElements) peUnit;
			for (ComputingResource resource : pes) {
				resource.handleEvent(new EnergyEvent(EnergyEventType.TASK_FINISHED, exec));
			}
		} else {
			ComputingResource resource = null;
			try {
				resource = ResourceController.getComputingResourceByName(peUnit.getResourceId());
			} catch (ResourceException e) {
				return;
			}
			resource.handleEvent(new EnergyEvent(EnergyEventType.TASK_FINISHED, exec));
		}*/

		//sendFinishedWorkloadUnit(executable);
	}
	
	protected void updateProcessingProgress() {
		double timeSpan = DoubleMath.subtract(Sim_system.clock(), lastUpdateTime);
		if (timeSpan <= 0.0) {
			// don't update when nothing changed
			return;
		}
		lastUpdateTime = Sim_system.clock();
		Iterator<ExecTask> iter = jobRegistry.getRunningTasks().iterator();
		while (iter.hasNext()) {
			ExecTask task = iter.next();
			Executable exec = (Executable)task;
			//exec.setCompletionPercentage(exec.getCompletionPercentage() + 100 * timeSpan/exec.getEstimatedDuration());
			exec.setCompletionPercentage(exec.getCompletionPercentage() + (100 - exec.getCompletionPercentage()) * timeSpan/(exec.getEstimatedDuration() - new DateTime().getMillis()/1000 + exec.getExecStartTime() + timeSpan));
			
			UsedResourcesList usedResourcesList = exec.getUsedResources();
			PEUnit peUnit = (PEUnit)usedResourcesList.getLast().getResourceUnits()
					.get(StandardResourceUnitName.PE);
			double load = getMIShare(timeSpan, peUnit);
			addTotalLoad(load);
		}
	}
	
	private void notifyComputingResources(PEUnit peUnit, EnergyEventType eventType, Object obj){

		if(peUnit instanceof ProcessingElements){
			ProcessingElements pes = (ProcessingElements) peUnit;
			for (ComputingResource resource : pes) {
				resource.handleEvent(new EnergyEvent(eventType, obj));
			}
			/*try {
				for (ComputingResource resource : resourceManager.getResourcesOfType(pes.get(0).getType())) {
					resource.handleEvent(new EnergyEvent(eventType, obj));
				}
			} catch (ResourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		} else {
			ComputingResource resource = null;
			try {
				resource = ResourceController.getComputingResourceByName(peUnit.getResourceId());
			} catch (ResourceException e) {
				return;
			}
			resource.handleEvent(new EnergyEvent(eventType, obj));
		}
	}
	
	private double getMIShare(double timeSpan, PEUnit pes) {
		double localLoad;
		ResourceCalendar resCalendar = (ResourceCalendar) moduleList.getModule(ModuleType.RESOURCE_CALENDAR);
		if (resCalendar == null)
			localLoad = 0;
		else
			// 1 - localLoad_ = available MI share percentage
			localLoad = resCalendar.getCurrentLoad();

		int speed = pes.getSpeed();
		int cnt = pes.getAmount();

		double totalMI = speed * cnt * timeSpan * (1 - localLoad);
		return totalMI;
	}

	protected void updateProcessingTimes(Sim_event ev) {
		updateProcessingProgress();
		for (ExecTask execTask : jobRegistry.getRunningTasks()) {
			Executable exec = (Executable)execTask;
			List<String> visitedResource = exec.getVisitedResources();
			String originResource = ev.get_data().toString();
			if(!ArrayUtils.contains(visitedResource.toArray(new String[visitedResource.size()]), originResource)){
				continue;
			}
			
			Map<ResourceUnitName, ResourceUnit> choosenResources = exec.getUsedResources().getLast().getResourceUnits();
			int time = Double.valueOf(execTimeEstimationPlugin.execTimeEstimation(new SchedulingEvent(SchedulingEventType.RESOURCE_STATE_CHANGED), 
					execTask, choosenResources, exec.getCompletionPercentage())).intValue();

			//check if the new estimated end time is equal to the previous one; if yes the continue without update
			if(DoubleMath.subtract((exec.getExecStartTime() + exec.getEstimatedDuration()), (new DateTime().getMillis()/1000 + time)) == 0.0){
				continue;
			}
			//exec.setEstimatedDuration(time);
			exec.setEstimatedDuration(Long.valueOf(new DateTime().getMillis()/1000).intValue() - Double.valueOf(exec.getExecStartTime()).intValue() + time);
			ExecTaskFilter filter = new ExecTaskFilter(exec.getUniqueId(), DCWormsTags.TASK_EXECUTION_FINISHED);
			scheduler.sim_cancel(filter, null);
			scheduler.sendInternal(time, DCWormsTags.TASK_EXECUTION_FINISHED, execTask);
		}
	}	
	
	
	/*protected void updatePhases(ExecTask execTask) {
		updateProcessingProgress();

		if (execTask.getStatus() == DCWormsTags.INEXEC) {
			Executable exec = (Executable)execTask;

			double phaseLength = 0;
			try{
				phaseLength = exec.getResourceConsumptionProfile().getResourceConsumption(exec.getCurrentPhase()).getResourceConsumptionTypeChoice().getPercentage()/100;
			} catch(Exception e){
				ExecTaskFilter filter = new ExecTaskFilter(exec.getUniqueId(), DCWormsTags.TASK_EXECUTION_FINISHED);
				scheduler.sim_cancel(filter, null);
				double t = DoubleMath.subtract((exec.getExecStartTime() + exec.getEstimatedDuration()), (new DateTime().getMillis()/1000));
				scheduler.sendInternal(t, DCWormsTags.TASK_EXECUTION_FINISHED, execTask);
				PEUnit peUnit = (PEUnit)exec.getUsedResources().getLast().getResourceUnits().get(StandardResourceUnitName.PE);
				notifyComputingResources(peUnit, EnergyEventType.RESOURCE_UTILIZATION_CHANGED, exec);
			}
			if(phaseLength != 0){			
				exec.setCurrentPhase(exec.getCurrentPhase() + 1);
				ExecTaskFilter filter = new ExecTaskFilter(exec.getUniqueId(), DCWormsTags.PHASE_CHANGED);
				scheduler.sim_cancel(filter, null);
				scheduler.sendInternal(phaseLength * exec.getEstimatedDuration(), DCWormsTags.PHASE_CHANGED, execTask);
				PEUnit peUnit = (PEUnit)exec.getUsedResources().getLast().getResourceUnits().get(StandardResourceUnitName.PE);
				notifyComputingResources(peUnit, EnergyEventType.RESOURCE_UTILIZATION_CHANGED, exec);
			} 
			System.out.println("===" + exec.getJobId() + ":" + phaseLength);
		}
	}	*/

	public double calculateTotalLoad(int size) {
		// background load, defined during initialization
		double load;
		ResourceCalendar resCalendar = (ResourceCalendar) moduleList.getModule(ModuleType.RESOURCE_CALENDAR);
		if (resCalendar == null)
			load = 0;
		else
			load = resCalendar.getCurrentLoad();

		int numberOfPE = 0;
		try {
			for(ResourceUnit resUnit : getResourceManager().getPE()){
				numberOfPE = numberOfPE + resUnit.getAmount();
			}
		} catch (Exception e) {
			numberOfPE = 1;
		}
		double tasksPerPE = (double) size / numberOfPE;
		load += Math.min(1.0 - load, tasksPerPE);

		return load;
	}

	public Accumulator getTotalLoad() {
		return accTotalLoad;
	}

	protected void addTotalLoad(double load) {
		accTotalLoad.add(load);
	}
	
	private Map<ResourceUnitName, ResourceUnit> chooseResourcesForExecution(String resourceName,
			ExecTask task) {

		Map<ResourceUnitName, ResourceUnit> map = new HashMap<ResourceUnitName, ResourceUnit>();
		LocalResourceManager resourceManager = getResourceManager();
		if(resourceName != null){
			ComputingResource resource = null;
			try {
				resource = resourceManager.getResourceByName(resourceName);
			} catch (ResourceException e) {
				return null;
			}
			resourceManager = new LocalResourceManager(resource);
		}

		int cpuRequest;
		try {
			cpuRequest = Double.valueOf(task.getCpuCntRequest()).intValue();
		} catch (NoSuchFieldException e) {
			cpuRequest = 1;
		}

		if (cpuRequest != 0) {
			
			List<ResourceUnit> availableUnits = null;
			try {
				availableUnits = resourceManager.getPE();
			} catch (ResourceException e) {
				return null;
			}
			
			List<ResourceUnit> choosenPEUnits = new ArrayList<ResourceUnit>();
			for (int i = 0; i < availableUnits.size() && cpuRequest > 0; i++) {
				PEUnit peUnit = (PEUnit) availableUnits.get(i);
				if(peUnit.getFreeAmount() > 0){
					int allocPE = Math.min(peUnit.getFreeAmount(), cpuRequest);
					cpuRequest = cpuRequest - allocPE;
					choosenPEUnits.add(peUnit.replicate(allocPE));	
				}	
			}
			
			if(cpuRequest > 0){
				return null;
			}
			map.put(StandardResourceUnitName.PE, choosenPEUnits.get(0));
		}

		return  map;
	}
	
	public void notifySubmittedWorkloadUnit(WorkloadUnit wu, boolean ack) {
		updateProcessingProgress();
		registerWorkloadUnit(wu);
	}

	private void registerWorkloadUnit(WorkloadUnit wu){
		if(!wu.isRegistered()){
			wu.register(jobRegistry);
		}
		wu.accept(getWorkloadUnitHandler());
	}
	
	class LocalWorkloadUnitHandler implements WorkloadUnitHandler{
		
		public void handleJob(JobInterface<?> job){

			if (log.isInfoEnabled())
				log.info("Received job " + job.getId() + " at " + new DateTime(DateTimeUtilsExt.currentTimeMillis()));

			List<JobInterface<?>> jobsList = new ArrayList<JobInterface<?>>();
			jobsList.add(job);
			TaskListImpl availableTasks = new TaskListImpl();
			for(Task task: jobRegistry.getAvailableTasks(jobsList)){
				task.setStatus((int)BrokerConstants.TASK_STATUS_QUEUED);
				availableTasks.add(task);
			}

			for(TaskInterface<?> task: availableTasks){	
				registerWorkloadUnit(task);
			}
		}
		
		public void handleTask(TaskInterface<?> t){
			Task task = (Task)t;
			List<AbstractProcesses> processes = task.getProcesses();

			if(processes == null || processes.size() == 0){
				Executable exec = new Executable(task);
				registerWorkloadUnit(exec);
			} else {
				for(int j = 0; j < processes.size(); j++){
					AbstractProcesses procesesSet = processes.get(j);
					Executable exec = new Executable(task, procesesSet);
					registerWorkloadUnit(exec);
				}
			}
		}
		
		public void handleExecutable(ExecTask task){
			
			Executable exec = (Executable) task;
			jobRegistry.addExecTask(exec);
			
			exec.trackResource(scheduler.get_name());
			Scheduler parentScheduler = scheduler.getParent();
			List<String> visitedResource = exec.getVisitedResources();
			String [] visitedResourcesArray = visitedResource.toArray(new String[visitedResource.size()]);
			while (parentScheduler != null && !ArrayUtils.contains(visitedResourcesArray, parentScheduler.get_name())) {
				exec.trackResource(parentScheduler.get_name());
				parentScheduler = parentScheduler.getParent();
			}
			exec.setSchedulerName(scheduler.get_id());
			
			TaskList newTasks = new TaskListImpl();
			newTasks.add(exec);
		
			schedulingPlugin.placeTasksInQueues(newTasks, queues, getResourceManager(), moduleList);

			if (exec.getStatus() == DCWormsTags.QUEUED) {
				sendExecutableReadyEvent(exec);
			}
		}
	}

	public WorkloadUnitHandler getWorkloadUnitHandler() {
		return new LocalWorkloadUnitHandler();
	}
	
	
	public LocalResourceManager getResourceManager() {
		if (resourceManager instanceof ResourceManager)
			return (LocalResourceManager) resourceManager;
		else
			return null;
	}
	
}
