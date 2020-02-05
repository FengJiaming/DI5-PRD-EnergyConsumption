package model.scheduling.tasks;

import model.scheduling.WorkloadUnitHandler;
import model.scheduling.manager.tasks.JobRegistryImpl;

public interface WorkloadUnit {

	public String getId();
	
	public int getUserId();
	
	public int getStatus();
	
	public void setStatus(int status) throws Exception;

	public boolean isFinished();

	public boolean isRegistered();

	public void register(JobRegistryImpl jobRegistry);
	
	public void accept(WorkloadUnitHandler wuh);

}