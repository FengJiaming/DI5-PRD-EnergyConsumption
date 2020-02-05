package gridsim.schedframe;

import gridsim.DCWormsTags;
import gridsim.GridSim;
import model.scheduling.ResourceHistoryItem;
import model.scheduling.UsedResourcesList;
import model.scheduling.WorkloadUnitHandler;
import model.scheduling.manager.tasks.JobRegistryImpl;
import model.scheduling.tasks.AbstractProcesses;
import model.scheduling.tasks.AbstractProcessesGroup;
import model.scheduling.tasks.Task;
import model.scheduling.tasks.requirements.ResourceParameterName;
import model.resources.computing.ComputingResource;
import model.resources.units.ProcessingElements;
import model.resources.units.StandardResourceUnitName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtilsExt;
import org.joda.time.ReadableDuration;


public class Executable implements ExecTask{

	protected Task task;
	protected String processesSetId;
	
	protected int status; 
	protected double length;
	protected Map<ResourceParameterName, Object> specificResources;
	
	protected String reservationId;
	protected double completionPercentage;
	
	protected int estimatedDuration;
	//TO DO remove and benefit from visitedResources
	protected String schedName;
	protected UsedResourcesList usedResources;
	//TO DO consider removing
	protected List<String> visitedResources;

	protected double submissionTime;
    protected double arrivalTime;
	protected double execStartTime ;
    protected double totalCompletionTime; 
	protected double finishTime; 
	
	public Executable(Task t){
		this.task = t;
		this.status = DCWormsTags.CREATED;
		
		this.usedResources = new UsedResourcesList();
		this.visitedResources = new ArrayList<String>();
		init();
	}
	
	public Executable(Task t, AbstractProcesses procesesSet){
		this.task = t;
		this.status = DCWormsTags.CREATED;
		this.processesSetId = procesesSet.getId(); 
		
		this.usedResources = new UsedResourcesList();
		this.visitedResources = new ArrayList<String>();
		init();
	}
	
	protected void init() {
		double currentTime = DateTimeUtilsExt.currentTimeMillis() / 1000;
		this.submissionTime =  currentTime;
        this.totalCompletionTime = 0.0;
	 }
	
	public int getUserId() {
		return task.getSenderId();
	}
	public String getUserDN() {
		return task.getUserDN();
	}

	public String getJobId() {
		return task.getJobId();
	}
	
	public String getTaskId(){
		return this.task.getId();
	}
	
	public String getId() {
		if(processesSetId == null)
			return task.getId();
		else 
			return task.getId() + "_" + processesSetId;
	}
	
	public String getProcessesId(){
		return this.processesSetId;
	}
	
    public int getUniqueId(){
    	if(processesSetId == null){
    		return (task.getJobId() + "_" + task.getId()).hashCode();
    	} else {
    		return (task.getJobId() + "_" + task.getId() + "_" + processesSetId).hashCode();
    	}
    }
    
	public List<AbstractProcesses> getProcesses() {
		return task.getProcesses();
	}

	public List<AbstractProcesses> getProcesses(
			AbstractProcessesGroup processGroup) {
		return task.getProcesses(processGroup);
	}

	public List<AbstractProcessesGroup> getProcessesGroups() {
		return task.getProcessesGroups();
	}

	public org.qcg.broker.schemas.resreqs.Task getDescription() {
		return task.getDescription();
	}

	public String getDocument() throws Exception {
		return task.getDocument();
	}
	
	public long getLength() {
		return task.getLength();
	}

	public int getStatus() {
		return status;
	}
	
	public boolean isFinished()
    {
		return task.isFinished();
    }
	
	public void setStatus(int newStatus) throws Exception {
		int prevStatus = status;
	    
        if (status == newStatus) {
            return;
        }

        if (newStatus < DCWormsTags.CREATED || newStatus > DCWormsTags.FAILED_RESOURCE_UNAVAILABLE) {
            throw new Exception("Executable.setStatuts() : Error - " +
                    "Invalid integer range for Execiutable status.");
        }

        status = newStatus;
        double currentTime = DateTimeUtilsExt.currentTimeMillis() / 1000; // time in seconds 
    
		
		if (newStatus == DCWormsTags.SUCCESS || newStatus == DCWormsTags.CANCELED) {
            finishTime = DateTimeUtilsExt.currentTimeMillis() / 1000;
        }
		
		if(newStatus == DCWormsTags.SUBMITTED){
			 arrivalTime = GridSim.clock();
		}

        if (prevStatus == DCWormsTags.INEXEC) {
            if (status == DCWormsTags.CANCELED || status == DCWormsTags.PAUSED ||
                status == DCWormsTags.SUCCESS) {
                totalCompletionTime += (currentTime -  execStartTime);
            }
        }

        if (prevStatus == DCWormsTags.RESUMED && status == DCWormsTags.SUCCESS) {
            totalCompletionTime += (currentTime -  execStartTime);
        }

        if (status == DCWormsTags.INEXEC ||
            (prevStatus == DCWormsTags.PAUSED && status == DCWormsTags.RESUMED) ) {
        	execStartTime = currentTime;
        	
    		ProcessingElements pes = (ProcessingElements) getUsedResources().getLast().getResourceUnits().get(StandardResourceUnitName.PE);
    		for (ComputingResource resource : pes) {

    			trackResource(resource.getName());
    			
    			ComputingResource parent = resource.getParent();
				List<String> visitedResource = getVisitedResources();
				String [] visitedResourcesArray = visitedResource.toArray(new String[visitedResource.size()]);
    			while (parent != null && !ArrayUtils.contains(visitedResourcesArray, parent.getName())) {
    				trackResource(parent.getName());
    				parent = parent.getParent();
    			}
    		}
        }
	}
	
