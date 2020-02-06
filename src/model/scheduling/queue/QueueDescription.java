package model.scheduling.queue;

public class QueueDescription {
	
	protected String name;
	protected int priority;
	protected boolean reservation;
	protected long load;
	
	public QueueDescription(String name, int priority, boolean reservation, long load) {
		this.name = name;
		this.priority = priority;
		this.reservation = reservation;
		this.load = load;
	}
	
	public String getName() {
		return name;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public boolean supportReservation() {
		return reservation;
	}
	
	public long getLoad() {
		return load;
	}
}
