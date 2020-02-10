package model.scheduling.plan.impl;

import org.qcg.broker.schemas.schedulingplan.types.InterfaceType;

import java.io.StringWriter;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import model.scheduling.plan.PropertiesTypeInterface;
import model.scheduling.plan.ProviderInfoInterface;


public class ProviderInfo implements ProviderInfoInterface<org.qcg.broker.schemas.schedulingplan.ProviderInfo> {

	private static final long serialVersionUID = -1629036434099074699L;
	private org.qcg.broker.schemas.schedulingplan.ProviderInfo pi;
	
	public ProviderInfo(){
		pi = new org.qcg.broker.schemas.schedulingplan.ProviderInfo();
	}
	
	public ProviderInfo(org.qcg.broker.schemas.schedulingplan.ProviderInfo info){
		this.pi = info;
	}

	public org.qcg.broker.schemas.schedulingplan.ProviderInfo getDescription() {
		pi.getAdditionalProperties();
		return pi;
	}
	
	public String getDocument() {
		StringWriter writer = new StringWriter();
		try {
			pi.marshal(writer);
		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	public String getLocation() {
		return pi.getLocation();
	}

	public String getProxyName() {
		return pi.getProxyName();
	}

	public InterfaceType getType() {
		return pi.getType();
	}

	public void setLocation(String location) {
		pi.setLocation(location);
	}

	public void setProxyName(String proxyName) {
		pi.setProxyName(proxyName);
	}

	public void setType(InterfaceType type) {
		pi.setType(type);
	}

	@SuppressWarnings("unchecked")
	public PropertiesTypeInterface<org.qcg.broker.schemas.schedulingplan.PropertiesType> getAdditionalProperties() {
		org.qcg.broker.schemas.schedulingplan.PropertiesType properties = this.pi.getAdditionalProperties();
		PropertiesType ret = new PropertiesType(properties);
		return ret;
	}

	public <Properties_> void setAdditionalProperties(
			PropertiesTypeInterface<Properties_> additionalProperties) {
		this.pi.setAdditionalProperties(
					(org.qcg.broker.schemas.schedulingplan.PropertiesType) additionalProperties.getDescription());
	}

}
