package model.scheduling.tasks;


import org.qcg.broker.schemas.resreqs.ParentType;
import org.qcg.broker.schemas.resreqs.ResourceRequirements;
import org.qcg.broker.schemas.resreqs.types.TaskStatesName;

import model.scheduling.WorkloadUnitHandler;
import model.scheduling.manager.tasks.JobRegistryImpl;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;

import qcg.shared.constants.BrokerConstants;


public class Job implements JobInterface<ResourceRequirements> {

	protected List<Task> tasks;
	protected int senderId;
	protected boolean isRegistered;
	
	public Job(String id){
		tasks = new ArrayList<Task>();
	}
	
	public Job(ResourceRequirements resourceRequirements) throws Exception{
		org.qcg.broker.schemas.resreqs.Task task[] = resourceRequirements.getTask();
		if(task == null || task.length == 0)
			throw new NoSuchFieldException("No tasks are defined for job.");
		
		this.tasks = new ArrayList<Task>();
		
		for(int i = 0; i < task.length; i++){
			this.tasks.add(new Task(task[i]));
		}
		
		isRegistered = true;
	}

	public void add(Task task) {
		this.tasks.add(task);
	}
	
	public String getId() {
		if(this.tasks.size() == 0)
			throw new RuntimeException("No tasks are defined for job, so it is not possible to obtain job id.");
		
		return this.tasks.get(0).getJobId();
	}
	
	public List<Task> getTask() {
		return this.tasks;
	}
	
	public Task getTask(String taskId) throws NoSuchFieldException {
		if(taskId == null)
			throw new IllegalArgumentException("TaskId can not be null. Specify appropriate taskId.");
		
		if(this.tasks == null || this.tasks.size() == 0)
			throw new NoSuchFieldException("No tasks are defined for job.");
		
		Task retTask = null;
		
		Iterator<Task> itr = this.tasks.iterator();
		while(itr.hasNext() && retTask == null){
			Task task = itr.next();
			if(taskId.equals(task.getId())){
				retTask = task;
			}
		}

		if(retTask == null)
			throw new NoSuchFieldException("Task "+taskId + " is not available in job " + getId());
		
		return retTask;
	}
	
	public int getTaskCount() {
		return this.tasks.size();
	}

	
	public ResourceRequirements getDescription() {
		
		ResourceRequirements resReq = new ResourceRequirements();
		if(this.tasks == null)
			return resReq;
		
		Iterator<Task> itr = this.tasks.iterator();
		
		while(itr.hasNext()){
			Task task = (Task) itr.next();
			resReq.addTask(task.getDescription());
		}
		
		return resReq;
	}

	public String getDocument() throws Exception {
		ResourceRequirements resReq = getDescription();
		Writer writer = new StringWriter();
		
		resReq.marshal(writer);
		
		return writer.toString();
	}

	public boolean isFinished(){
		
		for(int i = 0; i < tasks.size(); i++){
			//if(tasks.get(i).getStatus() != BrokerConstants.TASK_STATUS_FINISHED)
			//	return false;
			if(!tasks.get(i).isFinished())
				return false;
		}
		return true;
	}
	
	public DateTime getSubmissionTimeToBroker(){
		return tasks.get(0).getSubmissionTimeToBroker();
	}
	
	public int getStatus(){
		boolean isForAll = true;
		int baseStatus = tasks.get(0).getStatus();
		
		for(int i = 1; i < tasks.size() && isForAll; i++){
			Task t = tasks.get(i);
			isForAll = (t.getStatus() == baseStatus);
			switch(t.getStatus()){
				case (int)BrokerConstants.TASK_STATUS_QUEUED: 
					return (int)BrokerConstants.TASK_STATUS_QUEUED;
				case (int)BrokerConstants.TASK_STATUS_UNSUBMITTED:
					return (int)BrokerConstants.TASK_STATUS_UNSUBMITTED;
			}
		}
		
		if(isForAll && baseStatus == BrokerConstants.TASK_STATUS_FINISHED)
			return (int)BrokerConstants.JOB_STATUS_FINISHED;
		
		return -1;
	}
	
	public boolean setStatus(String taskId, int status){
		boolean found = false;
		for(int i = 0; i < tasks.size() && !found; i++){
			Task t = tasks.get(i);
			if(taskId.equals(t.getId())){
				try {
					t.setStatus(status);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				found = true;
			}
		}
		return found;
	}
	
	public void setSenderId(int id){
		this.senderId = id;
	}
	
	public int getSenderId(){
		return this.senderId;
	}
	
	public int getUserId(){
		return this.senderId;
	}

	public void setStatus(int status) throws Exception{

	}

	public boolean isRegistered() {
		return isRegistered;
	}

	public void register(JobRegistryImpl jobRegistry) {
		isRegistered = jobRegistry.addJob(this);
		
	}
	
	@Override
	public void accept(WorkloadUnitHandler wuh) {
		wuh.handleJob(this);
	}
	
	private List<Task> getReadyTasks(){
		
		List<Task> readyTasks = new ArrayList<Task>();
		int size = tasks.size();
		
		for(int i = 0; i < size; i++){
			int parCnt;
			int previousTaskReadyCnt = 0;
			Task task = tasks.get(i);
			if(task.getStatus() != (int)BrokerConstants.TASK_STATUS_UNSUBMITTED)
				continue;
			try{		
				parCnt = task.getDescription().getWorkflow().getParentCount();
			} catch(Exception e){
				parCnt = 0;
			}
			if(parCnt == 0) {
				readyTasks.add(task);
			}
			else {
				for(int j = 0; j < parCnt; j++){
					ParentType par = task.getDescription().getWorkflow().getParent(j);
					if(par.getTriggerState().compareTo(TaskStatesName.FINISHED) == 0){
						try {
							if(!getTask(par.getContent()).isFinished()){
								break;
							}
						} catch (NoSuchFieldException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					previousTaskReadyCnt++;
				}
				
				if(previousTaskReadyCnt == parCnt && task.getDescription().getWorkflow().getAnd() != null)
						readyTasks.add(task);
				else if(previousTaskReadyCnt > 0 && task.getDescription().getWorkflow().getOr() != null)
					readyTasks.add(task);
			}
		}		
		return readyTasks;
	}
	
}
