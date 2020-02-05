package model;


public interface DescriptionContainer<T> {

	/**
	 * @return an object of a class that represents the description 
	 */
	public T getDescription();

	/**
	 * Provides the description as an original document
	 * @return string representation of the description
	 */
	public String getDocument() throws Exception;
	
	/**
	 * Calls {@link #getDocument} method
	 * @return string representation of the description
	 */
	public String toString();
}
