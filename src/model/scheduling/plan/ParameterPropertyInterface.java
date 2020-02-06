package model.scheduling.plan;

import model.DescriptionContainer;


public interface ParameterPropertyInterface<T> extends DescriptionContainer<T> {

	/**
	 * 
	 * 
	 * @param vValue
	 * @throws java.lang.IndexOutOfBoundsException if the index
	 * given is outside the bounds of the collection
	 */
	public void addValue(final java.lang.String vValue)
			throws java.lang.IndexOutOfBoundsException;

	/**
	 * 
	 * 
	 * @param index
	 * @param vValue
	 * @throws java.lang.IndexOutOfBoundsException if the index
	 * given is outside the bounds of the collection
	 */
	public void addValue(final int index, final java.lang.String vValue)
			throws java.lang.IndexOutOfBoundsException;

	/**
	 */
	public void deleteExclude();

	/**
	 * Returns the value of field 'exclude'.
	 * 
	 * @return the value of field 'Exclude'.
	 */
	public boolean getExclude();

	/**
	 * Returns the value of field 'name'.
	 * 
	 * @return the value of field 'Name'.
	 */
	public java.lang.String getName();

	/**
	 * Method getValue.
	 * 
	 * @param index
	 * @throws java.lang.IndexOutOfBoundsException if the index
	 * given is outside the bounds of the collection
	 * @return the value of the java.lang.String at the given index
	 */
	public java.lang.String getValue(final int index)
			throws java.lang.IndexOutOfBoundsException;

	/**
	 * Method getValue.Returns the contents of the collection in an
	 * Array.  <p>Note:  Just in case the collection contents are
	 * changing in another thread, we pass a 0-length Array of the
	 * correct type into the API call.  This way we <i>know</i>
	 * that the Array returned is of exactly the correct length.
	 * 
	 * @return this collection as an Array
	 */
	public java.lang.String[] getValue();

	/**
	 * Method getValueCount.
	 * 
	 * @return the size of this collection
	 */
	public int getValueCount();

	/**
	 * Method hasExclude.
	 * 
	 * @return true if at least one Exclude has been added
	 */
	public boolean hasExclude();

	/**
	 * Returns the value of field 'exclude'.
	 * 
	 * @return the value of field 'Exclude'.
	 */
	public boolean isExclude();

	/**
	 * Method isValid.
	 * 
	 * @return true if this object is valid according to the schema
	 */
	public boolean isValid();

	/**
	 */
	public void removeAllValue();

	/**
	 * Method removeValue.
	 * 
	 * @param vValue
	 * @return true if the object was removed from the collection.
	 */
	public boolean removeValue(final java.lang.String vValue);

	/**
	 * Method removeValueAt.
	 * 
	 * @param index
	 * @return the element removed from the collection
	 */
	public java.lang.String removeValueAt(final int index);

	/**
	 * Sets the value of field 'exclude'.
	 * 
	 * @param exclude the value of field 'exclude'.
	 */
	public void setExclude(final boolean exclude);

	/**
	 * Sets the value of field 'name'.
	 * 
	 * @param name the value of field 'name'.
	 */
	public void setName(final java.lang.String name);

	/**
	 * 
	 * 
	 * @param index
	 * @param vValue
	 * @throws java.lang.IndexOutOfBoundsException if the index
	 * given is outside the bounds of the collection
	 */
	public void setValue(final int index, final java.lang.String vValue)
			throws java.lang.IndexOutOfBoundsException;

	/**
	 * 
	 * 
	 * @param vValueArray
	 */
	public void setValue(final java.lang.String[] vValueArray);

}