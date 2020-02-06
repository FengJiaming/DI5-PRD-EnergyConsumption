package model.scheduling.plan.impl;

import org.qcg.broker.schemas.schedulingplan.AllocationChoice;
import org.qcg.broker.schemas.schedulingplan.Node;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import model.resources.units.ProcessingElements;
import model.resources.units.ResourceUnit;
import model.resources.units.ResourceUnitName;
import model.resources.units.StandardResourceUnitName;
import model.scheduling.plan.AllocationInterface;
import model.scheduling.plan.HostInterface;
import model.scheduling.plan.ProcessesMapInterface;
import model.scheduling.plan.PropertiesTypeInterface;
import model.scheduling.plan.ProviderInfoInterface;

public class Allocation implements AllocationInterface<org.qcg.broker.schemas.schedulingplan.Allocation> {

	private static final long serialVersionUID = 5194463015598737779L;
	protected org.qcg.broker.schemas.schedulingplan.Allocation allocation;
	
	public Allocation(){
		allocation = new org.qcg.broker.schemas.schedulingplan.Allocation();
	}
	
	public Allocation(org.qcg.broker.schemas.schedulingplan.Allocation value){
		allocation = value;
	}
	
	public org.qcg.broker.schemas.schedulingplan.Allocation getDescription() {
		return allocation;
	}

	public String getDocument() {
		StringWriter sw = new StringWriter();
		try {
			allocation.marshal(sw);
		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		}
		return sw.toString();
	}

	public void deleteProcessQuantity() {
		allocation.getAllocationChoice().deleteProcessesCount();
	}

	@SuppressWarnings("unchecked")
	public PropertiesTypeInterface<org.qcg.broker.schemas.schedulingplan.PropertiesType> getAdditionalProperties() {
		PropertiesType api = 
			new PropertiesType(allocation.getAdditionalProperties());
		return api;
	}

	@SuppressWarnings("unchecked")
	public HostInterface<org.qcg.broker.schemas.schedulingplan.Host> getHost() {
		HostInterface<org.qcg.broker.schemas.schedulingplan.Host> host = new model.scheduling.plan.impl.Host(allocation.getHost());
		return host;
	}

	public String getProcessGroupId() {
		return allocation.getProcessGroupId();
	}

	public int getProcessesCount() {
		return allocation.getAllocationChoice().getProcessesCount();
	}
	
	@SuppressWarnings("unchecked")
	public ProcessesMapInterface<org.qcg.broker.schemas.schedulingplan.ProcessesMap> getProcessesMap(){
		ProcessesMapInterface<org.qcg.broker.schemas.schedulingplan.ProcessesMap> map = 
			new model.scheduling.plan.impl.ProcessesMap(allocation.getAllocationChoice().getProcessesMap());
		return map;
	}

	public boolean hasProcessesCount() {
		return allocation.getAllocationChoice().hasProcessesCount();
	}
	
	public boolean hasProcessesMap(){
		return (allocation.getAllocationChoice().getProcessesMap() != null);
	}

	public <Properties_> void setAdditionalProperties(
				PropertiesTypeInterface<Properties_> additionalProperties) {
		allocation.setAdditionalProperties((org.qcg.broker.schemas.schedulingplan.PropertiesType)additionalProperties.getDescription());
	}

	public <Host_> void setHost(HostInterface<Host_> host) {
		allocation.setHost((org.qcg.broker.schemas.schedulingplan.Host)host.getDescription());
	}

	public void setProcessGroupId(String processGroupId) {
		allocation.setProcessGroupId(processGroupId);
	}

	public void setProcessesCount(int processCount) {
		AllocationChoice choice = new AllocationChoice();
		choice.setProcessesCount(processCount);
		allocation.setAllocationChoice(choice);
	}

	public <ProcessesMap_> void setProcessesMap(ProcessesMapInterface<ProcessesMap_> processesMap){
		AllocationChoice choice = new AllocationChoice();
		choice.setProcessesMap(
				(org.qcg.broker.schemas.schedulingplan.ProcessesMap) processesMap.getDescription());
		allocation.setAllocationChoice(choice);
		
	}

	@SuppressWarnings("unchecked")
	public ProviderInfoInterface<org.qcg.broker.schemas.schedulingplan.ProviderInfo> getProviderInfo() {
		org.qcg.broker.schemas.schedulingplan.ProviderInfo info = allocation.getProviderInfo();
		if(info == null)
			return null;
		
		ProviderInfoInterface providerInfo = new ProviderInfo(info);
		return providerInfo;
	}
	
	
	public String getReservationId() {
		org.qcg.broker.schemas.schedulingplan.Reservation r = allocation.getReservation();
		return (r == null ? null : r.getId());
	}

	public <ProviderInfo_> void setProviderInfo(
			ProviderInfoInterface<ProviderInfo_> providerInfo) {
		allocation.setProviderInfo((org.qcg.broker.schemas.schedulingplan.ProviderInfo)providerInfo.getDescription());
	}
	

	protected Map<ResourceUnitName, ResourceUnit> requestedResources;
	//protected boolean isProcessing = false;
	protected String providerName;

	@Override
	public void setRequestedResources(
			Map<ResourceUnitName, ResourceUnit> choosenResources) {
		requestedResources = choosenResources;
		if(choosenResources != null){
			ResourceManagerUtils.setPendingResources(choosenResources);
			/*ProcessingElements processingElements = (ProcessingElements)requestedResources.get(StandardResourceUnitName.PE);
			if(processingElements != null){
				isProcessing = true;
			}	*/
		}

	}

	@Override
	public Map<ResourceUnitName, ResourceUnit> getRequestedResources() {
		return this.requestedResources;
	}

	/*public boolean isProcessing(){
		return isProcessing;
	}*/

	@Override
	public void setProviderName(String providerName) {
		this.providerName = providerName;	
	}

	@Override
	public String getProviderName() {
		return providerName;
	}


}
