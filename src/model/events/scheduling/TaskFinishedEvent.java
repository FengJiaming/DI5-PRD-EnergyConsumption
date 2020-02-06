package model.events.scheduling;


public class TaskFinishedEvent extends SchedulingEvent {
	
	//protected List<String> epr;

	protected String jobId;
	protected String taskId;
	
	public TaskFinishedEvent(/*List<String> list*/ String jobId, String taskId){
		super(SchedulingEventType.TASK_FINISHED);
		//this.epr = list;
		this.jobId = jobId;
		this.taskId = taskId;
	}
	
	/*public List<String> getReservations(){
		return this.epr;
	}*/
	
	public String getJobId() {
		return jobId;
	}

	public String getTaskId() {
		return taskId;
	}

}
