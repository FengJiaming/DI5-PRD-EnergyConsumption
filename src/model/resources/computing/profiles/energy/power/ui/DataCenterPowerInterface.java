package model.resources.computing.profiles.energy.power.ui;

import model.resources.computing.ComputingResource;
import model.resources.computing.DataCenter;
import model.resources.computing.profiles.energy.power.PowerProfile;
import model.resources.computing.profiles.energy.power.PowerStateName;
import model.resources.computing.profiles.energy.power.StandardPowerStateName;

public class DataCenterPowerInterface extends ComputingResourcePowerInterface{

	public DataCenterPowerInterface(ComputingResource resource, PowerProfile pp){
		super(resource, pp);
		currentPowerState = StandardPowerStateName.ON;
	}

	public boolean setPowerState(PowerStateName state) {
		if(!supportPowerState(state))
			return false;
		currentPowerState = state;
		DataCenter dataCenter = (DataCenter)resource;

		for(ComputingResource child: dataCenter.getChildren()){
			child.getPowerInterface().setPowerState(state);
		}

		return true;
	}




}
