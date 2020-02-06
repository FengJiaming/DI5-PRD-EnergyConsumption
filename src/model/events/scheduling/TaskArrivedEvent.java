package model.events.scheduling;


public class TaskArrivedEvent extends SchedulingEvent {

	public TaskArrivedEvent(){
		super(SchedulingEventType.TASK_ARRIVED);
	}
}
