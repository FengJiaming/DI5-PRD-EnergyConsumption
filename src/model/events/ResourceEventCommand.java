package schedframe.events;

import schedframe.resources.computing.ComputingResource;

public class ResourceEventCommand implements EventCommand {

	private ComputingResource compResource;
	
	public ResourceEventCommand(ComputingResource compResource) {
		this.compResource = compResource;
	}
	
	public void execute(EventType evType) {
		//TODO - add event factory
		throw new RuntimeException("not implemented");
	}

	public void execute(Event event) {
		compResource.getEventHandler().handleResourceEvent(event);
	}

}
