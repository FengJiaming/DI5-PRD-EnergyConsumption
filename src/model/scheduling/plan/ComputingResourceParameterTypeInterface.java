package model.scheduling.plan;

import org.qcg.broker.schemas.schedulingplan.types.ComputingParameterName;

public interface ComputingResourceParameterTypeInterface<T> extends ParameterTypeInterface<T> {

	/**
	 * Returns the value of field 'name'.
	 * 
	 * @return the value of field 'Name'.
	 */
	public ComputingParameterName getName();

	/**
	 * Sets the value of field 'name'.
	 * 
	 * @param name the value of field 'name'.
	 */
	public void setName(final ComputingParameterName name);

}