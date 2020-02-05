package model.scheduling.tasks.requirements;

import model.DescriptionContainer;

public abstract class AbstractResourceRequirements<T> implements DescriptionContainer<T> {

	protected T resourceRequirements;
	protected boolean isBestEffort;
	
	
	protected AbstractResourceRequirements(){
		this.isBestEffort = true;
	}
	public abstract double getParameterDoubleValue(ResourceParameterName parameterName)
		throws NoSuchFieldException, IllegalArgumentException;
	
	public abstract String getParameterStringValue(ResourceParameterName parameterName)
		throws NoSuchFieldException, IllegalArgumentException;
	
	
	public double getCpuCntRequest() throws NoSuchFieldException{
		return getParameterDoubleValue(ResourceParameterName.CPUCOUNT);
	}
	
	public double getMemoryRequest() throws NoSuchFieldException{
		return getParameterDoubleValue(ResourceParameterName.MEMORY);
	}
	
	public int getProcessesCount(){
		return 1;
	}
	
	public boolean isBestEffort(){
		return this.isBestEffort;
	}
	
	public void setBestEffort(boolean value){
		this.isBestEffort = value;
	}
	
}
