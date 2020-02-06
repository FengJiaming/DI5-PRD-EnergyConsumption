package model.scheduling.plan;

import model.DescriptionContainer;


public interface ComputingResourceTypeItemInterface<T> extends DescriptionContainer<T> {

	/**
	 * Returns the value of field 'choiceValue'. The field
	 * 'choiceValue' has the following description: Internal choice
	 * value storage
	 * 
	 * @return the value of field 'ChoiceValue'.
	 */
	public java.lang.Object getChoiceValue();

	/**
	 * Returns the value of field 'hostParameter'.
	 * 
	 * @return the value of field 'HostParameter'.
	 */
	public <ComputingResourceParameterType> ComputingResourceParameterTypeInterface<ComputingResourceParameterType> getHostParameter();


	/**
	 * Returns the value of field 'otherParameter'.
	 * 
	 * @return the value of field 'OtherParameter'.
	 */
	public <OtherParameterType> OtherParameterTypeInterface<OtherParameterType> getOtherParameter();

	/**
	 * Sets the value of field 'hostParameter'.
	 * 
	 * @param hostParameter the value of field 'hostParameter'.
	 */
	public <ComputingResourceParameterType> void setHostParameter(
			final ComputingResourceParameterTypeInterface<ComputingResourceParameterType> hostParameter);

	/**
	 * Sets the value of field 'otherParameter'.
	 * 
	 * @param otherParameter the value of field 'otherParameter'.
	 */
	public <OtherParameterType> void setOtherParameter(
			final OtherParameterTypeInterface<OtherParameterType> otherParameter);

}