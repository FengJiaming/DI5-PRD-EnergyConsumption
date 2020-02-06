package model.events.scheduling;


public class StartTaskExecutionEvent extends SchedulingEvent {

	protected String jobId;
	protected String taskId;
	
	public StartTaskExecutionEvent(String jobId, String taskId) {
		super(SchedulingEventType.START_TASK_EXECUTION);
		this.jobId = jobId;
		this.taskId = taskId;
	}

	public String getJobId() {
		return jobId;
	}

	public String getTaskId() {
		return taskId;
	}
	
}
