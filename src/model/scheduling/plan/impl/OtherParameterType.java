package model.scheduling.plan.impl;

import java.io.StringWriter;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import model.scheduling.plan.OtherParameterTypeInterface;
import model.scheduling.plan.ParameterPropertyInterface;



public class OtherParameterType	implements OtherParameterTypeInterface <org.qcg.broker.schemas.schedulingplan.OtherParameterType>{

	private static final long serialVersionUID = 6601332996207982911L;
	protected org.qcg.broker.schemas.schedulingplan.OtherParameterType pt;
	
	public OtherParameterType(){
		pt = new org.qcg.broker.schemas.schedulingplan.OtherParameterType();
	}
	
	public OtherParameterType(org.qcg.broker.schemas.schedulingplan.OtherParameterType value){
		pt = value; 
	}
	
	public org.qcg.broker.schemas.schedulingplan.OtherParameterType getDescription() {
		return pt;
	}

	public String getDocument() {
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

	public String getEndpoint() {
		return pt.getEndpoint();
	}

	public String getName() {
		return pt.getName();
	}

	public void setEndpoint(String endpoint) {
		pt.setEndpoint(endpoint);
	}

	public void setName(String name) {
		pt.setName(name);
	}

	public void addParamValue(String paramValue)
	throws IndexOutOfBoundsException {
		pt.addParamValue(paramValue);
	}
	
	public void addParamValue(int index, String paramValue)
		throws IndexOutOfBoundsException {
		pt.addParamValue(index, paramValue);
	}
	
	public <Property_> void addProperty(ParameterPropertyInterface<Property_> property)
		throws IndexOutOfBoundsException {
		pt.addParameterProperty((org.qcg.broker.schemas.schedulingplan.ParameterProperty)property.getDescription());
	}
	
	public <Property_> void addProperty(int index, ParameterPropertyInterface<Property_> property)
		throws IndexOutOfBoundsException {
		pt.addParameterProperty(index, (org.qcg.broker.schemas.schedulingplan.ParameterProperty) property.getDescription());
	}
	
	public String getParamValue(int index) throws IndexOutOfBoundsException {
		return pt.getParamValue(index);
	}
	
	public String[] getParamValue() {
		return pt.getParamValue();
	}
	
	public int getParamValueCount() {
		return pt.getParamValueCount();
	}
	
	@SuppressWarnings("unchecked")
	public ParameterPropertyInterface<org.qcg.broker.schemas.schedulingplan.ParameterProperty> getProperty(int index)
		throws IndexOutOfBoundsException {
		return new model.scheduling.plan.impl.ParameterProperty(pt.getParameterProperty(index));
	}
	
	@SuppressWarnings("unchecked")
	public ParameterPropertyInterface<org.qcg.broker.schemas.schedulingplan.ParameterProperty>[] getProperty() {
		org.qcg.broker.schemas.schedulingplan.ParameterProperty tab[] = pt.getParameterProperty();
		if(tab == null) return null;
		
		model.scheduling.plan.impl.ParameterProperty ret[] = new model.scheduling.plan.impl.ParameterProperty[tab.length];
		for(int i = 0; i < tab.length; i++){
			ret[i] = new model.scheduling.plan.impl.ParameterProperty(tab[i]);
		}
		
		return ret;
	}
	
	public int getPropertyCount() {
		return pt.getParameterPropertyCount();
	}
	
	public void removeAllParamValue() {
		pt.removeAllParamValue();
	}
	
	public void removeAllProperty() {
		pt.removeAllParameterProperty();
	}
	
	public boolean removeParamValue(String paramValue) {
		return pt.removeParamValue(paramValue);
	}
	
	public String removeParamValueAt(int index) {
		return pt.removeParamValueAt(index);
	}
	
	public <Property_> boolean removeProperty(ParameterPropertyInterface<Property_> property) {
		return pt.removeParameterProperty((org.qcg.broker.schemas.schedulingplan.ParameterProperty)property.getDescription());
	}
	
	@SuppressWarnings("unchecked")
	public ParameterPropertyInterface<org.qcg.broker.schemas.schedulingplan.ParameterProperty> removePropertyAt(int index) {
		return new model.scheduling.plan.impl.ParameterProperty(pt.removeParameterPropertyAt(index));
	}
	
	public void setParamValue(int index, String paramValue)
		throws IndexOutOfBoundsException {
		pt.setParamValue(index, paramValue);
	}
	
	public void setParamValue(String[] paramValueArray) {
		pt.setParamValue(paramValueArray);
	}
	
	public <Property_> void setProperty(int index, ParameterPropertyInterface<Property_> property)
		throws IndexOutOfBoundsException {
		pt.setParameterProperty(index, (org.qcg.broker.schemas.schedulingplan.ParameterProperty)property.getDescription());
	}
	
	public <Property_> void setProperty(ParameterPropertyInterface<Property_>[] propertyArray) {
		if(propertyArray == null)
			return;
		org.qcg.broker.schemas.schedulingplan.ParameterProperty tab[] = new org.qcg.broker.schemas.schedulingplan.ParameterProperty[propertyArray.length];
		for(int i = 0; i < propertyArray.length; i++){
			tab[i] = (org.qcg.broker.schemas.schedulingplan.ParameterProperty) propertyArray[i].getDescription();
		}
		pt.setParameterProperty(tab);
	}
	
}
