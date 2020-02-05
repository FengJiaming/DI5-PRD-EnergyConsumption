package model.events.scheduling;

import model.events.EventType;

public enum SchedulingEventType implements EventType {

	// task related
	TASK_FINISHED(1),
	TASK_FAILED(2),
	TASK_CANCELED(4),
	TASK_ARRIVED(8),
	START_TASK_EXECUTION(16384),
	TASK_REQUESTED_TIME_EXPIRED(32768),

	//job related
	JOB_ARRIVED(16),
	JOB_FINISHED(32),
	JOB_FAILED(64),
	JOB_CANCELED(128),

	//time related
	TIMER(254),
	
	//resource related
	RESOURCE_FAILED(512),
	//provider related (relevant to grid scheduling)
	PROVIDER_FAILED(1024),
	
	//reservation related
	RESERVATION_ACTIVE(2048),
	RESERVATION_FINISHED(4096),
	RESERVATION_FAILED(8192),
	RESERVATION_EXPIRED(16384),
	
	RESOURCE_STATE_CHANGED(32668)
	;
	
	protected int tag;
	
	private SchedulingEventType(int tag){
		this.tag = tag;
	}
	
	public int getTag(){
		return this.tag;
	}
	
	public String getName(){
		return toString();
	}
}
