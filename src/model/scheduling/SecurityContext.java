package model.scheduling;

public interface SecurityContext {

	public String getUserIdentity();
	
	public Object getCertificate();
	
	public String getJobId();
	
	public String getTaskId();
	
	public String getProcessesSetId();

}
