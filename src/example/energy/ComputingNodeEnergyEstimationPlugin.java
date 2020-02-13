package example.energy;

import model.resources.computing.ComputingNode;
import model.resources.computing.ComputingResource;
import model.resources.computing.Processor;
import model.resources.computing.profiles.energy.EnergyEvent;
import model.scheduling.manager.tasks.JobRegistry;

public class ComputingNodeEnergyEstimationPlugin extends BaseEnergyEstimationPlugin {

	public double estimatePowerConsumption(EnergyEvent event, JobRegistry jobRegistry,
			ComputingResource resource) {
		double powerConsumption = 0;
		ComputingNode node = (ComputingNode) resource;
		for(Processor cpu: node.getProcessors()){
			try{
				powerConsumption = powerConsumption + cpu.getPowerInterface().getRecentPowerUsage().getValue();
			} catch (Exception e){
				
			}
		}
		try {
			powerConsumption = powerConsumption + node.getPowerInterface().getPowerConsumption(node.getPowerInterface().getPowerState());
		} catch (NoSuchFieldException e) {
		}

		return powerConsumption;
	}

}
