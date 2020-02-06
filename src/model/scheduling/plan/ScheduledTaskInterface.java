package model.scheduling.plan;

import java.util.ArrayList;

import model.DescriptionContainer;
import model.scheduling.tasks.TaskInterface;

public interface ScheduledTaskInterface<T> extends DescriptionContainer<T> {

	/**
	 * 
	 * 
	 * @param vAllocation
	 * @throws java.lang.IndexOutOfBoundsException if the index
	 * given is outside the bounds of the collection
	 */
	public <Allocation> void addAllocation(
			final AllocationInterface<Allocation> vAllocation)
			throws java.lang.IndexOutOfBoundsException;

	/**
	 * 
	 * 
	 * @param index
	 * @param vAllocation
	 * @throws java.lang.IndexOutOfBoundsException if the index
	 * given is outside the bounds of the collection
	 */
	public <Allocation> void addAllocation(final int index,
			final AllocationInterface<Allocation> vAllocation)
			throws java.lang.IndexOutOfBoundsException;

	/**
	 */
	public void deleteTopology();

	/**
	 * Method getAllocation.
	 * 
	 * @param index
	 * @throws java.lang.IndexOutOfBoundsException if the index
	 * given is outside the bounds of the collection
	 * @return the value of the
	 * schedframe.schedulingplan.impl.Allocation at the given index
	 */
	public <Allocation>AllocationInterface<Allocation> getAllocation(
			final int index) throws java.lang.IndexOutOfBoundsException;

	/**
	 * Method getAllocation.Returns the contents of the collection
	 * in an Array.  <p>Note:  Just in case the collection contents
	 * are changing in another thread, we pass a 0-length Array of
	 * the correct type into the API call.  This way we <i>know</i>
	 * that the Array returned is of exactly the correct length.
	 * 
	 * @return this collection as an Array
	 */
	public <Allocation>AllocationInterface<Allocation>[] getAllocation();

	/**
	 * Method getAllocationCount.
	 * 
	 * @return the size of this collection
	 */
	public int getAllocationCount();

	/**
	 * Returns the value of field 'jobId'.
	 * 
	 * @return the value of field 'JobId'.
	 */
	public java.lang.String getJobId();

	/**
	 * Returns the value of field 'scheduledTime'.
	 * 
	 * @return the value of field 'ScheduledTime'.
	 */
	public <ScheduledTime>ScheduledTimeInterface<ScheduledTime> getScheduledTime();

	/**
	 * Returns the value of field 'status'.
	 * 
	 * @return the value of field 'Status'.
	 */
	public org.qcg.broker.schemas.schedulingplan.types.AllocationStatus getStatus();
	
	public String getStatusDescription();

	/**
	 * Returns the value of field 'taskId'.
	 * 
	 * @return the value of field 'TaskId'.
	 */
	public java.lang.String getTaskId();

	/**
	 * Returns the value of field 'topology'.
	 * 
	 * @return the value of field 'Topology'.
	 */
	public int getTopology();

	/**
	 * Method hasTopology.
	 * 
	 * @return true if at least one Topology has been added
	 */
	public boolean hasTopology();

	/**
	 */
	public void removeAllAllocation();

	/**
	 * Method removeAllocation.
	 * 
	 * @param vAllocation
	 * @return true if the object was removed from the collection.
	 */
	public <Allocation> boolean removeAllocation(
			final AllocationInterface<Allocation> vAllocation);

	/**
	 * Method removeAllocationAt.
	 * 
	 * @param index
	 * @return the element removed from the collection
	 */
	public <Allocation>AllocationInterface<Allocation> removeAllocationAt(final int index);

	/**
	 * 
	 * 
	 * @param index
	 * @param vAllocation
	 * @throws java.lang.IndexOutOfBoundsException if the index
	 * given is outside the bounds of the collection
	 */
	public <Allocation> void setAllocation(final int index,
			final AllocationInterface<Allocation> vAllocation)
			throws java.lang.IndexOutOfBoundsException;

	/**
	 * 
	 * 
	 * @param vAllocationArray
	 */
	public <Allocation> void setAllocation(
			final AllocationInterface<Allocation>[] vAllocationArray);

	/**
	 * Sets the value of field 'jobId'.
	 * 
	 * @param jobId the value of field 'jobId'.
	 */
	public void setJobId(final java.lang.String jobId);

	/**
	 * Sets the value of field 'scheduledTime'.
	 * 
	 * @param scheduledTime the value of field 'scheduledTime'.
	 */
	public <ScheduledTime> void setScheduledTime(
			final ScheduledTimeInterface<ScheduledTime> scheduledTime);

	/**
	 * Sets the value of field 'status'.
	 * 
	 * @param status the value of field 'status'.
	 */
	public void setStatus(
			final org.qcg.broker.schemas.schedulingplan.types.AllocationStatus status);
	
	public void setStatusDescription(String statusDescription);

	/**
	 * Sets the value of field 'taskID'.
	 * 
	 * @param taskId the value of field 'taskId'.
	 */
	public void setTaskId(final java.lang.String taskId);

	/**
	 * Sets the value of field 'topology'.
	 * 
	 * @param topology the value of field 'topology'.
	 */
	public void setTopology(final int topology);
	

	public ArrayList<AllocationInterface<?>> getAllocations();
	
	public TaskInterface<?> getTask();

}