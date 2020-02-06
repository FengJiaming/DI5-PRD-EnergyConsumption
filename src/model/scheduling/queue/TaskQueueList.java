package model.scheduling.queue;

import java.util.ArrayList;

public class TaskQueueList extends ArrayList<TaskQueue> implements QueueList<TaskQueue> {

	private static final long serialVersionUID = -3824600938144742457L;

	public TaskQueueList(){
		super();
	}
	
	public TaskQueueList(int initialSize){
		super(initialSize);
	}
	
}
