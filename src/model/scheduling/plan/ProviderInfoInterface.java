package model.scheduling.plan;

import model.DescriptionContainer;

import org.qcg.broker.schemas.schedulingplan.types.InterfaceType;

public interface ProviderInfoInterface<T> extends DescriptionContainer<T> {

	/**
	 * Returns the value of field 'location'.
	 * 
	 * @return the value of field 'Location'.
	 */
	public java.lang.String getLocation();

	/**
	 * Returns the value of field 'proxyName'.
	 * 
	 * @return the value of field 'ProxyName'.
	 */
	public java.lang.String getProxyName();

	/**
	 * Returns the value of field 'type'.
	 * 
	 * @return the value of field 'Type'.
	 */
	public InterfaceType getType();
	
	/**
	 * Sets the value of field 'location'.
	 * 
	 * @param location the value of field 'location'.
	 */
	public void setLocation(final java.lang.String location);

	/**
	 * Sets the value of field 'proxyName'.
	 * 
	 * @param proxyName the value of field 'proxyName'.
	 */
	public void setProxyName(final java.lang.String proxyName);

	/**
	 * Sets the value of field 'type'.
	 * 
	 * @param type the value of field 'type'.
	 */
	public void setType(final InterfaceType type);
	
	public <Properties> void setAdditionalProperties(
            final PropertiesTypeInterface<Properties> additionalProperties);
	
	public <Properties> PropertiesTypeInterface<Properties> getAdditionalProperties();

}