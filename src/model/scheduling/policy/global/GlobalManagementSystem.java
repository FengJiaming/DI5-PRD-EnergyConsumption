package model.scheduling.policy.global;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.types.Duration;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtilsExt;
import org.qcg.broker.schemas.schedulingplan.types.AllocationStatus;

import qcg.shared.constants.BrokerConstants;
import model.events.scheduling.TaskArrivedEvent;
import model.events.scheduling.TimerEvent;
import model.scheduling.TaskListImpl;
import model.scheduling.WorkloadUnitHandler;
import model.scheduling.plan.AllocationInterface;
import model.scheduling.plan.ScheduledTaskInterface;
import model.scheduling.plan.SchedulingPlanInterface;
import model.scheduling.plugin.SchedulingPlugin;
import model.scheduling.plugin.estimation.ExecutionTimeEstimationPlugin;
import model.scheduling.policy.AbstractManagementSystem;
import model.scheduling.queue.TaskQueueList;
import model.scheduling.tasks.AbstractProcesses;
import model.scheduling.tasks.Job;
import model.scheduling.tasks.JobInterface;
import model.scheduling.tasks.Task;
import model.scheduling.tasks.TaskInterface;
import model.scheduling.tasks.WorkloadUnit;
import eduni.simjava.Sim_event;
import gridsim.DCWormsTags;
import gridsim.GridSim;
import gridsim.GridSimTags;
import gridsim.IO_data;
import gridsim.schedframe.ExecTask;
import gridsim.schedframe.Executable;

public class GlobalManagementSystem extends AbstractManagementSystem {

	private static Log log = LogFactory.getLog(GlobalManagementSystem.class);

	public GlobalManagementSystem(String providerId, String entityName, SchedulingPlugin schedPlugin,
			ExecutionTimeEstimationPlugin execTimeEstimationPlugin, TaskQueueList queues)
			throws Exception {
		super(providerId, entityName,  execTimeEstimationPlugin, queues);

		if(schedPlugin == null){
			throw new Exception("Can not create global scheduling plugin instance");
		}
		
		this.schedulingPlugin =  schedPlugin;
	}

	public void processEvent(Sim_event ev) {

		int tag = ev.get_tag();
		switch (tag) {

		case DCWormsTags.TIMER:
			if (pluginSupportsEvent(DCWormsTags.TIMER)) {
				TimerEvent event = new  TimerEvent();
				SchedulingPlanInterface<?> decision =  schedulingPlugin.schedule(event, 
						queues,  getJobRegistry(), getResourceManager(), moduleList);
				executeSchedulingPlan(decision);
			}
			sendTimerEvent();
			break;
		}
	}
	
	public void notifySubmittedWorkloadUnit(WorkloadUnit wu, boolean ack) {
		if (!pluginSupportsEvent(GridSimTags.GRIDLET_SUBMIT)) {
			log.error("Plugin " + schedulingPlugin.getClass()
					+ " does not provide support for TASK_ARRIVED event.\n"
					+ "Check plugin configuration or use default one.");
			return;
		}
		
		registerWorkloadUnit(wu);

	}
	
	private void registerWorkloadUnit(WorkloadUnit wu){
		if(!wu.isRegistered()){
			wu.register(jobRegistry);
		}
		wu.accept(getWorkloadUnitHandler());
	}
	

	protected void schedule(JobInterface<?> job){
		List<JobInterface<?>> jobsList = new ArrayList<JobInterface<?>>();
		jobsList.add(job);
		TaskListImpl readyTasks = new TaskListImpl();
		readyTasks.addAll(jobRegistry.getAvailableTasks(jobsList));
		
		schedulingPlugin.placeTasksInQueues(readyTasks, queues, getResourceManager(), moduleList);
		SchedulingPlanInterface<?> decision = schedulingPlugin.schedule(
				new TaskArrivedEvent(), queues, getJobRegistry(),  getResourceManager(), moduleList);
		if (decision != null)
			executeSchedulingPlan(decision);
	}

