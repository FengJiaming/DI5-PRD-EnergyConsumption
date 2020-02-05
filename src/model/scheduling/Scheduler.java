package model.scheduling;

import eduni.simjava.Sim_event;
import eduni.simjava.Sim_port;
import eduni.simjava.Sim_system;
import gridsim.GridSim;
import gridsim.GridSimCore;
import gridsim.GridSimTags;
import gridsim.IO_data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import model.Initializable;
import model.PluginConfiguration;
import model.events.scheduling.SchedulingEventType;
import model.resources.Resource;
import model.resources.ResourceStatus;
import model.resources.ResourceType;
import model.resources.computing.ComputingResource;
import model.resources.providers.LocalSystem;
import model.resources.units.ResourceUnit;
import model.resources.units.ResourceUnitName;
import model.scheduling.manager.resources.ManagedComputingResources;
import model.scheduling.manager.resources.ManagedResources;
import model.scheduling.policy.AbstractManagementSystem;
import model.scheduling.queue.QueueDescription;
import model.scheduling.queue.TaskQueue;
import model.scheduling.tasks.WorkloadUnit;

public class Scheduler extends GridSimCore implements Resource, Initializable{
	
	protected ResourceStatus status;
	protected ResourceType type;
	
	protected Scheduler parent;
	protected List<Scheduler> children;
	
	protected AbstractManagementSystem managementSystem;
	
	protected ManagedComputingResources compResources;	

	public Scheduler(AbstractManagementSystem manSys, ResourceType schedType, ManagedResources managedResources) throws Exception {
		super(manSys.getName(), 1);
		this.managementSystem = manSys;
		this.compResources = new ManagedComputingResources();
		this.status = ResourceStatus.AVAILABLE;
		this.children = new ArrayList<Scheduler>(1);
		this.type = schedType;
		init(managedResources);
	}

	public void init(ManagedResources managedResources){
		if(managedResources != null)
			addResources(managedResources.getComputingResources());
		managementSystem.init(this, managedResources);
		
		//required to terminate all scheduling entities after the end of simulation
		Integer schedulerIdObj = new Integer(get_id());
		List<Integer> schedRes = GridSim.getGridResourceList();
		schedRes.add(schedulerIdObj);

		/*if (supportsAR) {
			res = GridSim.getAdvancedReservationList();
			res.add(resIdObj);
		} */

	}
	
	public void addChild(Scheduler child) {
		child.setParent(this);
		if (children == null) {
			children = new ArrayList<Scheduler>(1);
		}
		children.add(child);
	}

	public List<Scheduler> getChildren() {
		return children;
	}

	public void setParent(Scheduler newParent) {
		this.parent = newParent;
	}

	public Scheduler getParent() {
		return parent;
	}

	/*public void addResource(ComputingResource resource) {
		compResources.add(resource);
	}*/

	public void addResources(List<ComputingResource> res) {
		for(ComputingResource resource:res){
			compResources.add(resource);
			resource.setScheduler(this);
			for(ComputingResource child: resource.filterDescendants(new Properties())){
				child.setScheduler(this);
			}
		}
	}

	public ManagedComputingResources getCompResources() {
		return compResources;
	}

	public void body() {

		PluginConfiguration pluginConfig = managementSystem.getSchedulingPluginConfiguration();
		if (pluginConfig != null) {
			Map<SchedulingEventType, Object> events = pluginConfig.getServedEvents();
			if (events != null) {
				Object obj = events.get(SchedulingEventType.TIMER);
				if (obj != null) {
					int delay = (Integer) obj;
					send(this.get_id(), delay, DCWormsTags.TIMER);
				}
			}
		}

		// Process events until END_OF_SIMULATION is received from the
		// GridSimShutdown Entity
		Sim_event ev = new Sim_event();
		sim_get_next(ev);
		boolean run = true;
		while (Sim_system.running()  && run) {
			// sim_get_next(ev);
			// if the simulation finishes then exit the loop
			if (ev.get_tag() == GridSimTags.END_OF_SIMULATION) {
				// managemetnSystem_.setEndSimulation();
				run = false;
				break;
			}
			
			// process the received event
			processRequest(ev);
			sim_get_next(ev);
		}
	}
	
	protected void processRequest(Sim_event ev) {
		switch (ev.get_tag()) {

		case GridSimTags.GRIDLET_SUBMIT:
			processWorkloadUnitSubmit(ev, false);
			break;

		case GridSimTags.GRIDLET_SUBMIT_ACK:
			processWorkloadUnitSubmit(ev, true);
			break;

		case GridSimTags.GRIDLET_RETURN:
			processWorkloadUnitReturn(ev);
			break;

		default:
			processOtherRequest(ev);
			break;
		}
	}

