package model.scheduling.plan;

import java.util.ArrayList;

import model.DescriptionContainer;

public interface SchedulingPlanInterface<T> extends DescriptionContainer<T> {

	/**
	 * 
	 * 
	 * @param vTask
	 * @throws java.lang.IndexOutOfBoundsException if the index
	 * given is outside the bounds of the collection
	 */
	public <Task> void addTask(final ScheduledTaskInterface<Task> vTask)
			throws java.lang.IndexOutOfBoundsException;

	/**
	 * 
	 * 
	 * @param index
	 * @param vTask
	 * @throws java.lang.IndexOutOfBoundsException if the index
	 * given is outside the bounds of the collection
	 */
	public <Task> void addTask(final int index,
			final ScheduledTaskInterface<Task> vTask)
			throws java.lang.IndexOutOfBoundsException;

	/**
	 * Method getTask.
	 * 
	 * @param index
	 * @throws java.lang.IndexOutOfBoundsException if the index
	 * given is outside the bounds of the collection
	 * @return the value of the schedframe.schedulingplan.impl.Task
	 * at the given index
	 */
	public <Task>ScheduledTaskInterface<Task> getTask(final int index)
			throws java.lang.IndexOutOfBoundsException;

	/**
	 * Method getTask.Returns the contents of the collection in an
	 * Array.  <p>Note:  Just in case the collection contents are
	 * changing in another thread, we pass a 0-length Array of the
	 * correct type into the API call.  This way we <i>know</i>
	 * that the Array returned is of exactly the correct length.
	 * 
	 * @return this collection as an Array
	 */
	public <Task>ScheduledTaskInterface<Task>[] getTask();

	/**
	 * Method getTaskCount.
	 * 
	 * @return the size of this collection
	 */
	public int getTaskCount();

	/**
	 */
	public void removeAllTask();

	/**
	 * Method removeTask.
	 * 
	 * @param vTask
	 * @return true if the object was removed from the collection.
	 */
	public <Task> boolean removeTask(final ScheduledTaskInterface<Task> vTask);

	/**
	 * Method removeTaskAt.
	 * 
	 * @param index
	 * @return the element removed from the collection
	 */
	public <Task>ScheduledTaskInterface<Task> removeTaskAt(final int index);

	/**
	 * 
	 * 
	 * @param index
	 * @param vTask
	 * @throws java.lang.IndexOutOfBoundsException if the index
	 * given is outside the bounds of the collection
	 */
	public <Task> void setTask(final int index,
			final ScheduledTaskInterface<Task> vTask)
			throws java.lang.IndexOutOfBoundsException;

	/**
	 * 
	 * 
	 * @param vTaskArray
	 */
	public <Task> void setTask(final ScheduledTaskInterface<Task>[] vTaskArray);


	public ArrayList<ScheduledTaskInterface<?>> getTasks();
}