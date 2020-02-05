package model.resources.units;

import model.Parameters;

public interface ResourceUnit {

	public ResourceUnitName getName();
	
	public ResourceUnitType getResourceUnitType();
	
	public String getResourceId();
	
	public int getFreeAmount() ;

	public int getUsedAmount();
	
	public int getAmount();
	
	public void setUsedAmount(int amount);
	
	public ResourceUnit toDiscrete() throws ClassNotFoundException;
	
	public void init(Parameters parameters);
	
	public Parameters getParameters();
	
	public ResourceUnitProvisioner getProvisioner();
}
