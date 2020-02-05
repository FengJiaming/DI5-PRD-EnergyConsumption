package model;

import java.io.StringReader;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLContext;

import org.qcg.broker.schemas.jobdesc.Task;

public class TaskDescription implements DescriptionContainer<Task> {

	
	protected static Unmarshaller unmarshaller;
	static {
		XMLContext context = new XMLContext();
		try {
			context.addClass(org.qcg.broker.schemas.jobdesc.Task.class);
			unmarshaller = context.createUnmarshaller();
		} catch (ResolverException e) {
			e.printStackTrace();
			unmarshaller = null;
		}
	}
	
	protected String xml;
	protected Task task;
	protected String taskId;
	protected String userDn;
	protected long submissionTime;
	protected long taskLength;
	protected long waitTime;
	
	
	public TaskDescription(String xmlTask){
		xml = xmlTask;
	}
	
	public String getTaskId(){
		return taskId;
	}
	
	public void setTaskId(String id){
		this.taskId = id;
	}
	
	
	
	public void discardUnused(){
		xml = null;
		task = null;
	}
	
	public Task getDescription() {
		if(task == null){
			try {
				task = (Task) unmarshaller.unmarshal(new StringReader(xml));
			} catch (MarshalException e) {
				e.printStackTrace();
			} catch (ValidationException e) {
				e.printStackTrace();
			}
		}
		
		return task;
	}

	public String getDocument() throws Exception {
		return xml;
	}

	public String getUserDn() {
		return userDn;
	}

	public void setUserDn(String userDn) {
		this.userDn = userDn;
	}

	public long getSubmissionTime() {
		return submissionTime;
	}

	public void setSubmissionTime(long submissionTime) {
		this.submissionTime = submissionTime;
	}

	public long getTaskLength() {
		return taskLength;
	}

	public void setTaskLength(long taskLength) {
		this.taskLength = taskLength;
	}
	
	public long getWorkloadLogWaitTime() {
		return waitTime;
	}

	public void setWorkloadLogWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}

}
