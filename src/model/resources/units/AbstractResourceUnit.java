package model.resources.units;

import model.Parameters;

public abstract class AbstractResourceUnit implements ResourceUnit, Cloneable {
	
	protected ResourceUnitName name;
	
	protected ResourceUnitType resourceType;;

	protected String resourceId;
	
	protected ResourceUnitProvisioner provisioner;
	
	
	public AbstractResourceUnit(AbstractResourceUnit unit){
		this.name = unit.name;
		this.resourceType = unit.resourceType;
		this.resourceId = unit.resourceId;
		this.provisioner = new SimpleResourceUnitProvisioner(unit, ResourceUnitState.FREE, 0);
	}
	
	public AbstractResourceUnit(ResourceUnitName name) {
		this.name = name;
		resourceId = "";
		resourceType = ResourceUnitType.CONTINUOUS_RESOURCE;
		this.provisioner = new SimpleResourceUnitProvisioner(this, ResourceUnitState.FREE, 0);
	}

	public AbstractResourceUnit(ResourceUnitName name, String resId) {
		this(name);
		resourceId = resId;
		resourceType = ResourceUnitType.CONTINUOUS_RESOURCE;
		this.provisioner = new SimpleResourceUnitProvisioner(this, ResourceUnitState.FREE, 0);
	}
	
	public ResourceUnitName getName() {
		return name;
	}

	public String getResourceId() {
		return resourceId;
	}
	
	public ResourceUnitType getResourceUnitType() {
		return this.resourceType;
	}
	
	public boolean equals(Object o){
		if(o instanceof AbstractResourceUnit){
			AbstractResourceUnit unit = (AbstractResourceUnit) o;
			if(this.name != unit.getName()) return false;
			if(!this.resourceId.equals(unit.getResourceId())) return false;
			if(this.getFreeAmount() != unit.getFreeAmount()) return false;
			if(this.getUsedAmount() != unit.getUsedAmount()) return false;
			return true;
		} else {
			return false;
		}
	}
	
	public Object clone(){
		Object obj = null;
		try {
			obj = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public void reset(){};
	
	public void init(Parameters parameters){
		
	}
	
	public ResourceUnitProvisioner getProvisioner() {
        return provisioner;
	}
	
	public Parameters getParameters(){
		return null;
	}

	class SimpleResourceUnitProvisioner implements ResourceUnitProvisioner{

		protected ResourceUnit primaryUnit;
		protected ResourceUnitState state;
		protected int pending;
		
		public SimpleResourceUnitProvisioner (ResourceUnit primaryUnit, ResourceUnitState state, int pending) {
			this.primaryUnit = primaryUnit;
			this.state = state;
			this.pending = pending;
		}

		public ResourceUnitState getState() {
			return state;
		}

		public void setState(ResourceUnitState newState) {

			if(newState == ResourceUnitState.FREE){
				primaryUnit.setUsedAmount(primaryUnit.getUsedAmount() - getAmount());
			} else if(newState == ResourceUnitState.PENDING){
				primaryUnit.getProvisioner().setPending(primaryUnit.getProvisioner().getPending() + getAmount());
			} else if(state == ResourceUnitState.PENDING && newState == ResourceUnitState.BUSY){
				primaryUnit.getProvisioner().setPending(primaryUnit.getProvisioner().getPending() - getAmount());
				primaryUnit.setUsedAmount(primaryUnit.getUsedAmount() + getAmount());
			} else if(state == ResourceUnitState.FREE && newState == ResourceUnitState.BUSY){
				primaryUnit.setUsedAmount(primaryUnit.getUsedAmount() + getAmount());
			}
			state = newState;
		}
		
		public int getPending() {
			return pending;
		}

		public void setPending(int pending) {
			this.pending = pending;
		}
		
	}
}