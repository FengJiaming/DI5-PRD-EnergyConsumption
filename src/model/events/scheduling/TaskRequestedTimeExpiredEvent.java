package model.events.scheduling;

public class TaskRequestedTimeExpiredEvent extends SchedulingEvent{
	protected String jobId;
	protected String taskId;
	
	public TaskRequestedTimeExpiredEvent(String jobId, String taskId) {
		super(SchedulingEventType.TASK_REQUESTED_TIME_EXPIRED);
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
