package model.scheduling.queue;

import java.util.List;

import org.joda.time.DateTime;

public interface Queue <K> extends List<K> {

	/**
	 * Gets a queue name
	 * @return name
	 */
	public String getName();

	/**
	 * Sets a queue name
	 * @param name the name to set
	 */
	public void setName(String name);
	
	/**
	 * Gets a queue priority
	 * @return priority
	 */
	public int getPriority();
	
	
	public boolean supportReservations();
	
	/**
	 * Sets a queue priority
	 * @param priority the priority to set
	 */
	public void setPriority(int priority);
		
	/**
	 * Gets arrival time of the object at position pos in a queue.
	 * @param pos the position of the object
	 * @return time when the object at position pos in queue was added to the queue; 
	 * @throws IndexOutOfBoundsException if pos id out of the queue range
	 */
	public DateTime getArrivalTime(int pos) throws IndexOutOfBoundsException;

}
