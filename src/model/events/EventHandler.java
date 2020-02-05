package model.events;

import model.events.scheduling.SchedulingEvent;

public interface EventHandler {
	
	public void handleResourceEvent(Event event);
	
	public void handleSchedulingEvent(SchedulingEvent event);
	
}
