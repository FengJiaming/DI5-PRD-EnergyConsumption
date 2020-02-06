package model.resources.computing.profiles.energy.power.ui;


import java.util.Map;

import model.resources.ResourceStatus;
import model.resources.computing.ComputingResource;
import model.resources.computing.profiles.energy.EnergyEvent;
import model.resources.computing.profiles.energy.EnergyEventType;
import model.resources.computing.profiles.energy.power.PState;
import model.resources.computing.profiles.energy.power.PowerProfile;
import model.resources.computing.profiles.energy.power.PowerStateName;
import model.resources.computing.profiles.energy.power.StandardPowerStateName;

public class ProcessorPowerInterface extends ComputingResourcePowerInterface {

	protected PState currentPState;


	public ProcessorPowerInterface(ComputingResource resource, PowerProfile pp){
		super(resource, pp);
		currentPowerState = StandardPowerStateName.ON;
		currentPState = getLowestPState();
	}
	
	public boolean setPowerState(PowerStateName powerState){
		if(!supportPowerState(powerState))
			return false;
		if(powerState != currentPowerState){
			currentPowerState = powerState;
			if(powerState == StandardPowerStateName.OFF){
				resource.setStatus(ResourceStatus.UNAVAILABLE);
			} else if(powerState == StandardPowerStateName.ON){
				resource.setStatus(ResourceStatus.FREE);
			}
			resource.handleEvent(new EnergyEvent(EnergyEventType.POWER_STATE_CHANGED, resource.getName()));		
		}

		return true;
	}

	public PState getPState(){
		return currentPState;
	}
	
	public Map<String, PState> getSupportedPStates() {
		return powerProfile.getSupportedPStates();
	}
	
	public boolean supportPState(PState pState) {

		for(String pStateName: getSupportedPStates().keySet()){
			if(pState.getName().equals(pStateName)){
				return true;
			}
		}
		return false;
	}
	
	public boolean setPState(String pStateName){
		PState newPState = getSupportedPStates().get(pStateName);


		if(newPState != null && newPState != currentPState){
			//double factor = newPState.getFrequency()/currentPState.getFrequency();
			currentPState = newPState;
			//CpuSpeed speed = (CpuSpeed)resource.getResourceCharacteristic().getResourceUnits().get(StandardResourceUnitName.CPUSPEED).get(0);
			//speed.setAmount(Double.valueOf(currentPState.getFrequency()).intValue());
			//new ResourceEventCommand(resource).execute(EnergyEventType.FREQUENCY_CHANGED);
			resource.handleEvent(new EnergyEvent(EnergyEventType.FREQUENCY_CHANGED, resource.getName()));
			//resource.getScheduler().sendInternal(GridSimTags.SCHEDULE_NOW, DCWormsTags.UPDATE, resource.getName());
			return true;
		}

		return false;
	}
	
	public double getFrequency() {
		return currentPState.getFrequency();
	}

	public boolean setFrequency(double freq) {

		for(String pStateName: getSupportedPStates().keySet()){
			if(getSupportedPStates().get(pStateName).getFrequency() == freq){
				setPState(pStateName);
				return true;
			}
		}
		return false;
	}

	public double getPowerConsumption(PState state) throws NoSuchFieldException{
		double powerConsumption = 0;
		if(supportPState(state)){
			for(String pStateName: getSupportedPStates().keySet()){
				if(pStateName.equals(state.getName())){
					powerConsumption = getSupportedPStates().get(pStateName).getPower();
					break;
				}
			}
		} 
		else throw new NoSuchFieldException();
		return powerConsumption;
	}
	
	public PState getLowestPState(){
		PState lowPState = null;
		double highestFreq = Double.MIN_VALUE;
		for(String key: getSupportedPStates() .keySet()){
			PState pState = getSupportedPStates().get(key);
			if(pState.getFrequency() > highestFreq){
				highestFreq = pState.getFrequency();
				lowPState = pState;
			}
		}
		return lowPState;
	}
	
	public PState getHighestPState(){
		PState highPState = null;
		double lowestFreq = Double.MAX_VALUE;
		for(String key: getSupportedPStates() .keySet()){
			PState pState = getSupportedPStates().get(key);
			if(pState.getFrequency() < lowestFreq){
				lowestFreq = pState.getFrequency();
				highPState = pState;
			}
		}
		return highPState;
	}

}

