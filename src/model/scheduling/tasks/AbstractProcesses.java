package model.scheduling.tasks;

import model.scheduling.tasks.requirements.ResourceParameterName;

public abstract class AbstractProcesses implements WorkloadUnit{
	
	
	public abstract String getId();
	
	public abstract int getProcessesCount();
	
	public abstract int[] getProcessesMap();
	
	public abstract int getSlotsPerNode();
	
	public abstract boolean belongsTo(AbstractProcessesGroup group);
	
	public abstract double getParameterDoubleValue(ResourceParameterName parameterName)
		throws NoSuchFieldException, IllegalArgumentException;

	public abstract String getParameterStringValue(ResourceParameterName parameterName)
		throws NoSuchFieldException, IllegalArgumentException;
	
	public abstract boolean isFinished();
	
	public abstract void setStatus(int status);
	
	public abstract boolean isDivisible();
}
