package model.resources.units;


public interface ResourceUnitProvisioner {

	public int getPending();

	public void setPending(int pending);
	
	public ResourceUnitState getState();
	
	public void setState(ResourceUnitState newState);
}
