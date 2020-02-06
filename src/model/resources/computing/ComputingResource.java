package model.resources.computing;

import gridsim.DCWormsTags;
import gridsim.GridSimTags;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import model.Initializable;
import model.events.Event;
import model.events.EventHandler;
import model.events.ResourceEventCommand;
import model.events.scheduling.EventReason;
import model.events.scheduling.SchedulingEvent;
import model.events.scheduling.SchedulingEventCommand;
import model.exceptions.ResourceException;
import model.resources.Resource;
import model.resources.ResourceStatus;
import model.resources.ResourceType;
import model.resources.computing.description.ComputingResourceDescription;
import model.resources.computing.extensions.Extension;
import model.resources.computing.extensions.ExtensionList;
import model.resources.computing.extensions.ExtensionListImpl;
import model.resources.computing.extensions.ExtensionType;
import model.resources.computing.profiles.energy.EnergyEvent;
import model.resources.computing.profiles.energy.EnergyEventType;
import model.resources.computing.profiles.energy.EnergyExtension;
import model.resources.computing.profiles.energy.power.ui.PowerInterface;
import model.resources.computing.properties.DefaultPropertiesBuilder;
import model.resources.computing.properties.PropertiesDirector;
import model.resources.computing.validator.ResourceNameValidator;
import model.resources.computing.validator.ResourcePropertiesValidator;
import model.resources.computing.validator.ResourceStatusValidator;
import model.resources.computing.validator.ResourceTypeValidator;
import model.resources.computing.validator.ResourceValidator;
import model.resources.units.PEUnit;
import model.resources.units.ResourceUnit;
import model.resources.units.StandardResourceUnitName;
import model.scheduling.Scheduler;

public class ComputingResource implements Resource, Initializable{
	
	protected String name;
	protected ResourceType type;
	protected String category;
	protected ResourceStatus status;

	protected ComputingResource parent;
	protected List<ComputingResource> children;
	
	protected Scheduler scheduler;
	protected ResourceCharacteristics resourceCharacteristic;
	protected ExtensionList extensionList;
	

	public ExtensionList getExtensionList() {
		return extensionList;
	}

	public ComputingResource(ComputingResourceDescription resDesc) {
		this.type = resDesc.getType();
		this.name = resDesc.getId();
		this.category = resDesc.getCategory();
		this.status = ResourceStatus.FREE;
		this.extensionList = new ExtensionListImpl(1);
		initCharacteristics(resDesc);
		accept(new EnergyExtension(this, resDesc.getPowerProfile()));	
		addFakeProcessors();
	}

	//TODO remove if possible (check if all scenarios can be realized - statistics issue), since it's a temporary method
	private void addFakeProcessors() {
		if(getResourceCharacteristic().getResourceUnits().get(StandardResourceUnitName.PE) != null){
			for(ResourceUnit resUnit: getResourceCharacteristic().getResourceUnits().get(StandardResourceUnitName.PE)){
				PEUnit peUnit = (PEUnit) resUnit;
				for(int i = 0; i < peUnit.getAmount(); i++){
					schemas.ComputingResource fakeCompResource = new schemas.ComputingResource();
					fakeCompResource.setClazz("Processor");
					addChild(ResourceFactory.createResource(new ComputingResourceDescription(fakeCompResource)));
				}
			}
		}
	}

	protected void initCharacteristics(ComputingResourceDescription resDesc){
		resourceCharacteristic = new ResourceCharacteristics.Builder().resourceUnits(resDesc.getResourceUnits()).location(resDesc.getLocation()).parameters(resDesc.getParameters()).build();
	}
	
	public ComputingResource getParent() {
		return parent;
	}

	public void setParent(ComputingResource newParent) {
		parent = newParent;
	}

	public List<ComputingResource> getChildren() {
		if (children == null)
			return new ArrayList<ComputingResource>(1);
		return children;
	}

	public void addChild(ComputingResource child) {
		child.setParent(this);
		if (children == null)
			children = new ArrayList<ComputingResource>();
		children.add(child);
	}

	public String getName() {
		return name;
	}

	public ResourceType getType() {
		return type;
	}

	public ResourceCharacteristics getResourceCharacteristic() {
		return resourceCharacteristic;
	}

	public ResourceStatus getStatus() {
		return status;
	}

	public void setStatus(ResourceStatus newStatus) {
		if(newStatus != status){
			status = newStatus;
			if(children != null) {
				for(ComputingResource child: children){
					child.setStatus(status);
				}
			}	
		}
	}
	
	private void triggerEventUp(Event event) {
		if(parent != null)
			parent.handleEvent(event);
	}

	public void handleEvent(Event event){
		ResourceEventCommand rec = new ResourceEventCommand(this);
		rec.execute(event);
		SchedulingEventCommand sec = new SchedulingEventCommand(this);
		sec.execute(event);

		//old, correctly working method
		/*if (extensionList != null) {
			for (Extension extension : extensionList) {
				if (extension.supportsEvent(event)) {
					extension.handleEvent(event);
				}
			}
		}*/
		//TODO - delete, check in advance
		//if(scheduler != null && (parent != null && scheduler != parent.getScheduler())/*scheduler.getResources().contains(this)*/){
		//	String src = event.getSource() != null ? event.getSource() : name;
		//	scheduler.sendInternal(GridSimTags.SCHEDULE_NOW, DCWormsTags.UPDATE, src);
		//}
		//triggerEventUp(event);
	}
	
