package model.scheduling.plan;

import model.DescriptionContainer;


public interface ParameterTypeInterface<T> extends DescriptionContainer<T> {

	/**
	 * 
	 * 
	 * @param vParamValue
	 * @throws java.lang.IndexOutOfBoundsException if the index
	 * given is outside the bounds of the collection
	 */
	public void addParamValue(final java.lang.String vParamValue)
			throws java.lang.IndexOutOfBoundsException;

	/**
	 * 
	 * 
	 * @param index
	 * @param vParamValue
	 * @throws java.lang.IndexOutOfBoundsException if the index
	 * given is outside the bounds of the collection
	 */
	public void addParamValue(final int index,
			final java.lang.String vParamValue)
			throws java.lang.IndexOutOfBoundsException;

	/**
	 * 
	 * 
	 * @param vProperty
	 * @throws java.lang.IndexOutOfBoundsException if the index
	 * given is outside the bounds of the collection
	 */
	public <Property> void addProperty(
			final ParameterPropertyInterface<Property> vProperty)
			throws java.lang.IndexOutOfBoundsException;

	/**
	 * 
	 * 
	 * @param index
	 * @param vProperty
	 * @throws java.lang.IndexOutOfBoundsException if the index
	 * given is outside the bounds of the collection
	 */
	public <Property> void addProperty(final int index,
			final ParameterPropertyInterface<Property> vProperty)
			throws java.lang.IndexOutOfBoundsException;


	/**
	 * Method getParamValue.
	 * 
	 * @param index
	 * @throws java.lang.IndexOutOfBoundsException if the index
	 * given is outside the bounds of the collection
	 * @return the value of the java.lang.String at the given index
	 */
	public java.lang.String getParamValue(final int index)
			throws java.lang.IndexOutOfBoundsException;

	/**
	 * Method getParamValue.Returns the contents of the collection
	 * in an Array.  <p>Note:  Just in case the collection contents
	 * are changing in another thread, we pass a 0-length Array of
	 * the correct type into the API call.  This way we <i>know</i>
	 * that the Array returned is of exactly the correct length.
	 * 
	 * @return this collection as an Array
	 */
	public java.lang.String[] getParamValue();

	/**
	 * Method getParamValueCount.
	 * 
	 * @return the size of this collection
	 */
	public int getParamValueCount();

	/**
	 * Method getProperty.
	 * 
	 * @param index
	 * @throws java.lang.IndexOutOfBoundsException if the index
	 * given is outside the bounds of the collection
	 * @return the value of the
	 * schedframe.schedulingplan.impl.Property at the given index
	 */
	public <Property> ParameterPropertyInterface<Property> getProperty(final int index)
			throws java.lang.IndexOutOfBoundsException;

	/**
	 * Method getProperty.Returns the contents of the collection in
	 * an Array.  <p>Note:  Just in case the collection contents
	 * are changing in another thread, we pass a 0-length Array of
	 * the correct type into the API call.  This way we <i>know</i>
	 * that the Array returned is of exactly the correct length.
	 * 
	 * @return this collection as an Array
	 */
	public <Property> ParameterPropertyInterface<Property>[] getProperty();

	/**
	 * Method getPropertyCount.
	 * 
	 * @return the size of this collection
	 */
	public int getPropertyCount();


	/**
	 */
	public void removeAllParamValue();

	/**
	 */
	public void removeAllProperty();


	/**
	 * Method removeParamValue.
	 * 
	 * @param vParamValue
	 * @return true if the object was removed from the collection.
	 */
	public boolean removeParamValue(final java.lang.String vParamValue);

	/**
	 * Method removeParamValueAt.
	 * 
	 * @param index
	 * @return the element removed from the collection
	 */
	public java.lang.String removeParamValueAt(final int index);

	/**
	 * Method removeProperty.
	 * 
	 * @param vProperty
	 * @return true if the object was removed from the collection.
	 */
	public <Property> boolean removeProperty(
			final ParameterPropertyInterface<Property> vProperty);

	/**
	 * Method removePropertyAt.
	 * 
	 * @param index
	 * @return the element removed from the collection
	 */
	public <Property> ParameterPropertyInterface<Property> removePropertyAt(
			final int index);

	/**
	 * 
	 * 
	 * @param index
	 * @param vParamValue
	 * @throws java.lang.IndexOutOfBoundsException if the index
	 * given is outside the bounds of the collection
	 */
	public void setParamValue(final int index,
			final java.lang.String vParamValue)
			throws java.lang.IndexOutOfBoundsException;

	/**
	 * 
	 * 
	 * @param vParamValueArray
	 */
	public void setParamValue(final java.lang.String[] vParamValueArray);

	/**
	 * 
	 * 
	 * @param index
	 * @param vProperty
	 * @throws java.lang.IndexOutOfBoundsException if the index
	 * given is outside the bounds of the collection
	 */
	public <Property> void setProperty(final int index,
			final ParameterPropertyInterface<Property> vProperty)
			throws java.lang.IndexOutOfBoundsException;

	/**
	 * 
	 * 
	 * @param vPropertyArray
	 */
	public <Property> void setProperty(
			final ParameterPropertyInterface<Property>[] vPropertyArray);


}