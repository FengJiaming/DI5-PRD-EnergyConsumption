package model.scheduling.plan.impl;

import java.io.StringWriter;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import model.scheduling.plan.ComputingResourceTypeInterface;
import model.scheduling.plan.HostInterface;

public class Host implements HostInterface<org.qcg.broker.schemas.schedulingplan.Host> {

	private static final long serialVersionUID = -5534266040248107980L;
	protected org.qcg.broker.schemas.schedulingplan.Host host;
	
	public Host(){
		host = new org.qcg.broker.schemas.schedulingplan.Host();
	}
	
	public Host(org.qcg.broker.schemas.schedulingplan.Host value){
		host = value;
	}
	
	public org.qcg.broker.schemas.schedulingplan.Host getDescription() {
		return host;
	}

	public String getDocument() {
		StringWriter writer = new StringWriter();
		try {
			host.marshal(writer);
		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	@SuppressWarnings("unchecked")
	public ComputingResourceTypeInterface<org.qcg.broker.schemas.schedulingplan.ComputingResourceType> getMachineParameters() {
		org.qcg.broker.schemas.schedulingplan.ComputingResourceType crt = host.getMachineParameters();
		if(crt == null)
			return null;
		else
			return new ComputingResourceType(crt);
	}

	public String getHostname() {
		return host.getHostname();
	}


	public String getQueue() {
		return host.getQueue();
	}

	public <ComputingResourceType_> void setMachineParameters(
			ComputingResourceTypeInterface<ComputingResourceType_> machineParameters) {
		host.setMachineParameters((org.qcg.broker.schemas.schedulingplan.ComputingResourceType) machineParameters.getDescription());
	}

	public void setHostname(String hostname) {
		host.setHostname(hostname);
	}

	public void setQueue(String queue) {
		host.setQueue(queue);
	}

}
