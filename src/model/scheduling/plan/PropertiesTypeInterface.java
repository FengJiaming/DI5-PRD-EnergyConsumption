package model.scheduling.plan;


import model.DescriptionContainer;

public interface PropertiesTypeInterface<T> extends DescriptionContainer<T> {


   /**
    * 
    * 
    * @param vProperty
    * @throws java.lang.IndexOutOfBoundsException if the index
    * given is outside the bounds of the collection
    */
   public <Property> void addProperty(
           final PropertyInterface<Property> vProperty);

   /**
    * 
    * 
    * @param index
    * @param vProperty
    * @throws java.lang.IndexOutOfBoundsException if the index
    * given is outside the bounds of the collection
    */
   public <Property> void addProperty(
           final int index,
           final PropertyInterface<Property> vProperty);

   /**
    * Method enumerateProperty.
    * 
    * @return an Enumeration over all possible elements of this
    * collection
    */
   public <Property> java.util.Enumeration<PropertyInterface<Property>> enumerateProperty();

   /**
    * Method getProperty.
    * 
    * @param index
    * @throws java.lang.IndexOutOfBoundsException if the index
    * given is outside the bounds of the collection
    * @return the value of the
    * org.qcg.broker.schemas.schedulingplan.Property at the given index
    */
   public <Property> PropertyInterface<Property> getProperty(final int index)
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
   public <Property> PropertyInterface<Property>[] getProperty();

   /**
    * Method getPropertyCount.
    * 
    * @return the size of this collection
    */
   public int getPropertyCount();

   /**
    * Method iterateProperty.
    * 
    * @return an Iterator over all possible elements in this
    * collection
    */
   public <Property> java.util.Iterator<PropertyInterface<Property>> iterateProperty();

   /**
    */
   public void removeAllProperty();
   
   /**
    * Method removeProperty.
    * 
    * @param vProperty
    * @return true if the object was removed from the collection.
    */
   public <Property> boolean removeProperty(
           final PropertyInterface<Property> vProperty);

   /**
    * Method removePropertyAt.
    * 
    * @param index
    * @return the element removed from the collection
    */
   public <Property> PropertyInterface<Property> removePropertyAt(
           final int index);

   /**
    * 
    * 
    * @param index
    * @param vProperty
    * @throws java.lang.IndexOutOfBoundsException if the index
    * given is outside the bounds of the collection
    */
   public <Property> void setProperty(
           final int index,
           final PropertyInterface<Property> vProperty)
   throws java.lang.IndexOutOfBoundsException;
   
   /**
    * 
    * 
    * @param vPropertyArray
    */
   public <Property> void setProperty(
           final PropertyInterface<Property>[] vPropertyArray);
   


}
