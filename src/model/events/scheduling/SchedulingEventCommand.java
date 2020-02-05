package model.events.scheduling;

import model.events.Event;
import model.events.EventCommand;
import model.events.EventType;
import model.resources.computing.ComputingResource;

public class SchedulingEventCommand implements EventCommand {

	private ComputingResource compResource;
	
	public SchedulingEventCommand (ComputingResource compResource) {
		this.compResource = compResource;
	}
	
	public void execute(EventType evType) {
		SchedulingEvent ev = new SchedulingEvent(evType);
		ev.setSource(compResource.getName());
		compResource.getEventHandler().handleSchedulingEvent(ev);
	}
	
	public void execute(Event event) {
		SchedulingEvent ev = new SchedulingEvent(event.getType());
		ev.setSource(compResource.getName());
		compResource.getEventHandler().handleSchedulingEvent(ev);
	}

}
