package model.scheduling.plan;

import java.util.Map;

import model.DescriptionContainer;
import model.resources.units.ResourceUnit;
import model.resources.units.ResourceUnitName;


public interface AllocationInterface<T> extends DescriptionContainer<T> {

	/**
	 */
	public void deleteProcessQuantity();

	/**
	 * Returns the value of field 'additionalProperties'.
	 * 
	 * @return the value of field 'AdditionalProperties'.
	 */
	public <Properties> PropertiesTypeInterface<Properties> getAdditionalProperties();

	/**
	 * Returns the value of field 'host'. The field 'host' has the
	 * following description: Submission host
	 * 
	 * @return the value of field 'Host'.
	 */
	public  HostInterface getHost();

	/**
	 * Returns the value of field 'processGroupId'.
	 * 
	 * @return the value of field 'ProcessGroupId'.
	 */
	public java.lang.String getProcessGroupId();

	/**
	 * Returns the value of field 'processQuantity'.
	 * 
	 * @return the value of field 'ProcessQuantity'.
	 */
	public int getProcessesCount();
	
	public <ProcessesMap_>ProcessesMapInterface<ProcessesMap_> getProcessesMap();

	/**
	 * Returns the value of field 'providerInfo'.
	 * 
	 * @return the value of field 'ProviderInfo'.
	 */
	public <ProviderInfo>ProviderInfoInterface<ProviderInfo> getProviderInfo();
	
	/**
	 * Returns the value of field 'reservationId'. The field
	 * 'reservationId' has the following description: Reservation
	 * identifier in local system 
	 * 
	 * @return the value of field 'ReservationId'.
	 */
	public java.lang.String getReservationId();

	/**
	 * Method hasProcessQuantity.
	 * 
	 * @return true if at least one ProcessQuantity has been added
	 */
	public boolean hasProcessesCount();
	
	public boolean hasProcessesMap();

	/**
	 * Sets the value of field 'additionalProperties'.
	 * 
	 * @param additionalProperties the value of field
	 * 'additionalProperties'.
	 */
	public <Properties> void setAdditionalProperties(
			final PropertiesTypeInterface<Properties> additionalProperties);

	/**
	 * Sets the value of field 'host'. The field 'host' has the
	 * following description: Submission host
	 * 
	 * @param host the value of field 'host'.
	 */
	public <Host> void setHost(final HostInterface<Host> host);

	/**
	 * Sets the value of field 'processGroupID'.
	 * 
	 * @param processGroupId the value of field 'processGroupId'.
	 */
	public void setProcessGroupId(final java.lang.String processGroupId);

	/**
	 * Sets the value of field 'processQuantity'.
	 * 
	 * @param processQuantity the value of field 'processQuantity'.
	 */
	public void setProcessesCount(final int processesCount);
	
	
	public <ProcessesMap_> void setProcessesMap(ProcessesMapInterface<ProcessesMap_> processesMap);

	/**
	 * Sets the value of field 'providerInfo'.
	 * 
	 * @param providerInfo the value of field 'providerInfo'.
	 */
	public <ProviderInfo> void setProviderInfo(ProviderInfoInterface<ProviderInfo> providerInfo);
	
	public void setRequestedResources(Map<ResourceUnitName, ResourceUnit> choosenResources);
	
	public Map<ResourceUnitName, ResourceUnit> getRequestedResources();
	
	public void setProviderName(String providerName);
	
	public String getProviderName();
	
	//public boolean isProcessing();
}