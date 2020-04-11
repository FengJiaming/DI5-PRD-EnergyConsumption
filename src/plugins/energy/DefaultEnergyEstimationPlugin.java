package plugins.energy;

import model.resources.computing.ComputingResource;
import model.resources.computing.profiles.energy.EnergyEvent;
import model.scheduling.manager.tasks.JobRegistry;


public class DefaultEnergyEstimationPlugin extends BaseEnergyEstimationPlugin{


	public double estimatePowerConsumption(EnergyEvent event, JobRegistry jobRegistry, ComputingResource resource) {
		double powerConsumption = 0;
		try {
			powerConsumption = resource.getPowerInterface().getPowerConsumption(resource.getPowerInterface().getPowerState());
		} catch (NoSuchFieldException e) {
			powerConsumption = 0;
		}
		
		powerConsumption = powerConsumption + getChildrenPowerConsumption(resource);
		
		for(ComputingResource child:resource.getChildren()){
			try {
				//powerConsumption = powerConsumption + child.getPowerInterface().getRecentPowerUsage().getValue(); 
			} catch (Exception e) {

			}
		}
		return powerConsumption;
	}
	
	private double getChildrenPowerConsumption(ComputingResource resource){
		double powerConsumption = 0;
		for(ComputingResource child:resource.getChildren()){
			try {
				powerConsumption = powerConsumption + child.getPowerInterface().getRecentPowerUsage().getValue(); 
			} catch (Exception e) {
				powerConsumption = powerConsumption + getChildrenPowerConsumption(child);
			}
		}
		return powerConsumption;
	}
}
