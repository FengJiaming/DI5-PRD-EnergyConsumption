package model.resources.computing.profiles.energy.power.ui;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTimeUtils;

import model.Parameters;
import model.resources.ResourceStatus;
import model.resources.computing.ComputingResource;
import model.resources.computing.profiles.energy.EnergyEvent;
import model.resources.computing.profiles.energy.EnergyEventType;
import model.resources.computing.profiles.energy.power.PowerProfile;
import model.resources.computing.profiles.energy.power.PowerState;
import model.resources.computing.profiles.energy.power.PowerStateName;
import model.resources.computing.profiles.energy.power.PowerUsage;
import model.resources.computing.profiles.energy.power.StandardPowerStateName;

public class ComputingResourcePowerInterface implements PowerInterface{

	protected PowerStateName currentPowerState;
	protected PowerProfile powerProfile;
	protected ComputingResource resource;
	
	public ComputingResourcePowerInterface(ComputingResource resource, PowerProfile powerProfile){
		this.resource = resource;
		this.powerProfile = powerProfile;
		this.currentPowerState = StandardPowerStateName.ON;
	}
	
	public boolean setPowerState(PowerStateName state) {
		if(supportPowerState(state)){
			currentPowerState = state;

			for(ComputingResource child:resource.getChildren()){
				if(child.getPowerInterface().supportPowerState(state)){
					boolean status = child.getPowerInterface().setPowerState(state);
				}
			}

			if(state == StandardPowerStateName.OFF){
				resource.setStatus(ResourceStatus.UNAVAILABLE);
			}
			else if(state == StandardPowerStateName.ON){
				resource.setStatus(ResourceStatus.FREE);
			}
			//notifications from all levels
			resource.handleEvent(new EnergyEvent(EnergyEventType.POWER_STATE_CHANGED, resource.getName()));

			//notification from last level
			//if (resource.getChildren() == null) resource.handleEvent(new EnergyEvent(EnergyEventType.POWER_STATE_CHANGED, resource));
			
			return true;
		}
		return false;
	}
	

	public PowerStateName getPowerState() {
		return currentPowerState;
	}

	public List<PowerState> getSupportedPowerStates() throws NoSuchFieldException {
		List<PowerState> powerStates = new ArrayList<PowerState>();
		for(PowerState powerState: powerProfile.getSupportedPowerStates()){
			powerStates.add(powerState);
		}
		return powerStates;
	}

	public boolean supportPowerState(PowerStateName state) {
		try {
			for(PowerState powerState: powerProfile.getSupportedPowerStates()){
				if(powerState.getName().equals(state)){
					return true;
				}
			}
		} catch (NoSuchFieldException e) {
			return false;
		}
		return false;
		
	}
	
	public double getPowerConsumption(PowerStateName state) throws NoSuchFieldException {
		double powerConsumption = 0;
		if(supportPowerState(state)){
			for(PowerState powerState: powerProfile.getSupportedPowerStates()){
				if(powerState.getName().equals(state)){
					powerConsumption = powerState.getPowerUsage();
					break;
				}
			}
		} else {
			throw new NoSuchFieldException("Power state not supported");
		}
		return powerConsumption;
	}

	public PowerUsage getRecentPowerUsage() {
		PowerUsage powerUsage = null;
		int lastIdx = getPowerUsageHistory().size() - 1;
		if(lastIdx >= 0)
			powerUsage = getPowerUsageHistory().get(lastIdx);
		else {	
			try {
				powerUsage = new PowerUsage(DateTimeUtils.currentTimeMillis(), getPowerConsumption(currentPowerState));
			} catch (NoSuchFieldException e) {
			}
		}
		return powerUsage;
	}
	
	public List<PowerUsage> getPowerUsageHistory(){
		return powerProfile.getPowerUsageHistory();
	}
	
	public Parameters getParameters() {
		return powerProfile.getParameters();
	}

}
