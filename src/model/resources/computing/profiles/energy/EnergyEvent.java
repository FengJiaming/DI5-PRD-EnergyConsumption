package model.resources.computing.profiles.energy;

import model.events.Event;
import model.events.EventType;
import model.events.scheduling.EventReason;


public class EnergyEvent implements Event{

	protected EventType type;
	protected EventReason reason;

	protected String source;
	protected Object data;

	public EnergyEvent(EventType eventType, String source){
		this.type = eventType;
		this.source = source;
		setReason(EventReason.UNKNOWN);
	}
	
	public EnergyEvent(EventType eventType, Object data){
		this.type = eventType;
		this.data = data;
		setReason(EventReason.UNKNOWN);
	}
	
	public EnergyEvent(EventType eventType, String source, Object data){
		this.type = eventType;
		this.source = source;
		this.data = data;
		setReason(EventReason.UNKNOWN);
	}
	
	public int getTag() {
		return type.getTag();
	}

	public Object getData() {
		return data;
	}

	public EnergyEventType getType() {
		EnergyEventType enEventType = null; 
		try{
			enEventType = (EnergyEventType)type;
		} catch(Exception e){

		}
		return enEventType;

	}

	public String getSource() {
		return source;
	}

	public EventReason getReason() {
		return reason;
	}

	public void setReason(EventReason reason) {
		this.reason = reason;
	}
}
