package model.scheduling.plan.impl;

import java.io.StringWriter;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import model.scheduling.plan.ProcessesMapInterface;

public class ProcessesMap implements ProcessesMapInterface<org.qcg.broker.schemas.schedulingplan.ProcessesMap> {

	private org.qcg.broker.schemas.schedulingplan.ProcessesMap pm;
	
	public ProcessesMap(){
		this.pm = new org.qcg.broker.schemas.schedulingplan.ProcessesMap();
	}
	
	public ProcessesMap(org.qcg.broker.schemas.schedulingplan.ProcessesMap map){
		this.pm = map;
	}
	
	@Override
	public int[] getProcessesPerNode() {
		return pm.getProcessesPerNode();
	}

	@Override
	public int getSlotsPerNode() {
		return pm.getSlotsPerNode();
	}

	@Override
	public void setProcessesPerNode(int[] arg) {
		this.pm.setProcessesPerNode(arg);
	}

	@Override
	public void setSlotsPerNode(int arg) {
		this.pm.setSlotsPerNode(arg);
	}

	@Override
	public org.qcg.broker.schemas.schedulingplan.ProcessesMap getDescription() {
		return this.pm;
	}

	@Override
	public String getDocument() throws Exception {
		StringWriter sw = new StringWriter();
		try {
			pm.marshal(sw);
		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		}
		return sw.toString();
	}

}
