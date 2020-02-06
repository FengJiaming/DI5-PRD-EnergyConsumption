package gridsim;

import eduni.simjava.Sim_event;
import eduni.simjava.Sim_system;
import gridsim.GridSimCore;
import gridsim.GridSimTags;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import controller.ResourceController;
import model.events.Event;
import model.events.ResourceEventCommand;
import model.exceptions.ModuleException;
import model.resources.computing.ComputingResource;
import model.scheduling.Scheduler;
import model.scheduling.plugin.grid.Module;
import model.scheduling.plugin.grid.ModuleType;

public class EventManager extends GridSimCore implements Module{

	protected ResourceController resourceController;
	
	public EventManager(String name, ResourceController resourceController) throws Exception {
		super(name, 1);
		this.resourceController = resourceController;
	}

	public void body() {


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

		case DCWormsTags.TO_COMP_RESOURCES:
			sendToResources(ev);
			break;

		case DCWormsTags.TO_SCHEDULERS:
			//super.send((Integer)ev.get_data(), 0, DCWormsTags.PHASE_CHANGED, null);
			break;

		default:
			break;
		}
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
	
	public void init(Properties properties) throws ModuleException {
	}

	public void sendToAllSchedulers(double delay, int tag, Object data){
		List<Scheduler> allSchedulers =  new ArrayList<Scheduler>();
		if (resourceController.getScheduler().getChildren() != null) {
			LinkedList<Scheduler> toExamine = new LinkedList<Scheduler>();
			toExamine.push(resourceController.getScheduler());
			allSchedulers.add(resourceController.getScheduler());

			while (!toExamine.isEmpty()) {
				Scheduler scheduler = toExamine.pop();
				List<Scheduler> schedulers = scheduler.getChildren();
				int numberOfSched = schedulers.size();
				for (int i = 0; i < numberOfSched; i++) {
					Scheduler schedulerChild = schedulers.get(i);
					toExamine.addLast(schedulerChild);
					allSchedulers.add(schedulerChild);
				}
			}
		}
		
		
		for(Scheduler scheduler: allSchedulers){
			//sendInternal(delay, DCWormsTags.TO_SCHEDULERS, scheduler.get_id());
			super.send(scheduler.get_id(), delay, tag, data);
		}
	}
	
	public void sendToAllResources(double delay, Event event){
		List<ComputingResource> allComputingResources = new ArrayList<ComputingResource>();

		if (resourceController.getComputingResources() != null) {
			LinkedList<ComputingResource> toExamine = new LinkedList<ComputingResource>();
			for(ComputingResource compRes: resourceController.getComputingResources()){
				toExamine.push(compRes);
				allComputingResources.add(compRes);
			}

			while (!toExamine.isEmpty()) {
				ComputingResource resource = toExamine.pop();
				List<ComputingResource> resources = resource.getChildren();
				int numberOfRes = resources.size();
				for (int i = 0; i < numberOfRes; i++) {
					ComputingResource resourceChild = resources.get(i);
					toExamine.addLast(resourceChild);
					allComputingResources.add(resourceChild);
				}
			}
		}
		sendInternal(delay, DCWormsTags.TO_COMP_RESOURCES, new ResourceBroadcastOrder(allComputingResources, event));
	}

	private void sendToResources(Sim_event ev){
		ResourceEventCommand rec;
		
		ResourceBroadcastOrder rbo = (ResourceBroadcastOrder)ev.get_data();
		List<ComputingResource> allComputingResources = rbo.getComputingResources();
		Event event = rbo.getEvent();
		for(ComputingResource compRes: allComputingResources){
			rec = new ResourceEventCommand(compRes);
			rec.execute(event);
		}
	}
	
	public void sendInternal(double delay, int tag, Object data) {
		this.send(this.get_id(), delay, tag, data);
	}
	
	public void sendTo(List<String> ids){
	}
	
	public void dispose() throws ModuleException {
	}

	public ModuleType getType() {
		return ModuleType.EVENT_MANAGER;
	}

}

class ResourceBroadcastOrder {

	private List<ComputingResource> computingResources;
	private Event event;
	
	public ResourceBroadcastOrder(List<ComputingResource> computingResources, Event event) {
		super();
		this.computingResources = computingResources;
		this.event = event;
	}

	public List<ComputingResource> getComputingResources() {
		return computingResources;
	}

	public Event getEvent() {
		return event;
	}
}


