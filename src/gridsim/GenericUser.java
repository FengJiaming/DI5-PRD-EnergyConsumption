package gridsim;

import java.util.List;

import model.JobDescription;
import model.TaskDescription;
import model.scheduling.tasks.JobInterface;


public interface GenericUser {

	/**
	 * @return a list of all jobs, that have been sent by the user entity
	 */
	public List<JobDescription> getAllSentJobs();
	
	/**
	 * @return a list of all tasks, that have been sent by the user entity
	 */
	public List<TaskDescription> getAllSentTasks();
	
	/**
	 * @return a list of all jobs, that have returned from execution to the user entity
	 */
	public List<JobInterface<?>> getAllReceivedJobs();
	
	/**
	 * @return the name of the user entity (unique in the entire simulation run)
	 */
	public String getUserName();
	
	public int getFinishedTasksCount() ;
	
	
	/**
	 * This method is intended for debug purposes.
	 * @return <code>true</code> if during the simulation, any error occurred 
	 * (e.g. the same job has been returned twice).
	 */
	public boolean isError();
	
	public long getSubmissionStartTime();
	
	public boolean isSimStartTimeDefined();
}
