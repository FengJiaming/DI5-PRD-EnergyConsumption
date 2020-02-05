package model.events.scheduling;

public enum EventReason {
	//Reasons that caused given events
	UNKNOWN,

	//task-related
	//TASK_FAILED
	APP_FAILURE,
	SYSTEM_FAILURE,
	RESOURCE_FAILURE,
	
	//TASK_CANCELED
	RESERVATION_EXCEEDED,
	RESOURCES_EXCEEDED,
	
	//resource/provider-related
	TEMPORARY_FAILURE,
	PERMAMENT_FAILURE,
	
	
	SIM_INIT
	;
}