	protected void processOtherRequest(Sim_event ev) {
		switch (ev.get_tag()) {
		case DCWormsTags.QUERY_RESOURCE_DESC:
			SchedulerDescription desc = new SchedulerDescription(new LocalSystem(get_name(), null, null));
			Map<ResourceUnitName, List<ResourceUnit>> units = managementSystem.getResourceManager().getSharedResourceUnits();
			desc.addResourceUnitList(units);
			desc.addQueuesDescription(getQueuesDescription());

			IO_data data = new IO_data(desc, 0, ev.get_src());
			send(ev.get_src(), GridSimTags.SCHEDULE_NOW, DCWormsTags.QUERY_RESOURCE_DESC_RESULT, data);
			break;

		default:
			managementSystem.processEvent(ev);
			break;
		}
	}

	protected void processWorkloadUnitReturn(Sim_event ev) {
		WorkloadUnit job = (WorkloadUnit) ev.get_data();
		managementSystem.notifyReturnedWorkloadUnit(job);
	}

	protected void processWorkloadUnitSubmit(Sim_event ev, boolean ack) {
		WorkloadUnit job = (WorkloadUnit) ev.get_data();
		managementSystem.notifySubmittedWorkloadUnit(job, ack);
	}
	
	public void send(String entityName, double delay, int tag, Object data){
		super.send(entityName, delay, tag, data);
	}
	
	public void send(int destId, double delay, int tag){
		super.send(destId, delay, tag);
	}
	
	public void send(int destId, double delay, int tag, Object data){
		super.send(destId, delay, tag, data);
	}
	
	public void send(Sim_port destPort, double delay, int tag, Object data) {
		super.send(destPort, delay, tag, data);
	}

	public void sendInternal(double delay, int tag, Object data) {
		this.send(this.get_id(), delay, tag, data);
	}
	
	public Sim_port getOutputPort() {
		return output;
	}

	/*public Scheduler getScheduler(String resourceName){
		ComputingResource resourceWithName = null;
		for(int i = 0 ; i < compResources.size() && resourceWithName == null; i++){
			ComputingResource resource = compResources.get(i);
			if(resource.getName().equals(resourceName))
				resourceWithName = resource;
			else
				try {
					resourceWithName = resource.getDescendantByName(resourceName);
				} catch (ResourceException e) {
					return null;
				}
		}
		if(resourceWithName == null)
			return null;
		List<ComputingResource> children = resourceWithName.getChildren();
		Set<Scheduler> childrenSchedulers = new HashSet<Scheduler>();
		for(ComputingResource child:children) {
			childrenSchedulers.add(child.getScheduler());
		}
		Set<Scheduler> tempChildrenSchedulers = new HashSet<Scheduler>(childrenSchedulers);
		while(childrenSchedulers.size() != 1){
			childrenSchedulers = new HashSet<Scheduler>();
			for(Scheduler s: tempChildrenSchedulers){
				childrenSchedulers.add(s.getParent());
			}
			tempChildrenSchedulers = new HashSet<Scheduler>(childrenSchedulers);
		}
		Iterator<Scheduler> it = childrenSchedulers.iterator();
		Scheduler potentialScheduler = it.next();
		if(potentialScheduler.getResources().containsAll(children))
			return potentialScheduler;
		return null;

	}*/
	
	public List<QueueDescription> getQueuesDescription(){
		List<QueueDescription> queues = new ArrayList<QueueDescription>();
		for(TaskQueue queue: managementSystem.getQueues()){
			QueueDescription qd;
			try {
				qd = new QueueDescription(queue.getName(), queue.getPriority(), queue.supportReservations(), getQueueLoad(queue.getName()));
				queues.add(qd);
			} catch (NoSuchFieldException e) {;
			}
		}
		return queues;

	}
	private long getQueueLoad(String queueName) throws NoSuchFieldException{
		Map<String, Integer> queue_size = managementSystem.getQueuesSize();
		long load = 0;
		if(queueName == null){
			for(String queue: queue_size.keySet()){
				load += queue_size.get(queue);
			}
			return load;
		}
		else if(queue_size.containsKey(queueName))
			return queue_size.get(queueName);
		else
			throw new NoSuchFieldException("Queue " + queueName + 
							" is not available in resource " + get_name());
	}

	public ResourceType getType() {
		return this.type;
	}

	public ResourceStatus getStatus() {
		return status;
	}
	
	public void setStatus(ResourceStatus newStatus) {
		status = newStatus;
	}
	
	public void getResourceDescription(){
		
	}

	public void initiate() {

	}
}
