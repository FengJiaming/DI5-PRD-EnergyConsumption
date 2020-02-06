package model.scheduling.plan;

import model.DescriptionContainer;


public interface ComputingResourceTypeInterface <T> extends DescriptionContainer<T> {

	/**
	 * 
	 * 
	 * @param vComputingResourceTypeItem
	 * @throws java.lang.IndexOutOfBoundsException if the index
	 * given is outside the bounds of the collection
	 */
	public <ComputingResourceTypeItem> void addComputingResourceTypeItem(
			final ComputingResourceTypeItemInterface<ComputingResourceTypeItem> vComputingResourceTypeItem)
			throws java.lang.IndexOutOfBoundsException;

	/**
	 * 
	 * 
	 * @param index
	 * @param vComputingResourceTypeItem
	 * @throws java.lang.IndexOutOfBoundsException if the index
	 * given is outside the bounds of the collection
	 */
	public <ComputingResourceTypeItem> void addComputingResourceTypeItem(
			final int index,
			final ComputingResourceTypeItemInterface<ComputingResourceTypeItem> vComputingResourceTypeItem)
			throws java.lang.IndexOutOfBoundsException;

	/**
	 * Method getComputingResourceTypeItem.
	 * 
	 * @param index
	 * @throws java.lang.IndexOutOfBoundsException if the index
	 * given is outside the bounds of the collection
	 * @return the value of the
	 * schedframe.schedulingplan.impl.ComputingResourceTypeItem at
	 * the given index
	 */
	public <ComputingResourceTypeItem> ComputingResourceTypeItemInterface<ComputingResourceTypeItem> 
			getComputingResourceTypeItem(
			final int index) throws java.lang.IndexOutOfBoundsException;

	/**
	 * Method getComputingResourceTypeItem.Returns the contents of
	 * the collection in an Array.  <p>Note:  Just in case the
	 * collection contents are changing in another thread, we pass
	 * a 0-length Array of the correct type into the API call. 
	 * This way we <i>know</i> that the Array returned is of
	 * exactly the correct length.
	 * 
	 * @return this collection as an Array
	 */
	public <ComputingResourceTypeItem> ComputingResourceTypeItemInterface<ComputingResourceTypeItem>[] getComputingResourceTypeItem();

	/**
	 * Method getComputingResourceTypeItemCount.
	 * 
	 * @return the size of this collection
	 */
	public int getComputingResourceTypeItemCount();

	/**
	 */
	public void removeAllComputingResourceTypeItem();

	/**
	 * Method removeComputingResourceTypeItem.
	 * 
	 * @param vComputingResourceTypeItem
	 * @return true if the object was removed from the collection.
	 */
	public <ComputingResourceTypeItem> boolean removeComputingResourceTypeItem(
			final ComputingResourceTypeItemInterface<ComputingResourceTypeItem> vComputingResourceTypeItem);

	/**
	 * Method removeComputingResourceTypeItemAt.
	 * 
	 * @param index
	 * @return the element removed from the collection
	 */
	public <ComputingResourceTypeItem> ComputingResourceTypeItemInterface<ComputingResourceTypeItem> removeComputingResourceTypeItemAt(
			final int index);

	/**
	 * 
	 * 
	 * @param index
	 * @param vComputingResourceTypeItem
	 * @throws java.lang.IndexOutOfBoundsException if the index
	 * given is outside the bounds of the collection
	 */
	public <ComputingResourceTypeItem> void setComputingResourceTypeItem(
			final int index,
			final ComputingResourceTypeItemInterface<ComputingResourceTypeItem> vComputingResourceTypeItem)
			throws java.lang.IndexOutOfBoundsException;

	/**
	 * 
	 * 
	 * @param vComputingResourceTypeItemArray
	 */
	public <ComputingResourceTypeItem> void setComputingResourceTypeItem(
			final ComputingResourceTypeItemInterface<ComputingResourceTypeItem>[] vComputingResourceTypeItemArray);

}