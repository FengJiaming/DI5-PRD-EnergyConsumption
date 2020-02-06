package schedframe.scheduling.plan.impl;

import java.io.StringWriter;
import java.util.ArrayList;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import schedframe.scheduling.plan.ScheduledTaskInterface;
import schedframe.scheduling.plan.SchedulingPlanInterface;


public class SchedulingPlan implements SchedulingPlanInterface<org.qcg.broker.schemas.schedulingplan.SchedulingPlan> {

	private static final long serialVersionUID = 2557774150841711876L;
	protected org.qcg.broker.schemas.schedulingplan.SchedulingPlan sp;
	
	public SchedulingPlan(){
		sp = new org.qcg.broker.schemas.schedulingplan.SchedulingPlan();
		taskList = new ArrayList<ScheduledTaskInterface<?>>();
	}
	
	public SchedulingPlan(org.qcg.broker.schemas.schedulingplan.SchedulingPlan value){
		sp = value;
		taskList = new ArrayList<ScheduledTaskInterface<?>>();
	}
	
	public org.qcg.broker.schemas.schedulingplan.SchedulingPlan getDescription() {
		return sp;
	}

	public String getDocument() {
		StringWriter writer = new StringWriter();
		try {
			sp.marshal(writer);
		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	public <Task_> void addTask(ScheduledTaskInterface<Task_> task)
			throws IndexOutOfBoundsException {
		sp.addTask((org.qcg.broker.schemas.schedulingplan.Task) task.getDescription());
		taskList.add(task);
	}

	public <Task_> void addTask(int index, ScheduledTaskInterface<Task_> task)
			throws IndexOutOfBoundsException {
		sp.addTask(index, (org.qcg.broker.schemas.schedulingplan.Task) task.getDescription());
		taskList.add(index, task);
	}

	@SuppressWarnings("unchecked")
	public ScheduledTaskInterface<org.qcg.broker.schemas.schedulingplan.Task> getTask(int index)
			throws IndexOutOfBoundsException {
		return new ScheduledTask(sp.getTask(index));
	}

	@SuppressWarnings("unchecked")
	public ScheduledTaskInterface<org.qcg.broker.schemas.schedulingplan.Task>[] getTask() {
		org.qcg.broker.schemas.schedulingplan.Task tab[] = sp.getTask();
		if(tab == null) return null;
		
		ScheduledTask ret[] = new ScheduledTask[tab.length];
		for(int i = 0; i < tab.length; i++){
			ret[i] = new ScheduledTask(tab[i]);
		}
		
		return ret;
	}

	public int getTaskCount() {
		return sp.getTaskCount();
	}

	public void removeAllTask() {
		sp.removeAllTask();
	}

	public <Task_> boolean removeTask(ScheduledTaskInterface<Task_> task) {
		return sp.removeTask((org.qcg.broker.schemas.schedulingplan.Task) task.getDescription());
	}

	@SuppressWarnings("unchecked")
	public ScheduledTaskInterface<org.qcg.broker.schemas.schedulingplan.Task> removeTaskAt(int index) {
		return new ScheduledTask(sp.removeTaskAt(index));
	}

	public <Task_> void setTask(int index, ScheduledTaskInterface<Task_> task)
			throws IndexOutOfBoundsException {
		sp.setTask(index, (org.qcg.broker.schemas.schedulingplan.Task) task.getDescription());
	}

	public <Task_> void setTask(ScheduledTaskInterface<Task_>[] taskArray) {
		if(taskArray == null) return;
		
		org.qcg.broker.schemas.schedulingplan.Task tab[] = new org.qcg.broker.schemas.schedulingplan.Task[taskArray.length];
		for(int i = 0; i < taskArray.length; i++){
			tab[i] = (org.qcg.broker.schemas.schedulingplan.Task)taskArray[i].getDescription();
		}
		sp.setTask(tab);
	}

	
	protected ArrayList<ScheduledTaskInterface<?>> taskList;

	public ArrayList<ScheduledTaskInterface<?>> getTasks() {

		return this.taskList;
	}


}
