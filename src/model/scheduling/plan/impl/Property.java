package model.scheduling.plan.impl;

import java.io.StringWriter;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import model.scheduling.plan.PropertyInterface;

public class Property implements PropertyInterface<org.qcg.broker.schemas.schedulingplan.Property> {

	protected org.qcg.broker.schemas.schedulingplan.Property p;
	
	public Property(){
		this.p = new org.qcg.broker.schemas.schedulingplan.Property();
	}
	
	public Property(org.qcg.broker.schemas.schedulingplan.Property value){
		this.p = value;
	}
	
	public String getContent() {
		return this.p.getContent();
	}

	public String getName() {
		return this.p.getName();
	}

	public void setContent(String content) {
		this.p.setContent(content);
	}

	public void setName(String name) {
		this.p.setName(name);
	}

	public org.qcg.broker.schemas.schedulingplan.Property getDescription() {
		return this.p;
	}

	public String getDocument() throws Exception {
		StringWriter writer = new StringWriter();
		try {
			p.marshal(writer);
		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

}
