package model.resources.computing.profiles.energy.power.ui;

import java.util.List;

import model.Parameters;
import model.resources.computing.profiles.energy.power.PowerState;
import model.resources.computing.profiles.energy.power.PowerStateName;
import model.resources.computing.profiles.energy.power.PowerUsage;

public interface PowerInterface {

	public PowerStateName getPowerState();
	
	public boolean setPowerState(PowerStateName powerState);
	
	public boolean supportPowerState(PowerStateName powerState);
	
	public List<PowerState> getSupportedPowerStates() throws NoSuchFieldException;
	
	public double getPowerConsumption(PowerStateName state) throws NoSuchFieldException;
	
	public PowerUsage getRecentPowerUsage();
	
	//public double getCurrentPowerConsumption();
	
	List<PowerUsage> getPowerUsageHistory();
	
	public Parameters getParameters();
}
