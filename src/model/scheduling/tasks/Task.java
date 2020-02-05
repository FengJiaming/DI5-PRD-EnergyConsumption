package model.scheduling.tasks;

import org.qcg.broker.schemas.resreqs.ComputingResource;
import org.qcg.broker.schemas.resreqs.ComputingResourceBaseTypeItem;
import org.qcg.broker.schemas.resreqs.ComputingResourceExtType;
import org.qcg.broker.schemas.resreqs.ComputingResourceParameterType;
import org.qcg.broker.schemas.resreqs.ExecutionTimeType;
import org.qcg.broker.schemas.resreqs.ProcessesResourceRequirements;
import org.qcg.broker.schemas.resreqs.Requirements;
import org.qcg.broker.schemas.resreqs.TaskResourceRequirements;
import org.qcg.broker.schemas.resreqs.TimePeriod;
import org.qcg.broker.schemas.resreqs.TimePeriodChoice;
import org.qcg.broker.schemas.resreqs.Topology;
import org.qcg.broker.schemas.resreqs.types.ComputingResourceParameterTypeNameType;

import model.scheduling.WorkloadUnitHandler;
import model.scheduling.manager.tasks.JobRegistryImpl;
import model.scheduling.tasks.requirements.ResourceParameterName;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.XMLContext;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableDuration;


public class Task implements TaskInterface<org.qcg.broker.schemas.resreqs.Task> {
	
	protected static Unmarshaller unmarshaller;
	protected static Marshaller marshaller;
	protected boolean isRegistered;
	
	static {
		XMLContext context = new XMLContext();
		try {
			context.addClass(org.qcg.broker.schemas.resreqs.Task.class);
			unmarshaller = context.createUnmarshaller();
			marshaller = context.createMarshaller();
		} catch (ResolverException e) {
			e.printStackTrace();
			unmarshaller = null;
			marshaller = null;
		}
	}
	
	protected org.qcg.broker.schemas.resreqs.Task task;
	/*
	 * The values for following variables are obtained from native Task
	 * object. This should significantly speed up access to task details.
	 */
	private DateTime startTime;
	private DateTime endTime;
	private DateTime brokerSubmitTime;
	private ReadableDuration duration;
	private List<AbstractProcessesGroup> groups;
	private List<AbstractProcesses> processes;
	private long length;
	private int status;
	private int senderId;
	private long workloadLogWaitTime;

	public Task(org.qcg.broker.schemas.resreqs.Task task){
		this.task = task;
		this.startTime = null;
		this.endTime = null;
		this.brokerSubmitTime = null;
		this.duration = null;
		prepareTopology();
	}
	
	public Task(String task) throws Exception{
		StringReader reader = new StringReader(task);
		this.task = (org.qcg.broker.schemas.resreqs.Task) unmarshaller.unmarshal(reader);
		this.startTime = null;
		this.endTime = null;
		this.brokerSubmitTime = null;
		this.duration = null;
		prepareTopology();
	}

	public DateTime getExecutionStartTime() throws NoSuchFieldException {
		if(this.startTime != null)
			return this.startTime;
		
		ExecutionTimeType execTime = this.task.getExecutionTime();
		if(execTime == null)
			throw new NoSuchFieldException("Execution Time for job " + getJobId() 
										+ " task "+ getId() + " is not defined.");
		
		TimePeriod timePeriod = execTime.getTimePeriod();
		if(timePeriod == null)
			throw new NoSuchFieldException("Time Period for job " + getJobId() 
					+ " task "+ getId() + " is not defined.");
		
		this.startTime = new DateTime(timePeriod.getPeriodStart());
		return this.startTime;
	}

	public DateTime getExecutionEndTime() throws NoSuchFieldException {
		if(this.endTime != null)
			return this.endTime;
		
		ExecutionTimeType execTime = this.task.getExecutionTime();
		if(execTime == null)
			throw new NoSuchFieldException("Execution Time for job " + getJobId() 
										+ " task "+ getId() + " is not defined.");
		
		TimePeriod timePeriod = execTime.getTimePeriod();
		if(timePeriod == null)
			throw new NoSuchFieldException("Time Period for job " + getJobId() 
					+ " task "+ getId() + " is not defined.");
		
		TimePeriodChoice periodChoice = timePeriod.getTimePeriodChoice();
		if(periodChoice == null)
			throw new NoSuchFieldException("Period End and Period Duration for job " + getJobId() 
					+ " task "+ getId() + " are not defined.");
		
		java.util.Date periodEnd = periodChoice.getPeriodEnd();
		if(periodEnd != null) {
			this.endTime = new DateTime(periodEnd);
			return this.endTime;
		}
		
		org.exolab.castor.types.Duration duration = periodChoice.getPeriodDuration();
		if(duration == null)
			throw new NoSuchFieldException("Period Duration for job " + getJobId() 
					+ " task "+ getId() + " is not defined.");
		
		DateTime periodStart = getExecutionStartTime();
		MutableDateTime m_periodEnd = periodStart.toMutableDateTime();
		m_periodEnd.add(duration.toLong());

		this.endTime = m_periodEnd.toDateTime();
		periodChoice.setPeriodDuration(null);
		periodChoice.setPeriodEnd(this.endTime.toDate());
		
		return this.endTime;
	}

