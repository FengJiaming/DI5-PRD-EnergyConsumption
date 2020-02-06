package model.scheduling.plan;

import model.DescriptionContainer;


public interface HostInterface<T> extends DescriptionContainer<T> {

	/**
	 * Returns the value of field 'frontendMachineParameters'.
	 * 
	 * @return the value of field 'FrontendMachineParameters'.
	 */
	public <ComputingResourcType> ComputingResourceTypeInterface<ComputingResourcType> getMachineParameters();

	/**
	 * Returns the value of field 'hostname'.
	 * 
	 * @return the value of field 'Hostname'.
	 */
	public java.lang.String getHostname();


	/**
	 * Returns the value of field 'queue'.
	 * 
	 * @return the value of field 'Queue'.
	 */
	public java.lang.String getQueue();

	/**
	 * Sets the value of field 'frontendMachineParameters'.
	 * 
	 * @param frontendMachineParameters the value of field
	 * 'frontendMachineParameters'.
	 */
	public <ComputingResourceType> void setMachineParameters(
			final ComputingResourceTypeInterface<ComputingResourceType> machineParameters);

	/**
	 * Sets the value of field 'hostname'.
	 * 
	 * @param hostname the value of field 'hostname'.
	 */
	public void setHostname(final java.lang.String hostname);


	/**
	 * Sets the value of field 'queue'.
	 * 
	 * @param queue the value of field 'queue'.
	 */
	public void setQueue(final java.lang.String queue);

}