package model.scheduling.plan;

import model.DescriptionContainer;

public interface ScheduledTimeInterface<T> extends DescriptionContainer<T> {

	/**
	 * Returns the value of field 'end'.
	 * 
	 * @return the value of field 'End'.
	 */
	public java.util.Date getEnd();

	/**
	 * Returns the value of field 'start'.
	 * 
	 * @return the value of field 'Start'.
	 */
	public java.util.Date getStart();

	/**
	 * Sets the value of field 'end'.
	 * 
	 * @param end the value of field 'end'.
	 */
	public void setEnd(final java.util.Date end);

	/**
	 * Sets the value of field 'start'.
	 * 
	 * @param start the value of field 'start'.
	 */
	public void setStart(final java.util.Date start);

}