	public List <? extends ComputingResource> getDescendantsByType(ResourceType type) {
		List<ResourceValidator> validators = new ArrayList<ResourceValidator>();
		validators.add(new ResourceTypeValidator(type));
		return searchDescendants(validators, true);
	}

	public List<? extends ComputingResource> getDescendantsByTypeAndStatus(ResourceType type, ResourceStatus status) {
		List<ResourceValidator> validators = new ArrayList<ResourceValidator>();
		validators.add(new ResourceStatusValidator(status));
		validators.add(new ResourceTypeValidator(type));
		return searchDescendants(validators, true);
	}

	public ComputingResource getDescendantByName(String resourceName) throws ResourceException {
		List<ResourceValidator> validators = new ArrayList<ResourceValidator>();
		validators.add(new ResourceNameValidator(resourceName));
		List<? extends ComputingResource> resources = searchDescendants(validators, true);
		return resources.size() == 0 ? null : resources.get(0);
	}
	
	public List<? extends ComputingResource> filterDescendants(Properties properties)  {
		List<ResourceValidator> validators = new ArrayList<ResourceValidator>();
		validators.add(new ResourcePropertiesValidator(properties));
		return searchDescendants(validators, false);
	}
	
	protected List<? extends ComputingResource> searchDescendants(List<ResourceValidator> validators, boolean cutOff) {

		List<ComputingResource> descendants = new ArrayList<ComputingResource>();
		if (children != null) {
			LinkedList<ComputingResource> toExamine = new LinkedList<ComputingResource>();
			toExamine.push(this);

			while (!toExamine.isEmpty()) {
				ComputingResource resource = toExamine.pop();
				List<ComputingResource> resources = resource.getChildren();
				int numberOfRes = resources.size();
				for (int i = 0; i < numberOfRes; i++) {
					ComputingResource resourceChild = resources.get(i);
					if (resourceChild.match(validators)) {
						descendants.add(resourceChild);
						if(cutOff == false) {
							toExamine.addLast(resourceChild);
						}
					} else
						//toExamine.insertElementAt(resourceChild, 0);
						toExamine.addLast(resourceChild);
				}
			}
		}
		return descendants;
	}
	
	protected boolean match(List<ResourceValidator> validators){
		for(ResourceValidator validator: validators){
			if(validator.validate(this) == false)
				return false;
		}
		return true;
	}
	
	public Properties getProperties(){
		PropertiesDirector propDirector = new PropertiesDirector();
		propDirector.setPropertiesBuilder(new DefaultPropertiesBuilder());
		propDirector.constructProperties(this);
		return propDirector.getProperties();
	}
	
	public String getCategory(){
		return category;
	}
	
	public PowerInterface getPowerInterface(){
		Extension extension = getExtension(ExtensionType.ENERGY_EXTENSION);
		if(extension != null){
			EnergyExtension ee = (EnergyExtension)extension;
			return ee.getPowerInterface();
		}
		return null;
	}
	
	

	private Extension getExtension(ExtensionType type){
		if (extensionList != null) {
			for (Extension extension : extensionList) {
				if (extension.getType() == type) {
					return extension;
				}
			}
		}
		return null;
	}
	
	public Scheduler getScheduler() {
		return scheduler;
	}
	
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
		if(children != null){
			for(ComputingResource child: children){
				child.setScheduler(scheduler);
			}	
		}
	}

	class ComputingResourceEventHandler implements EventHandler{
		
		public void handleResourceEvent(Event event){
			if (extensionList != null) {
				for (Extension extension : extensionList) {
					if (extension.supportsEvent(event)) {
						extension.handleEvent(event);
					}
				}
			}
			if(event.getReason() != EventReason.SIM_INIT)
				triggerEventUp(event);
		}
		
		public void handleSchedulingEvent(SchedulingEvent event){

			if(scheduler != null && (parent != null && scheduler != parent.getScheduler())/*scheduler.getResources().contains(this)*/){
				String src = event.getSource() != null ? event.getSource() : name;
				scheduler.sendInternal(GridSimTags.SCHEDULE_NOW, DCWormsTags.UPDATE_PROCESSING, src);
			} else if(parent != null)
				parent.getEventHandler().handleSchedulingEvent(event);
			
			//TODO - check if needed
			//if(event.getReason() != EventReason.SIM_INIT)
			//	triggerEventUp(event);
		}
	}
	
	public EventHandler getEventHandler(){
		return new ComputingResourceEventHandler();
	}
	
	public void initiate(){
		
		ResourceEventCommand rec = new ResourceEventCommand(this);
		EnergyEvent event = new EnergyEvent(EnergyEventType.AIRFLOW_STATE_CHANGED, "Resource controller");
		event.setReason(EventReason.SIM_INIT);
		rec.execute(event);
		/*ResourceEventCommand*/ rec = new ResourceEventCommand(this);
		/*EnergyEvent*/ event = new EnergyEvent(EnergyEventType.POWER_STATE_CHANGED, "Resource controller");
		event.setReason(EventReason.SIM_INIT);
		rec.execute(event);
		
		//alternative way
		//getEventHandler().handleResourceEvent(new EnergyEvent(EnergyEventType.AIRFLOW_STATE_CHANGED, "Resource controller"));
		//getEventHandler().handleResourceEvent(new EnergyEvent(EnergyEventType.POWER_STATE_CHANGED, "Resource controller"));
	}
	
	
	private void accept(EnergyExtension e){
		extensionList.add(e);
	}
}
