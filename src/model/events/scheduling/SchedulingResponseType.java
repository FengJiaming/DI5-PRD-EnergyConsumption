package model.events.scheduling;

public enum SchedulingResponseType {

	TERMINATE_TASK(0),
	KILL_TASK(0),
	STOP_AND_RESUME_FROM_CHECKPOINT(0),
	EXECUTE_ANYWAY(0),
	ONE_HOUR_GRACE_PERIOD(3600);
	
	protected int timer;
	
	private SchedulingResponseType(int timer){
		this.timer = timer;
	}
	
	public int getTimer(){
		return this.timer;
	}
}