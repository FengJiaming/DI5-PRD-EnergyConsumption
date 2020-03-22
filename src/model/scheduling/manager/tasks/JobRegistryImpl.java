package model.scheduling.manager.tasks;

import gridsim.DCWormsTags;
import gridsim.schedframe.ExecTask;
import model.ExecutablesList;
import model.scheduling.tasks.JobInterface;
import model.scheduling.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qcg.broker.schemas.resreqs.ParentType;
import org.qcg.broker.schemas.resreqs.Workflow;
import org.qcg.broker.schemas.resreqs.types.TaskStatesName;


import qcg.shared.constants.BrokerConstants;

public class JobRegistryImpl extends AbstractJobRegistry {
	
	private static final long serialVersionUID = 8030555906990767342L;

	private static Log log = LogFactory.getLog(JobRegistryImpl.class);

	private String context;

	//TO DO - consider data structure
	protected static final ExecutablesList executables = new ExecutablesList();
//	protected ExecutablesList executables = new ExecutablesList();
	
	//protected static final List<ExecTask> executables = Collections.synchronizedList(new ArrayList<ExecTask>());;
	//protected static final List<ExecTaskInterface> executables = new CopyOnWriteArrayList<ExecTaskInterface>();

	public JobRegistryImpl(String context) {
		this.context = context;
	}

	public boolean addExecTask(ExecTask newTask) {
		if(getExecutable(newTask.getJobId(), newTask.getId()) == null) {
			synchronized (executables)  {
				executables.add(newTask);
			}
			return true;
		}
		return false;
	}

	public ExecutablesList getExecutableTasks() {
		return executables;
	}
	public List<ExecTask> getTasks(int status) {
		List<ExecTask> taskList = new ArrayList<ExecTask>();
		synchronized (executables)  {
			for (ExecTask task: executables) {
				if (task.getStatus() == status) {
					List<String> visitedResource = task.getVisitedResources();
					if(ArrayUtils.contains(visitedResource.toArray(new String[visitedResource.size()]), context)) {
						taskList.add(task);
					}
				}
			}
		}
		return taskList;
	}

	public List<ExecTask> getQueuedTasks() {
		return getTasks(DCWormsTags.QUEUED);
	}
	
	public List<ExecTask> getRunningTasks() {
		return getTasks(DCWormsTags.INEXEC);
	}
	
	public List<ExecTask> getReadyTasks() {
		return getTasks(DCWormsTags.READY);
	}
	
	public List<ExecTask> getFinishedTasks() {
		return getTasks(DCWormsTags.SUCCESS);
	}
	
	public ExecTask getExecutable(String jobId, String taskId){
		synchronized (executables)  {
			for (ExecTask task : executables) {
				if (task.getJobId().compareTo(jobId) == 0 && task.getId().compareTo(taskId) == 0) {
					return task;
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Task> getAvailableTasks(List<JobInterface<?>> wuList) {
		List<Task> availableTasks = new ArrayList<Task>();
		List<Task> waitingTasks = new ArrayList<Task>();
		
		for(int i = 0; i < wuList.size(); i++){
			JobInterface<?> wu  = (JobInterface<?>)wuList.get(i);
			waitingTasks.addAll((List<Task>)wu.getTask());
		}

		availableTasks.addAll(getPrecedenceConstrainedAvailableTasks(waitingTasks));
		return availableTasks;
	}


	private List<Task> getPrecedenceConstrainedAvailableTasks(List<Task> tasks){
		
		List<Task> availableTasks = new ArrayList<Task>();
		int size = tasks.size();
		
		for(int i = 0; i < size; i++){
			int parCnt;
			int previousTaskSucceedCnt = 0;
			Task task = tasks.get(i);
			if(task.getStatus() != (int)BrokerConstants.TASK_STATUS_UNSUBMITTED)
				continue;
			//the following procedure supports only one nested structure
			Workflow w = task.getDescription().getWorkflow();
			if (w == null){
				availableTasks.add(task);
				continue;
			}
			if(w.getAnd() != null) {
				parCnt = w.getAnd().getParentOpTypeItemCount();
				if(parCnt == 0)
				{
					availableTasks.add(task);
				}
				else
				{
					for(int j = 0; j < parCnt; j++){
						ParentType par = w.getAnd().getParentOpTypeItem(j).getParent();
						if(par.getTriggerState().compareTo(TaskStatesName.FINISHED) == 0){
							if(!checkTaskCompletion(task.getJobId(), par.getContent())){
								break;
							}
						}
						previousTaskSucceedCnt++;
					}

					if(previousTaskSucceedCnt == parCnt)
						availableTasks.add(task);
				}
			} 
			else if(w.getOr() != null) {
				parCnt = w.getOr().getParentOpTypeItemCount();
				if(parCnt == 0)
				{
					availableTasks.add(task);
				}
				else
				{
					for(int j = 0; j < parCnt; j++){
						ParentType par = w.getOr().getParentOpTypeItem(j).getParent();
						if(par.getTriggerState().compareTo(TaskStatesName.FINISHED) == 0){
							if(!checkTaskCompletion(task.getJobId(), par.getContent())){
								continue;
							}
						}
						previousTaskSucceedCnt++;
					}

					if(previousTaskSucceedCnt > 0)
						availableTasks.add(task);
				}
			}
			else {
				parCnt = w.getParentCount();
				if(parCnt == 0)
				{
					availableTasks.add(task);
				}
				else
				{
					for(int j = 0; j < parCnt; j++){
						ParentType par = w.getParent(j);
						if(par.getTriggerState().compareTo(TaskStatesName.FINISHED) == 0){
							if(!checkTaskCompletion(task.getJobId(), par.getContent())){
								continue;
							}
						}
						previousTaskSucceedCnt++;
					}

					if(previousTaskSucceedCnt == parCnt)
						availableTasks.add(task);
				}
			}
			
			/*try{		
				parCnt = task.getDescription().getWorkflow().getParentCount();
			} catch(Exception e){
				parCnt = 0;
			}
			if(parCnt == 0){
				availableTasks.add(task);
			}
			else {
				for(int j = 0; j < parCnt; j++){
					ParentType par = task.getDescription().getWorkflow().getParent(j);
					if(par.getTriggerState().compareTo(TaskStatesName.FINISHED) == 0){
						if(!checkTaskCompletion(task.getJobId(), par.getContent())){
							break;
						}
					}
					previousTaskSucceedCnt++;
				}
				
				if(previousTaskSucceedCnt == parCnt && task.getDescription().getWorkflow().getAnd() != null)
					availableTasks.add(task);
				else if(previousTaskSucceedCnt > 0 && task.getDescription().getWorkflow().getOr() != null)
					availableTasks.add(task);
				else if (previousTaskSucceedCnt == parCnt)
					availableTasks.add(task);
			}*/
		}		
		return availableTasks;
	}
	
	private boolean checkTaskCompletion(String jobID, String taskID){
		JobInterface<?> job = getJobInfo(jobID);
		try {
			if(job.getTask(taskID).isFinished())
				return true;
		} catch (NoSuchFieldException e) {
			//e.printStackTrace();
		}
		return false;
	}

}
