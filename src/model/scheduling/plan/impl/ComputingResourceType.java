package model.scheduling.plan.impl;

import java.io.StringWriter;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import model.scheduling.plan.ComputingResourceTypeInterface;
import model.scheduling.plan.ComputingResourceTypeItemInterface;


public class ComputingResourceType implements ComputingResourceTypeInterface <
									        org.qcg.broker.schemas.schedulingplan.ComputingResourceType> {

	private static final long serialVersionUID = 5254980770479844861L;
	protected org.qcg.broker.schemas.schedulingplan.ComputingResourceType rt;
	
	public ComputingResourceType(){
		rt = new org.qcg.broker.schemas.schedulingplan.ComputingResourceType();
	}
	
	public ComputingResourceType(org.qcg.broker.schemas.schedulingplan.ComputingResourceType value){
		rt = value;
	}
	
	public org.qcg.broker.schemas.schedulingplan.ComputingResourceType getDescription() {
		return rt;
	}

	public String getDocument() {
		StringWriter writer = new StringWriter();
		try {
			rt.marshal(writer);
		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	public <ComputingResourceTypeItem_> void addComputingResourceTypeItem(
			ComputingResourceTypeItemInterface<ComputingResourceTypeItem_> computingResourceTypeItem)
			throws IndexOutOfBoundsException {
		rt.addComputingResourceTypeItem((org.qcg.broker.schemas.schedulingplan.ComputingResourceTypeItem)computingResourceTypeItem.getDescription());
	}

	public <ComputingResourceTypeItem_> void addComputingResourceTypeItem(
			int index,
			ComputingResourceTypeItemInterface<ComputingResourceTypeItem_> computingResourceTypeItem)
			throws IndexOutOfBoundsException {
		rt.addComputingResourceTypeItem(index, (org.qcg.broker.schemas.schedulingplan.ComputingResourceTypeItem)computingResourceTypeItem.getDescription());
	}

	@SuppressWarnings("unchecked")
	public ComputingResourceTypeItemInterface<org.qcg.broker.schemas.schedulingplan.ComputingResourceTypeItem> getComputingResourceTypeItem(
			int index) throws IndexOutOfBoundsException {
		return new ComputingResourceTypeItem(rt.getComputingResourceTypeItem(index));
	}

	@SuppressWarnings("unchecked")
	public ComputingResourceTypeItemInterface<org.qcg.broker.schemas.schedulingplan.ComputingResourceTypeItem>[] getComputingResourceTypeItem() {
		org.qcg.broker.schemas.schedulingplan.ComputingResourceTypeItem items[] = rt.getComputingResourceTypeItem();
		if(items == null) return null;
		
		ComputingResourceTypeItem ret[] = new ComputingResourceTypeItem[items.length];
		for(int i = 0; i < items.length; i++){
			ret[i] = new ComputingResourceTypeItem(items[i]);
		}
		
		return ret;
	}

	public int getComputingResourceTypeItemCount() {
		return rt.getComputingResourceTypeItemCount();
	}

	public void removeAllComputingResourceTypeItem() {
		rt.removeAllComputingResourceTypeItem();
	}

	public <ComputingResourceTypeItem_> boolean removeComputingResourceTypeItem(
			ComputingResourceTypeItemInterface<ComputingResourceTypeItem_> computingResourceTypeItem) {
		return rt.removeComputingResourceTypeItem((org.qcg.broker.schemas.schedulingplan.ComputingResourceTypeItem)computingResourceTypeItem.getDescription());
	}

	@SuppressWarnings("unchecked")
	public ComputingResourceTypeItemInterface<org.qcg.broker.schemas.schedulingplan.ComputingResourceTypeItem> removeComputingResourceTypeItemAt(
			int index) {
		return new ComputingResourceTypeItem(rt.removeComputingResourceTypeItemAt(index));
	}

	public <ComputingResourceTypeItem_> void setComputingResourceTypeItem(
			int index,
			ComputingResourceTypeItemInterface<ComputingResourceTypeItem_> computingResourceTypeItem)
			throws IndexOutOfBoundsException {
		rt.setComputingResourceTypeItem(index, (org.qcg.broker.schemas.schedulingplan.ComputingResourceTypeItem) computingResourceTypeItem.getDescription());
	}

	public <ComputingResourceTypeItem_> void setComputingResourceTypeItem(
			ComputingResourceTypeItemInterface<ComputingResourceTypeItem_>[] computingResourceTypeItemArray) {
		if(computingResourceTypeItemArray == null) return;
		
		org.qcg.broker.schemas.schedulingplan.ComputingResourceTypeItem tab[] = 
			new org.qcg.broker.schemas.schedulingplan.ComputingResourceTypeItem[computingResourceTypeItemArray.length];
		
		for(int i = 0; i < computingResourceTypeItemArray.length; i++){
			tab[i] = (org.qcg.broker.schemas.schedulingplan.ComputingResourceTypeItem) computingResourceTypeItemArray[i].getDescription();
		}
		
		rt.setComputingResourceTypeItem(tab);
	}

}