	public ReadableDuration getExpectedDuration() throws NoSuchFieldException {
		if(this.duration != null)
			return this.duration;
		
		ExecutionTimeType execTime = this.task.getExecutionTime();
		if(execTime == null)
			throw new NoSuchFieldException("Execution Time for job " + getJobId() 
										+ " task "+ getId() + " is not defined.");
		
		org.exolab.castor.types.Duration d = execTime.getExecutionDuration();
		if(d == null)
			throw new NoSuchFieldException("Execution Duration for job " + getJobId() 
					+ " task "+ getId() + " is not defined.");

		this.duration = new Duration(d.toLong());
		return this.duration;
	}

	public String getJobId() {
		return this.task.getJobId();
	}

	public double getParameterDoubleValue(ResourceParameterName parameterName)
			throws NoSuchFieldException, IllegalArgumentException {
		
		ComputingResourceParameterTypeNameType name = ComputingResourceParameterTypeNameType.valueOf(parameterName.value().toUpperCase());
		
		switch (name) {
			case APPLICATION:
			case CPUARCH:
			case HOSTNAME:
			case LOCALRESOURCEMANAGER:
			case OSNAME:
			case OSRELEASE:
			case OSTYPE:
			case OSVERSION:
			case REMOTESUBMISSIONINTERFACE:
				throw new IllegalArgumentException("For " + parameterName + " use getParameterStringValue() method.");
		}

		ComputingResourceBaseTypeItem item[] = getComputingResourceRequirements();
		
		double returnValue = 0;
		boolean notFound = true;
		
		for(int i = 0; i < item.length && notFound; i++){
			ComputingResourceParameterType hostParameter = item[i].getHostParameter();
			if(hostParameter == null)
				continue;
			
			if(name == hostParameter.getName()) {
				returnValue = hostParameter.getParameterTypeChoice().getParameterTypeChoiceItem(0).getParameterValue().getContent();
				notFound = false;
			}
		}
		
		if(notFound)
			throw new NoSuchFieldException(parameterName + " for job " + getJobId() 
					+ " task "+ getId() + " is not defined.");
		
		return returnValue;
	}

	public String getParameterStringValue(ResourceParameterName parameterName)
			throws NoSuchFieldException, IllegalArgumentException {
		ComputingResourceParameterTypeNameType name = ComputingResourceParameterTypeNameType.valueOf(parameterName.value().toUpperCase());
		
		switch (name) {
			case CPUCOUNT:
			case GPUCOUNT:
			case CPUSPEED:
			case DISKSPACE:
			case FREECPUS:
			case FREEDISKSPACE:
			case FREEMEMORY:
			case MEMORY:
				throw new IllegalArgumentException("For " + parameterName + " use getParameterDoubleValue() method.");
		}
		
		ComputingResourceBaseTypeItem item[] = getComputingResourceRequirements();
		
		String returnValue = null;
		boolean notFound = true;
		
		for(int i = 0; i < item.length && notFound; i++){
			ComputingResourceParameterType hostParameter = item[i].getHostParameter();
			if(hostParameter == null)
				continue;
			
			if(name == hostParameter.getName()) {
				returnValue = hostParameter.getStringValue(0).getValue();
				notFound = false;
			}
		}
		
		if(notFound)
			throw new NoSuchFieldException(parameterName + " for job " + getJobId() 
					+ " task "+ getId() + " is not defined.");
		
		return returnValue;
	}

	public DateTime getSubmissionTimeToBroker() {
		if(this.brokerSubmitTime != null)
			return this.brokerSubmitTime;
		
		this.brokerSubmitTime = new DateTime(this.task.getSubmissionTime());
		
		return this.brokerSubmitTime;
	}

	public String getId() {
		return this.task.getTaskId();
	}

	public String getUserDN() {
		return this.task.getUserDN();
	}

	public org.qcg.broker.schemas.resreqs.Task getDescription() {
		return this.task;
	}

	public String getDocument() throws Exception {
		StringWriter writer = new StringWriter();
		
		marshaller.marshal(this.task, writer);
		
		return writer.toString();
	}
	
	protected ComputingResourceBaseTypeItem[] getComputingResourceRequirements() throws NoSuchFieldException{
		
		Requirements req = this.task.getRequirements();
		if(req == null)
			throw new NoSuchFieldException("Requierements section for job " + getJobId() 
										+ " task "+ getId() + " is not defined.");
		
		TaskResourceRequirements taskReq = req.getTaskResourceRequirements();
		if(taskReq == null)
			throw new NoSuchFieldException("Task resource requirements section for job " + getJobId() 
					+ " task "+ getId() + " is not defined.");
		
		ComputingResource computingResource = taskReq.getComputingResource(0);
		if(computingResource == null)
			throw new NoSuchFieldException("Computing resource requirement for job " + getJobId() 
					+ " task "+ getId() + " is not defined.");
		
		ComputingResourceBaseTypeItem item[] = computingResource.getComputingResourceBaseTypeItem();
		if(item == null || item.length == 0)
			throw new NoSuchFieldException("Computing resource requirement is empty for job " + getJobId() 
					+ " task "+ getId());
	
		return item;
	}
	
