package model.scheduling.tasks;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.ReadableDuration;

import model.DescriptionContainer;
import model.scheduling.tasks.requirements.ResourceParameterName;


public interface TaskInterface<T> extends WorkloadUnit, DescriptionContainer<T> {
	
	public abstract String getJobId();
	
	public abstract String getUserDN();
	
	public abstract DateTime getSubmissionTimeToBroker();
	
	public abstract DateTime getExecutionStartTime() throws NoSuchFieldException;
	
	public abstract DateTime getExecutionEndTime() throws NoSuchFieldException;
	
	public abstract ReadableDuration getExpectedDuration() throws NoSuchFieldException;
	
	public abstract double getParameterDoubleValue(ResourceParameterName parameterName)
		throws NoSuchFieldException, IllegalArgumentException;
	
	public abstract String getParameterStringValue(ResourceParameterName parameterName)
		throws NoSuchFieldException, IllegalArgumentException;
	
	public long getLength();

	
	public List<AbstractProcessesGroup> getProcessesGroups();
	
	public List<AbstractProcesses> getProcesses();
	
	public List<AbstractProcesses> getProcesses(AbstractProcessesGroup processGroup);
	
	public double getCpuCntRequest() throws NoSuchFieldException;
	
	public double getMemoryRequest() throws NoSuchFieldException;
	
	public long getWorkloadLogWaitTime();

		
}
