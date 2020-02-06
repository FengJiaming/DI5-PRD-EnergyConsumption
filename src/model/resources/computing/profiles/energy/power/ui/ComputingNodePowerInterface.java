package model.resources.computing.profiles.energy.power.ui;

import model.resources.ResourceStatus;
import model.resources.computing.ComputingNode;
import model.resources.computing.ComputingResource;
import model.resources.computing.profiles.energy.EnergyEvent;
import model.resources.computing.profiles.energy.EnergyEventType;
import model.resources.computing.profiles.energy.power.PowerProfile;
import model.resources.computing.profiles.energy.power.PowerStateName;
import model.resources.computing.profiles.energy.power.StandardPowerStateName;

public class ComputingNodePowerInterface extends ComputingResourcePowerInterface{

	public static long START_TIME = 600000;
	public static long SHUTDOWN_TIME = 300000;
	public static double START_COST = 4000;
	public static double SHUTDOWN_COST = 2000;

	
	public ComputingNodePowerInterface(ComputingResource resource, PowerProfile pp){
		super(resource, pp);
		currentPowerState = StandardPowerStateName.ON;
	}

	public boolean setPowerState(PowerStateName state){
		if(!supportPowerState(state))
			return false;
		currentPowerState = state;
		ComputingNode computingNode = (ComputingNode) resource;
		boolean pePowerStateChangeStatus = false;
		if(computingNode.getProcessors() != null)
		{
			for(ComputingResource child:computingNode.getProcessors()){
				if(child.getPowerInterface() != null){
					pePowerStateChangeStatus = child.getPowerInterface().setPowerState(state);	
				}
			}
		} 
		
		if(!pePowerStateChangeStatus){
			computingNode.handleEvent(new EnergyEvent(EnergyEventType.POWER_STATE_CHANGED, computingNode.getName()));
		}

		if(state == StandardPowerStateName.OFF){
			computingNode.setStatus(ResourceStatus.UNAVAILABLE);
		}
		else if(state == StandardPowerStateName.ON){
			computingNode.setStatus(ResourceStatus.FREE);
		}
		//computingNode.handleEvent(new EnergyEvent(EnergyEventType.POWER_STATE_CHANGED, computingNode.getName()));
		return true;
	}

	public void turnOn(){
		setPowerState(StandardPowerStateName.ON);
	}
	
	public void turnOff(){
		setPowerState(StandardPowerStateName.OFF);
	}



}