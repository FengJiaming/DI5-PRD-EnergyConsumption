package model.scheduling.plan.impl;

import java.io.StringWriter;
import java.util.Date;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import model.scheduling.plan.ScheduledTimeInterface;


public class ScheduledTime implements ScheduledTimeInterface<org.qcg.broker.schemas.schedulingplan.ScheduledTime>{

	private static final long serialVersionUID = 8546333273529232717L;
	private org.qcg.broker.schemas.schedulingplan.ScheduledTime st;
	
	public ScheduledTime(){
		st = new org.qcg.broker.schemas.schedulingplan.ScheduledTime();
	}
	
	public ScheduledTime(org.qcg.broker.schemas.schedulingplan.ScheduledTime st){
		this.st = st;
	}
	
	public org.qcg.broker.schemas.schedulingplan.ScheduledTime getDescription() {
		return st;
	}

	public String getDocument() {
		StringWriter writer = new StringWriter();
		try {
			st.marshal(writer);
		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	public Date getEnd() {
		return st.getEnd();
	}

	public Date getStart() {
		return st.getStart();
	}

	public void setEnd(Date end) {
		st.setEnd(end);
	}

	public void setStart(Date start) {
		st.setStart(start);
	}

}
