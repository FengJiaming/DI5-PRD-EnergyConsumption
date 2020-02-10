package model.scheduling.plan.impl;

import java.io.StringWriter;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import model.scheduling.plan.ParameterPropertyInterface;


public class ParameterProperty implements ParameterPropertyInterface<org.qcg.broker.schemas.schedulingplan.ParameterProperty> {

	private static final long serialVersionUID = -1452887249087909576L;
	private org.qcg.broker.schemas.schedulingplan.ParameterProperty p;
	
	public ParameterProperty(){
		p = new org.qcg.broker.schemas.schedulingplan.ParameterProperty();
	}
	
	public ParameterProperty(org.qcg.broker.schemas.schedulingplan.ParameterProperty value){
		this.p = value;
	}
	
	public org.qcg.broker.schemas.schedulingplan.ParameterProperty getDescription() {
		return p;
	}

	public String getDocument() {
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

	public void addValue(String value) throws IndexOutOfBoundsException {
		p.addValue(value);
	}

	public void addValue(int index, String value)
			throws IndexOutOfBoundsException {
		p.addValue(index, value);
	}

	public void deleteExclude() {
		p.deleteExclude();
	}

	public boolean getExclude() {
		return p.getExclude();
	}

	public String getName() {
		return p.getName();
	}

	public String getValue(int index) throws IndexOutOfBoundsException {
		return p.getValue(index);
	}

	public String[] getValue() {
		return p.getValue();
	}

	public int getValueCount() {
		return p.getValueCount();
	}

	public boolean hasExclude() {
		return p.hasExclude();
	}

	public boolean isExclude() {
		return p.isExclude();
	}

	public boolean isValid() {
		return p.isValid();
	}

	public void removeAllValue() {
		p.removeAllValue();
	}

	public boolean removeValue(String value) {
		return p.removeValue(value);
	}

	public String removeValueAt(int index) {
		return p.removeValueAt(index);
	}

	public void setExclude(boolean exclude) {
		p.setExclude(exclude);
	}

	public void setName(String name) {
		p.setName(name);
	}

	public void setValue(int index, String value)
			throws IndexOutOfBoundsException {
		p.setValue(index, value);
	}

	public void setValue(String[] valueArray) {
		p.setValue(valueArray);
	}

}
