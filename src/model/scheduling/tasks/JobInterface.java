package model.scheduling.tasks;

import java.util.List;

import model.DescriptionContainer;


/**
 * 
 * @author Marcin Krystek
 *
 */
public interface JobInterface<T> extends WorkloadUnit, DescriptionContainer<T> {

	/**
	 * 
	 * @return job identifier
	 * @throws NoSuchFieldException if there is no tasks for this job, and job id can not be obtained
	 */
	//public abstract String getId() throws NoSuchFieldException;
	
	/**
	 * 
	 * @return list of tasks which belongs to this job
	 */
	public abstract List<? extends TaskInterface<?>> getTask();
	
	/**
	 * 
	 * @param taskId
	 * @return task with specified taskId
	 * @throws NoSuchFieldException if task with taskId does not exist in this job
	 */
	public abstract TaskInterface<?> getTask(String taskId) throws NoSuchFieldException;
	
	/**
	 * 
	 * @return number of tasks in this job 
	 */
	public abstract int getTaskCount();
	
	/**
	 * 
	 * @return constant which represents current status of this job
	 */
	//public int getStatus();
	
	//public int getUserID();
}
