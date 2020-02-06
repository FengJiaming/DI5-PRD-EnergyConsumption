package model.resources.computing.profiles.energy;

import model.events.EventType;

public enum EnergyEventType implements EventType {
	
	TASK_FINISHED(1),
	TASK_FAILED(2),
	TASK_CANCELED(4),
	TASK_STARTED(8),

	TIMER(16),

	POWER_STATE_CHANGED(32),
	FREQUENCY_CHANGED(64),
	VOLTAGE_CHANGED(128),
	
	AIRFLOW_STATE_CHANGED(256),
	
	RESOURCE_FAILED(512),
	
	RESOURCE_UTILIZATION_CHANGED(1024)
	
	;

	protected int tag;
	
	public int getTag() {
		return tag;
	}

	private EnergyEventType(int tag){
		this.tag = tag;
	}
	
	public String getName(){
		return toString();
	}
}
