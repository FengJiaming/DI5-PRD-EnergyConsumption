package model.scheduling.plan;


import model.DescriptionContainer;

public interface PropertyInterface<T> extends DescriptionContainer<T> {

		public java.lang.String getName();
		
		public java.lang.String getContent();

	    public void setContent(
	            final java.lang.String content);

	    public void setName(final java.lang.String name);

}
