package model.scheduling.queue;


import org.joda.time.DateTime;

import gridsim.DCWormsTags;
import gridsim.schedframe.queues.AbstractStatsSupportingQueue;
import model.scheduling.tasks.TaskInterface;

public class TaskQueue extends AbstractStatsSupportingQueue<TaskInterface<?>> implements Queue<TaskInterface<?>>{

	private static final long serialVersionUID = 6576299222910508209L;

	protected String name;
	protected int priority;
	protected boolean supportReservation;
	
	public TaskQueue (boolean supportReservation){
		this.name = "Queue";
		this.priority = 0;
		this.supportReservation = supportReservation;
	}
	
	public boolean add(TaskInterface<?> task){
		try {
			task.setStatus(DCWormsTags.QUEUED);
		} catch(Exception e){
			throw new RuntimeException(e);
		}
		return super.add(task);
	}
	
	public void add(int pos, TaskInterface<?> task){
		try {
			task.setStatus(DCWormsTags.QUEUED);
		} catch(Exception e){
			throw new RuntimeException(e);
		}
		 super.add(pos, task);
	}
	
	public DateTime getArrivalTime(int pos) throws IndexOutOfBoundsException {
		return get(pos).getSubmissionTimeToBroker();
	}

	public String getName() {
		return name;
	}

	public int getPriority() {
		return priority;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean supportReservations() {
		return supportReservation;
	}
}
