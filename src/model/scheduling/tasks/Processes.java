package model.scheduling.tasks;

import org.qcg.broker.schemas.resreqs.ComputingResourceBaseTypeItem;
import org.qcg.broker.schemas.resreqs.ComputingResourceExtType;
import org.qcg.broker.schemas.resreqs.ComputingResourceParameterType;
import org.qcg.broker.schemas.resreqs.CountType;
import org.qcg.broker.schemas.resreqs.ProcessesChoice;
import org.qcg.broker.schemas.resreqs.ProcessesMapType;
import org.qcg.broker.schemas.resreqs.ProcessesResourceRequirements;
import org.qcg.broker.schemas.resreqs.types.ComputingResourceParameterTypeNameType;

import model.scheduling.WorkloadUnitHandler;
import model.scheduling.manager.tasks.JobRegistryImpl;
import model.scheduling.tasks.requirements.ResourceParameterName;

public class Processes extends AbstractProcesses{

	protected org.qcg.broker.schemas.resreqs.Processes pr;
	protected int status;
	
	protected Processes(){
		this.pr = null;
	}
	
	public Processes(org.qcg.broker.schemas.resreqs.Processes qcgProcesses){
		this.pr = qcgProcesses;
	}

	public String getId() {
		return this.pr.getProcessesId();
	}

	public int getProcessesCount() {
		ProcessesChoice choice = this.pr.getProcessesChoice();
		
		CountType countType = choice.getProcessesCount();
		if(countType != null) {
			return countType.getValue().getContent().intValue();
		} 
		
		ProcessesMapType mapType = choice.getProcessesMap();
		if(mapType != null) {
			int slots = mapType.getSlotsPerNode();
			int processesPerNode = mapType.getProcessesPerNodeCount();
			return slots * processesPerNode;
		}
		
		return 0;
	}
	
	public int[] getProcessesMap(){
		if(this.pr.getProcessesChoice().getProcessesMap() == null)
			return null;
		
		return this.pr.getProcessesChoice().getProcessesMap().getProcessesPerNode();
	}
	
	public int getSlotsPerNode(){
		if(this.pr.getProcessesChoice().getProcessesMap() == null)
			return 0;
		
		return this.pr.getProcessesChoice().getProcessesMap().getSlotsPerNode();
		
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
			throw new NoSuchFieldException(parameterName + " for processes " + getId() 
					+ " is not defined.");
		
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
			throw new NoSuchFieldException(parameterName + " for processes " + getId() + " is not defined.");
		
		
		return returnValue;
	}
	
	public boolean belongsTo(AbstractProcessesGroup group) {
		for(int i = 0; i < this.pr.getGroupIdReferenceCount(); i++){
			String idRef = this.pr.getGroupIdReference(i);
			if(idRef.equals(group.getId())){
				return true;
			}
		}
		
		return false;
	}

	protected ComputingResourceBaseTypeItem[] getComputingResourceRequirements() throws NoSuchFieldException{
		
		ProcessesResourceRequirements req = this.pr.getProcessesResourceRequirements();
		if(req == null)
			throw new NoSuchFieldException("Requierements section for processes " + getId() 
										+ " is not defined.");
		
		ComputingResourceExtType computingResource = req.getComputingResource(0);
		if(computingResource == null)
			throw new NoSuchFieldException("Computing resource requirement for processes " + getId() 
										+ " is not defined.");
		
		ComputingResourceBaseTypeItem item[] = computingResource.getComputingResourceBaseTypeItem();
		if(item == null || item.length == 0)
			throw new NoSuchFieldException("Computing resource requirement is empty for processes " + getId());
	
		return item;
	}
	
	public void setStatus(int status){
		this.status = status;
	}
	
	public boolean isFinished(){
		return (this.status > 3 && this.status <= 6);
	}
	
	public boolean isDivisible(){
		return this.pr.getDivisible();
	}
	
	public void setDivisible(boolean value){
		this.pr.setDivisible(value);
	}

	public int getStatus() {
		return this.status;
	}

	@Override
	public int getUserId() {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public boolean isRegistered() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void register(JobRegistryImpl jobRegistry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void accept(WorkloadUnitHandler wuh) {
		// TODO Auto-generated method stub
		
	}
}
