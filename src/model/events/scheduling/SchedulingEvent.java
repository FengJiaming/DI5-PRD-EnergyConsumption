package model.events.scheduling;

import model.events.Event;
import model.events.EventType;

public class SchedulingEvent implements Event{
	
	protected EventType type;
	protected EventReason reason;
	
	protected String source;
	
	public SchedulingEvent(EventType eventType){
		this(eventType, EventReason.UNKNOWN);
	}
	
	public SchedulingEvent(EventType eventType, EventReason eventReason){
		type = eventType;
		reason = eventReason;
	}
	
	public SchedulingEventType getType(){
		SchedulingEventType schedEventType; 
		try{
			schedEventType = (SchedulingEventType)type;
		} catch(Exception e){
			schedEventType = SchedulingEventType.RESOURCE_STATE_CHANGED;
		}
		return schedEventType;
	}
	
	public EventReason getReason(){
		return reason;
	}
	
	public void setSource(String value){
		this.source = value;
	}
	
	public String getSource(){
		return this.source;
	}

	public int getTag() {
		return type.getTag();
	}

	@Override
	public Object getData() {
		return null;
	}
	
}