	public void setValue(ResourceParameterName parameterName, Object newValue) throws NoSuchFieldException{
		boolean notFound = true;
		
		ComputingResourceParameterTypeNameType name = ComputingResourceParameterTypeNameType.valueOf(parameterName.value().toUpperCase());
		
		ComputingResourceBaseTypeItem item[] = getComputingResourceRequirements();
		
		for(int i = 0; i < item.length && notFound; i++){
			ComputingResourceParameterType hostParameter = item[i].getHostParameter();
			if(hostParameter == null)
				continue;
			
			if(name == hostParameter.getName()) {
				hostParameter.
						getParameterTypeChoice().
						getParameterTypeChoiceItem(0).
						getParameterValue().
						setContent(((Integer)newValue).doubleValue());
				notFound = false;
			}
		}
		
		if(notFound)
			throw new NoSuchFieldException(parameterName + " for job " + getJobId() 
					+ " task "+ getId() + " is not defined.");
	}

	public List<AbstractProcessesGroup> getProcessesGroups() {
		return this.groups;
	}
	
	public List<AbstractProcesses> getProcesses(){
		return this.processes;
	}
	
	public List<AbstractProcesses> getProcesses(AbstractProcessesGroup processGroup){
		if(this.processes == null)
			return null;
		
		List<AbstractProcesses> ret = new ArrayList<AbstractProcesses>();
		
		for(int i = 0; i < processes.size(); i++){
			AbstractProcesses p = processes.get(i);
			if(p.belongsTo(processGroup))
				ret.add(p);
		}
		
		return ret;
	}
	
	protected void prepareTopology(){
		if(this.task.getRequirements() == null)
			return;
		
		if(this.task.getRequirements().getTopologyCount() < 1)
			return;
		
		Topology topology = this.task.getRequirements().getTopology(0);
		
		if(topology.getGroupCount() > 0){
			this.groups = new ArrayList<AbstractProcessesGroup>(topology.getGroupCount());
		}
		
		for(int i = 0; i < topology.getGroupCount(); i++){
			this.groups.add(new ProcessesGroup(topology.getGroup(i)));
		}

		if(topology.getProcessesCount() > 0){
			this.processes = new ArrayList<AbstractProcesses>(topology.getProcessesCount());
		}

		for(int i = 0; i < topology.getProcessesCount(); i++){
			org.qcg.broker.schemas.resreqs.Processes p = topology.getProcesses(i);
			if(p.getProcessesResourceRequirements() == null){
				TaskResourceRequirements trr = this.task.getRequirements().getTaskResourceRequirements();
				if(trr != null) {
					ProcessesResourceRequirements prr = new ProcessesResourceRequirements();
					
					for(int cridx = 0; cridx < trr.getComputingResourceCount(); cridx++){
						ComputingResourceExtType cre = new ComputingResourceExtType();
						ComputingResource cr = trr.getComputingResource(cridx);
						
						for(int j = 0; j < cr.getComputingResourceBaseTypeItemCount(); j++){
							cre.addComputingResourceBaseTypeItem(cr.getComputingResourceBaseTypeItem(j));
						}
						
						prr.addComputingResource(cre);
					}
					
					p.setProcessesResourceRequirements(prr);
				}
			}
			
			this.processes.add(new Processes(p));
		}
	}

	public double getCpuCntRequest() throws NoSuchFieldException {
		return getParameterDoubleValue(ResourceParameterName.CPUCOUNT);
	}

	public double getMemoryRequest() throws NoSuchFieldException {
		return getParameterDoubleValue(ResourceParameterName.MEMORY);
	}
	
	public long getLength() {
		return this.length;
	}

	public int getStatus() {
		return this.status;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public void setStatus(int status){
		this.status = status;
	}
	
	public void setSenderId(int id){
		this.senderId = id;
	}
	
	public int getSenderId(){
		return this.senderId;
	}
	
	public boolean isFinished(){
		if(processes == null)
			return (status > 3 && status <= 6);
		
		for(int i = 0; i < processes.size(); i++){
			if(!processes.get(i).isFinished())
				return false;
		}
		
		return true;
	}
	
	public long getWorkloadLogWaitTime() {
		return workloadLogWaitTime;
	}

	public void setWorkloadLogWaitTime(long waitTime) {
		this.workloadLogWaitTime = waitTime;
	}
	
	/*public void addToResPath(String resName){
		if(resPathHistory == null)
			resPathHistory = new String();
		resPathHistory = new StringBuffer(resPathHistory).append(resName).append("_").toString();

	}
	
	public String getResPath(){
		return this.resPathHistory;

	}*/


	@Override
	public int getUserId() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isRegistered() {
		return isRegistered;
	}

	public void register(JobRegistryImpl jobRegistry) {
		isRegistered = true;
	}

	public void accept(WorkloadUnitHandler wuh) {
		wuh.handleTask(this);
	}
}
