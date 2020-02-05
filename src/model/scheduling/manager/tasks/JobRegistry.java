package model.scheduling.manager.tasks;


import java.util.List;

import gridsim.schedframe.ExecTask;
import model.ExecutablesList;
import model.scheduling.tasks.JobInterface;
import model.scheduling.tasks.TaskInterface;


public interface JobRegistry {

	public JobInterface<?> getJobInfo(String jobId);

	public TaskInterface<?> getTaskInfo(String jobId, String taskId);

	
	public List<ExecTask> getTasks(int status) ;

	public List<ExecTask> getQueuedTasks() ;
	
	public List<ExecTask> getRunningTasks() ;
	
	public List<ExecTask> getReadyTasks() ;
	
	public List<ExecTask> getFinishedTasks();


	public ExecutablesList getExecutableTasks(); 
	
	public ExecTask getExecutable(String jobId, String taskId);

	
	public List<? extends TaskInterface<?>> getAvailableTasks(List<JobInterface<?>> jobsList);

}