	public void notifyReturnedWorkloadUnit(WorkloadUnit wu) {
		Executable exec = (Executable) wu;

		long duration = Double.valueOf(exec.getFinishTime() - exec.getExecStartTime()).longValue();
		log.debug("Executable " + exec.getJobId() + "_" + exec.getId() + "\nstart time:  " + 
				new java.util.Date(Double.valueOf(exec.getExecStartTime()).longValue() * 1000) 
		+ "\nfinish time: " + new java.util.Date(Double.valueOf(exec.getFinishTime()).longValue() * 1000)
		+ "\nduration: " + new Duration(duration * 1000));
		
		try {
			Job job = jobRegistry.getJob(exec.getJobId());
			Task task = job.getTask(exec.getTaskId());
			if(exec.getProcessesId() == null){
				try {
					task.setStatus(exec.getStatus());
				} catch (Exception e) {

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
			
			if(job.isFinished()){
				scheduler.send(job.getSenderId(), GridSimTags.SCHEDULE_NOW, GridSimTags.GRIDLET_RETURN, job);
			}
			else {
				schedule(job);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void executeSchedulingPlan(SchedulingPlanInterface<?> decision) {

		ArrayList<ScheduledTaskInterface<?>> taskSchedulingDecisions = decision.getTasks();
		for (int i = 0; i < taskSchedulingDecisions.size(); i++) {

			ScheduledTaskInterface<?> taskDecision = taskSchedulingDecisions.get(i);

			//log.info(decision.getDocument());

			String jobID = taskDecision.getJobId();
			String taskID = taskDecision.getTaskId();
			
			// Task allocations that were rejected because of lack of resources or which were canceled and 
			// not scheduled again are returned to the user.
			if(taskDecision.getStatus() == AllocationStatus.REJECTED){
				Job job = jobRegistry.getJob(jobID);
				scheduler.send(job.getSenderId(), GridSimTags.SCHEDULE_NOW, GridSimTags.GRIDLET_RETURN, job);
				continue;
			}
			
			Task task = (Task) jobRegistry.getTaskInfo(jobID, taskID);
			
			ArrayList<AllocationInterface<?>> allocations = taskDecision.getAllocations();
			for (int j = 0; j < allocations.size(); j++) {

				AllocationInterface<?> allocation = allocations.get(j);
				Executable exec = createExecutable(task, allocation);
				submitTask(exec, allocation);
				task.setStatus((int)BrokerConstants.TASK_STATUS_QUEUED);
			}								
		}
	}

	private Executable createExecutable(Task task, AllocationInterface<?> allocation) {

		String refersTo = allocation.getProcessGroupId(); // null;//allocation.getRefersTo();
		if(refersTo == null)
			refersTo = task.getId();
			
		Executable exec = null;

		if(refersTo.equals(task.getId())){
			exec = new Executable(task);
		} else {
			List<AbstractProcesses> processes = task.getProcesses();
			if(processes == null) {
				try {
					log.error("Allocation: " + allocation.getDocument() + "\nrefers to unknown task or processes set." +
							" Set correct value (task id or prcesses set id) for allocation refersTo attribute.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			boolean found = false;
			for(int j = 0; j < processes.size() && !found; j++){
				AbstractProcesses procesesSet = processes.get(j);
				if(refersTo.equals(procesesSet.getId())){
					exec = new Executable(task, procesesSet);
					found = true;
				}
			}
			if(!found){
				log.error("Allocation refers to unknown proceses set.");
			}
		}

		exec.setReservationId(allocation.getReservationId());
			
		/*HostInterface<?> host = allocation.getHost();
		ComputingResourceTypeInterface<?> crt = host.getMachineParameters();
		if(crt != null){
			ComputingResourceTypeItemInterface<?> crti = crt.getComputingResourceTypeItem(0);
			if(crti != null){
				ParameterPropertyInterface<?> properties[] = crti.getHostParameter().getProperty();
				for(int p = 0; p < properties.length; p++){
					ParameterPropertyInterface<?> property = properties[p];
					if("chosenCPUs".equals(property.getName())){
						Object cpuNames = property.getValue();
						exec.addSpecificResource(ResourceParameterName.FREECPUS, cpuNames);
					}
				}
			}
		}*/
		return exec;
	}
	
	protected void submitTask(TaskInterface<?> task, AllocationInterface<?> allocation) {

		String providerName = allocation.getProviderName();
		if (providerName == null) {
			return;
		}
		removeFromQueue(task);
		
		int resID = GridSim.getEntityId(providerName);
		IO_data data = new IO_data(task, 0, resID);
		scheduler.send(scheduler.getOutputPort(), GridSimTags.SCHEDULE_NOW, GridSimTags.GRIDLET_SUBMIT, data);	
		//scheduler.send(providerName, GridSimTags.SCHEDULE_NOW, GridSimTags.GRIDLET_SUBMIT, job);	
		
		if(log.isDebugEnabled())
			log.debug("Submitted job " + task.getId() + " to " + providerName);

	}

	class GlobalWorkloadUnitHandler implements  WorkloadUnitHandler{

		public void handleJob(JobInterface<?> job){
			if (log.isInfoEnabled())
				log.info("Received job " + job.getId() + " at " + new DateTime(DateTimeUtilsExt.currentTimeMillis()));

			jobRegistry.addJob(job);
			schedule(job);
		}
		
		public void handleTask(TaskInterface<?> task) {
			throw new RuntimeException("Not implemented since it isn't expected that tasks are send directly to the global scheduler.");
		}

		public void handleExecutable(ExecTask task) {
			throw new RuntimeException("Not implemented since it isn't expected that tasks are send directly to the global scheduler.");
		}
	}

	public WorkloadUnitHandler getWorkloadUnitHandler() {
		return new GlobalWorkloadUnitHandler();
	}


}
