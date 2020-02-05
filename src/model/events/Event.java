package model.events;

import model.events.scheduling.EventReason;

public interface Event {
	
	public EventType getType();
	
	public String getSource();
	
	public int getTag();
	
	public Object getData();
	
	public EventReason getReason();
}
