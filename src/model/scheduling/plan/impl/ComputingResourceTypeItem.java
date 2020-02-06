package model.scheduling.plan.impl;

import model.scheduling.plan.ComputingResourceParameterTypeInterface;
import schedframe.scheduling.plan.ComputingResourceTypeItemInterface;
import schedframe.scheduling.plan.OtherParameterTypeInterface;

public class ComputingResourceTypeItem 
			implements ComputingResourceTypeItemInterface <org.qcg.broker.schemas.schedulingplan.ComputingResourceTypeItem> {

	private static final long serialVersionUID = -8559307821866515716L;
	protected org.qcg.broker.schemas.schedulingplan.ComputingResourceTypeItem ti;
	
	public ComputingResourceTypeItem(){
		ti = new org.qcg.broker.schemas.schedulingplan.ComputingResourceTypeItem();
	}
	
	public ComputingResourceTypeItem(org.qcg.broker.schemas.schedulingplan.ComputingResourceTypeItem value){
		ti = value;
	}
	
	public org.qcg.broker.schemas.schedulingplan.ComputingResourceTypeItem getDescription() {
		return ti;
	}

	public String getDocument() {
		return ""; 
	}

	public Object getChoiceValue() {
		return ti.getChoiceValue();
	}

	@SuppressWarnings("unchecked")
	public ComputingResourceParameterTypeInterface<org.qcg.broker.schemas.schedulingplan.ComputingResourceParameterType> getHostParameter() {
		return new schedframe.scheduling.plan.impl.ComputingResourceParameterType(ti.getHostParameter());
	}

	@SuppressWarnings("unchecked")
	public OtherParameterTypeInterface<org.qcg.broker.schemas.schedulingplan.OtherParameterType> getOtherParameter() {
		return new schedframe.scheduling.plan.impl.OtherParameterType(ti.getOtherParameter());
	}

	public <ComputingResourceParameterType_> void setHostParameter(
			ComputingResourceParameterTypeInterface<ComputingResourceParameterType_> hostParameter) {
		ti.setHostParameter((org.qcg.broker.schemas.schedulingplan.ComputingResourceParameterType) hostParameter.getDescription());
	}

	public <OtherParameterType_> void setOtherParameter(
			OtherParameterTypeInterface<OtherParameterType_> otherParameter) {
		ti.setOtherParameter((org.qcg.broker.schemas.schedulingplan.OtherParameterType) otherParameter.getDescription());
	}

}
