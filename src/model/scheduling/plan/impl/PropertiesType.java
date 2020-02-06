package model.scheduling.plan.impl;

import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Iterator;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import model.scheduling.plan.PropertiesTypeInterface;
import model.scheduling.plan.PropertyInterface;

public class PropertiesType implements PropertiesTypeInterface<org.qcg.broker.schemas.schedulingplan.PropertiesType> {

	protected org.qcg.broker.schemas.schedulingplan.PropertiesType pt;
	
	public PropertiesType(){
		this.pt = new org.qcg.broker.schemas.schedulingplan.PropertiesType();
	}
	
	public PropertiesType(org.qcg.broker.schemas.schedulingplan.PropertiesType value){
		this.pt = value;
	}
	
	public <Property_> void addProperty(PropertyInterface<Property_> property) {
		this.pt.addProperty((org.qcg.broker.schemas.schedulingplan.Property)property.getDescription());
	}

	public <Property_> void addProperty(int index,
			PropertyInterface<Property_> property) {
		this.pt.addProperty(index, (org.qcg.broker.schemas.schedulingplan.Property) property.getDescription());
	}

	public <Property_> Enumeration<PropertyInterface<Property_>> enumerateProperty() {
		throw new RuntimeException("Not implemented.");
	}

	@SuppressWarnings("unchecked")
	public PropertyInterface<org.qcg.broker.schemas.schedulingplan.Property> getProperty(int index)
			throws IndexOutOfBoundsException {
		return new Property(this.pt.getProperty(index));
	}

	
	@SuppressWarnings("unchecked")
	public PropertyInterface<org.qcg.broker.schemas.schedulingplan.Property>[] getProperty() {
		org.qcg.broker.schemas.schedulingplan.Property property[] = this.pt.getProperty();
		if(property == null)
			return null;
		
		Property ret[] = new Property[property.length];
		for(int i = 0; i < property.length; i++){
			ret[i] = new Property(property[i]);
		}
		
		return ret;
	}

	public int getPropertyCount() {
		return this.pt.getPropertyCount();
	}

	public <Property_> Iterator<PropertyInterface<Property_>> iterateProperty() {
		throw new RuntimeException("Not implemented.");
	}

	public void removeAllProperty() {
		this.pt.removeAllProperty();
	}

	public <Property_> boolean removeProperty(
			PropertyInterface<Property_> property) {
		return this.pt.removeProperty((org.qcg.broker.schemas.schedulingplan.Property)property.getDescription());
	}

	@SuppressWarnings("unchecked")
	public PropertyInterface<org.qcg.broker.schemas.schedulingplan.Property> removePropertyAt(int index) {
		return new Property(this.pt.removePropertyAt(index));
	}

	public <Property_> void setProperty(int index,
			PropertyInterface<Property_> property)
			throws IndexOutOfBoundsException {
		this.pt.setProperty(index, (org.qcg.broker.schemas.schedulingplan.Property) property.getDescription());
	}

	public <Property_> void setProperty(
			PropertyInterface<Property_>[] propertyArray) {
		
		org.qcg.broker.schemas.schedulingplan.Property tab[] = 
				new org.qcg.broker.schemas.schedulingplan.Property[propertyArray.length];
		
		for(int i = 0; i < tab.length; i++){
			tab[i] = (org.qcg.broker.schemas.schedulingplan.Property) propertyArray[i].getDescription();
		}
		
		this.pt.setProperty(tab);
	}

	public org.qcg.broker.schemas.schedulingplan.PropertiesType getDescription() {
		return this.pt;
	}

	public String getDocument() throws Exception {
		StringWriter writer = new StringWriter();
		try {
			pt.marshal(writer);
		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

}
