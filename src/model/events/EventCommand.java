package model.events;



public interface EventCommand {

	public void execute(EventType evType);
	
	public void execute(Event event);
}
