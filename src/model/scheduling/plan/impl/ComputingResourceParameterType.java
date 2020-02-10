package model.scheduling.plan.impl;

import java.io.StringWriter;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import org.qcg.broker.schemas.schedulingplan.types.ComputingParameterName;

import model.scheduling.plan.ComputingResourceParameterTypeInterface;
import model.scheduling.plan.ParameterPropertyInterface;

public class ComputingResourceParameterType implements
		ComputingResourceParameterTypeInterface<org.qcg.broker.schemas.schedulingplan.ComputingResourceParameterType> {

	private static final long serialVersionUID = 7369276462976219345L;
	protected org.qcg.broker.schemas.schedulingplan.ComputingResourceParameterType pt;

	public ComputingResourceParameterType() {
		pt = new org.qcg.broker.schemas.schedulingplan.ComputingResourceParameterType();
	}

	public ComputingResourceParameterType(org.qcg.broker.schemas.schedulingplan.ComputingResourceParameterType value) {
		pt = value;
	}

	public org.qcg.broker.schemas.schedulingplan.ComputingResourceParameterType getDescription() {
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

	public ComputingParameterName getName() {
		return pt.getName();
	}

	public void setName(ComputingParameterName name) {
		pt.setName(name);
	}

	public void addParamValue(String paramValue) throws IndexOutOfBoundsException {
		pt.addParamValue(paramValue);
	}

	public void addParamValue(int index, String paramValue) throws IndexOutOfBoundsException {
		pt.addParamValue(index, paramValue);
	}

	public <Property_> void addProperty(ParameterPropertyInterface<Property_> property)
			throws IndexOutOfBoundsException {
		pt.addParameterProperty((org.qcg.broker.schemas.schedulingplan.ParameterProperty) property.getDescription());
	}

	public <Property_> void addProperty(int index, ParameterPropertyInterface<Property_> property)
			throws IndexOutOfBoundsException {
		pt.addParameterProperty(index,
				(org.qcg.broker.schemas.schedulingplan.ParameterProperty) property.getDescription());
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
		if (tab == null)
			return null;

		model.scheduling.plan.impl.ParameterProperty ret[] = new model.scheduling.plan.impl.ParameterProperty[tab.length];
		for (int i = 0; i < tab.length; i++) {
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
		return pt.removeParameterProperty(
				(org.qcg.broker.schemas.schedulingplan.ParameterProperty) property.getDescription());
	}

	@SuppressWarnings("unchecked")
	public ParameterPropertyInterface<org.qcg.broker.schemas.schedulingplan.ParameterProperty> removePropertyAt(
			int index) {
		return new model.scheduling.plan.impl.ParameterProperty(pt.removeParameterPropertyAt(index));
	}

	public void setParamValue(int index, String paramValue) throws IndexOutOfBoundsException {
		pt.setParamValue(index, paramValue);
	}

	public void setParamValue(String[] paramValueArray) {
		pt.setParamValue(paramValueArray);
	}

	public <Property_> void setProperty(int index, ParameterPropertyInterface<Property_> property)
			throws IndexOutOfBoundsException {
		pt.setParameterProperty(index,
				(org.qcg.broker.schemas.schedulingplan.ParameterProperty) property.getDescription());
	}

	public <Property_> void setProperty(ParameterPropertyInterface<Property_>[] propertyArray) {
		if (propertyArray == null)
			return;
		org.qcg.broker.schemas.schedulingplan.ParameterProperty tab[] = new org.qcg.broker.schemas.schedulingplan.ParameterProperty[propertyArray.length];
		for (int i = 0; i < propertyArray.length; i++) {
			tab[i] = (org.qcg.broker.schemas.schedulingplan.ParameterProperty) propertyArray[i].getDescription();
		}
		pt.setParameterProperty(tab);
	}

}
