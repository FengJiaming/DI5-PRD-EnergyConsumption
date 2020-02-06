package model.scheduling;

import java.util.ArrayList;

import model.scheduling.tasks.TaskInterface;


public class TaskListImpl extends ArrayList<TaskInterface<?>> implements TaskList {

	private static final long serialVersionUID = -3824600938144742457L;

	public TaskListImpl(){
		super();
	}
	
	public TaskListImpl(int initialSize){
		super(initialSize);
	}	
}
