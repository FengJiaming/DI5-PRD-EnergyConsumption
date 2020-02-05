package model.scheduling;

import gridsim.schedframe.ExecTask;
import model.scheduling.tasks.JobInterface;
import model.scheduling.tasks.TaskInterface;

public interface WorkloadUnitHandler{
	
	public void handleJob(JobInterface<?> job);
	
	public void handleTask(TaskInterface<?> task);
	
	public void handleExecutable(ExecTask task);

}