	public void addSpecificResource(ResourceParameterName resourceName, Object value){
		if(this.specificResources == null)
			this.specificResources = new HashMap<ResourceParameterName, Object>();
		
		this.specificResources.put(resourceName, value);
	}

	public boolean expectSpecificResource(ResourceParameterName resourceName){
		if(this.specificResources == null)
			return false;

		return this.specificResources.containsKey(resourceName);
	}
	
	public Object getExpectedSpecificResource(ResourceParameterName resourceName){
		if(this.specificResources == null)
			return null;
		
		return this.specificResources.get(resourceName);
	}
	
	public void setReservationId(String reservationId){
		this.reservationId = reservationId;
	}
	
	public boolean requireReservation(){
		return (reservationId != null);
	}
	
	public String getReservationId(){
		return this.reservationId;
	}
	
	public double getCompletionPercentage() {
		return completionPercentage;
	}

	public void setCompletionPercentage(double completionPercentage) {
		this.completionPercentage = completionPercentage;
	}

	public void addUsedResources(ResourceHistoryItem usedResources){
		this.usedResources.add(usedResources);
	}
	
	public UsedResourcesList getUsedResources(){
		return this.usedResources;
	}
	
    public void setSchedulerName(int resourceId)
    {
        this.schedName = GridSim.getEntityName(resourceId);
    }

    public String getSchedulerName()
    {
        return schedName;
    }
	
	public int getEstimatedDuration(){
		return this.estimatedDuration;
	}
	
	public void setEstimatedDuration(int value){
		this.estimatedDuration = value;
	}

	public boolean isRegistered() {
		return task.isRegistered();
	}

	public void register(JobRegistryImpl jobRegistry) {
		task.register(jobRegistry);
	}
	
	public void trackResource(String resName){
		visitedResources.add(resName);
	}
	
	public List<String> getVisitedResources(){
		return visitedResources;
	}
	
	public ReadableDuration getExpectedDuration() throws NoSuchFieldException {
		return task.getExpectedDuration();
	}

	public double getParameterDoubleValue(ResourceParameterName parameterName)
			throws NoSuchFieldException, IllegalArgumentException {
		return task.getParameterDoubleValue(parameterName);
	}

	public String getParameterStringValue(ResourceParameterName parameterName)
			throws NoSuchFieldException, IllegalArgumentException {
		return task.getParameterStringValue(parameterName);
	}
	
	public double getCpuCntRequest() throws NoSuchFieldException{
		return getParameterDoubleValue(ResourceParameterName.CPUCOUNT);
	}
	
	public double getMemoryRequest() throws NoSuchFieldException{
		return getParameterDoubleValue(ResourceParameterName.MEMORY);
	}
	
	public DateTime getExecutionEndTime() throws NoSuchFieldException {
		return task.getExecutionEndTime();
	}

	public DateTime getExecutionStartTime() throws NoSuchFieldException {
		return task.getExecutionStartTime();
	}

	public DateTime getSubmissionTimeToBroker() {
		return task.getSubmissionTimeToBroker();
	}

	public long getWorkloadLogWaitTime() {
		return task.getWorkloadLogWaitTime();
	}

    public double getExecStartTime() {
        return execStartTime;
    }

    public double getFinishTime() {
        return finishTime;
    }

    public double getSubmissionTime() {
        return submissionTime;
    }
    
    public double getWaitingTime() {
        return execStartTime - submissionTime;
    }
    
    public void finalizeExecutable(){
		try {
			setStatus(DCWormsTags.SUCCESS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	public void accept(WorkloadUnitHandler wuh) {
		wuh.handleExecutable(this);
	}
    

}
