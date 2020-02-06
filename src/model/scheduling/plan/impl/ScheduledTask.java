package schedframe.scheduling.plan.impl;

import java.io.StringWriter;
import java.util.ArrayList;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.qcg.broker.schemas.schedulingplan.types.AllocationStatus;

import schedframe.scheduling.plan.AllocationInterface;
import schedframe.scheduling.plan.ScheduledTaskInterface;
import schedframe.scheduling.plan.ScheduledTimeInterface;
import schedframe.scheduling.tasks.TaskInterface;

public class ScheduledTask implements ScheduledTaskInterface<org.qcg.broker.schemas.schedulingplan.Task> {

	private static final long serialVersionUID = -9006532413203045991L;
	protected org.qcg.broker.schemas.schedulingplan.Task t;

	public ScheduledTask(){
		t = new org.qcg.broker.schemas.schedulingplan.Task();
		allocationList = new ArrayList<AllocationInterface<?>>();
	}
	
	public ScheduledTask(org.qcg.broker.schemas.schedulingplan.Task value){
		t = value;
		allocationList = new ArrayList<AllocationInterface<?>>();
	}
	
	
	public org.qcg.broker.schemas.schedulingplan.Task getDescription() {
		return t;
	}

	public String getDocument() {
		StringWriter writer = new StringWriter();
		try {
			t.marshal(writer);
		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	public <Allocation_> void addAllocation(
			AllocationInterface<Allocation_> allocation)
			throws IndexOutOfBoundsException {
		t.addAllocation((org.qcg.broker.schemas.schedulingplan.Allocation) allocation.getDescription());
		allocationList.add(allocation);
	}

	public <Allocation_> void addAllocation(int index, AllocationInterface<Allocation_> allocation)
			throws IndexOutOfBoundsException {
		t.addAllocation(index, (org.qcg.broker.schemas.schedulingplan.Allocation) allocation.getDescription());
		allocationList.add(index, allocation);
	}

	public void deleteTopology() {
		t.deleteTopology();
	}

	@SuppressWarnings("unchecked")
	public AllocationInterface<org.qcg.broker.schemas.schedulingplan.Allocation> getAllocation(int index)
			throws IndexOutOfBoundsException {
		return new schedframe.scheduling.plan.impl.Allocation(t.getAllocation(index));
	}

	@SuppressWarnings("unchecked")
	public AllocationInterface<org.qcg.broker.schemas.schedulingplan.Allocation>[] getAllocation() {
		org.qcg.broker.schemas.schedulingplan.Allocation tab[] = t.getAllocation();
		if(tab == null) return null;
		
		schedframe.scheduling.plan.impl.Allocation ret[] = new schedframe.scheduling.plan.impl.Allocation[tab.length];
		for(int i = 0; i < tab.length; i++){
			ret[i] = new schedframe.scheduling.plan.impl.Allocation(tab[i]);
		}
		return ret;
	}

	public int getAllocationCount() {
		return t.getAllocationCount();
	}

	public String getJobId() {
		return t.getJobId();
	}

	public AllocationStatus getStatus() {
		return t.getStatus();
	}
	
	public String getStatusDescription(){
		return t.getStatusDescription();
	}

	public String getTaskId() {
		return t.getTaskId();
	}

	public int getTopology() {
		return t.getTopology();
	}

	public boolean hasTopology() {
		return t.hasTopology();
	}

	public void removeAllAllocation() {
		t.removeAllAllocation();
	}

	public <Allocation_> boolean removeAllocation(
			AllocationInterface<Allocation_> allocation) {
		return t.removeAllocation((org.qcg.broker.schemas.schedulingplan.Allocation) allocation.getDescription());
	}

	@SuppressWarnings("unchecked")
	public AllocationInterface<org.qcg.broker.schemas.schedulingplan.Allocation> removeAllocationAt(int index) {
		return new schedframe.scheduling.plan.impl.Allocation(t.removeAllocationAt(index));
	}

	public <Allocation_> void setAllocation(int index,
			AllocationInterface<Allocation_> allocation)
			throws IndexOutOfBoundsException {
		t.setAllocation(index, (org.qcg.broker.schemas.schedulingplan.Allocation)allocation.getDescription());
		allocationList.set(index, allocation);
	}

	public <Allocation_> void setAllocation(
			AllocationInterface<Allocation_>[] allocationArray) {
		if(allocationArray == null) return;
		
		org.qcg.broker.schemas.schedulingplan.Allocation tab[] = new org.qcg.broker.schemas.schedulingplan.Allocation[allocationArray.length];
		for(int i = 0; i < allocationArray.length; i++){
			tab[i] = (org.qcg.broker.schemas.schedulingplan.Allocation)allocationArray[i].getDescription();
		}
		t.setAllocation(tab);
	}

	public void setJobId(String jobId) {
		t.setJobId(jobId);
	}

	public void setStatus(AllocationStatus status) {
		t.setStatus(status);
	}

	public void setStatusDescription(String statusDescription){
		t.setStatusDescription(statusDescription);
	}
	
	public void setTaskId(String taskId) {
		t.setTaskId(taskId);
	}

	public void setTopology(int topology) {
		t.setTopology(topology);
	}

	@SuppressWarnings("unchecked")
	public ScheduledTimeInterface<org.qcg.broker.schemas.schedulingplan.ScheduledTime> getScheduledTime() {
		org.qcg.broker.schemas.schedulingplan.ScheduledTime time = t.getScheduledTime();
		if(time == null)
			return null;
		
		ScheduledTime st = new ScheduledTime(time);
		return st;
	}

	public <ScheduledTime_> void setScheduledTime(
			ScheduledTimeInterface<ScheduledTime_> scheduledTime) {
		t.setScheduledTime((org.qcg.broker.schemas.schedulingplan.ScheduledTime) scheduledTime.getDescription());
	}
	

	protected TaskInterface<?> task;
	protected ArrayList<AllocationInterface<?>> allocationList;

	public ScheduledTask(TaskInterface<?> task){
		this();
		this.task = task;
	}

	public ArrayList<AllocationInterface<?>> getAllocations() {
		return this.allocationList;
	}
	
	public TaskInterface<?> getTask(){
		return this.task;
	}
}
