package model.scheduling.manager.tasks;


import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import model.scheduling.tasks.Job;
import model.scheduling.tasks.JobInterface;
import model.scheduling.tasks.Task;
import model.scheduling.tasks.TaskInterface;



public abstract class AbstractJobRegistry  implements JobRegistry, Cloneable{
	
	private static final long serialVersionUID = 8409060063583755824L;

	
	protected static final ConcurrentHashMap<String, JobInterface<?>> jobs = new ConcurrentHashMap<String, JobInterface<?>>();
	
	protected AbstractJobRegistry(){
	}
	
	public boolean addJob(JobInterface<?> job) {
		jobs.put(job.getId(),  job);
		return true;
	}

	public boolean addTask(TaskInterface<?> task) {
		if(jobs.containsKey(task.getJobId())){
			getJob(task.getJobId()).add((Task)task);
			return true;
		} else {
			return false;
		}
	}

	public JobInterface<?> getJobInfo(String jobId) {
		return jobs.get(jobId);
	}

	public TaskInterface<?> getTaskInfo(String jobId, String taskId) {
		Task task = null;
		Job job = getJob(jobId);
		
		if(job == null)
			return null;
		
		try {
			task = job.getTask(taskId);
		} catch (NoSuchFieldException e) {
		}
		return task;
	}
	
	public Job getJob(String jobId){
		return (Job)jobs.get(jobId);
	}

}
