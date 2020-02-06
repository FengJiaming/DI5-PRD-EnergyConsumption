package model.scheduling.plan;


import model.DescriptionContainer;

public interface ProcessesMapInterface<T> extends DescriptionContainer<T> {

	public void setSlotsPerNode(int arg);
	
	public int getSlotsPerNode();
	
	public void setProcessesPerNode(int[] arg);
	
	public int[] getProcessesPerNode();
}
