package example.energy;

import model.resources.ResourceStatus;
import model.resources.computing.ComputingResource;
import model.resources.computing.Processor;
import model.resources.computing.profiles.energy.EnergyEvent;
import model.resources.computing.profiles.energy.power.StandardPowerStateName;
import model.scheduling.manager.tasks.JobRegistry;

public class CPUEnergyEstimationPlugin extends BaseEnergyEstimationPlugin  {

	public double estimatePowerConsumption(EnergyEvent event, JobRegistry jobRegistry,
			ComputingResource resource) {
		double powerConsumption;
		Processor cpu = (Processor)resource;
		if(resource.getPowerInterface().getPowerState().equals(StandardPowerStateName.OFF))
			powerConsumption = 0;
		else {
			try {
				if(resource.getStatus() == ResourceStatus.BUSY)
					powerConsumption = cpu.getPowerInterface().getPowerConsumption(cpu.getPowerInterface().getPState());
				else {
					powerConsumption = cpu.getPowerInterface().getPowerConsumption(StandardPowerStateName.ON);
				}
			} catch (NoSuchFieldException e) {
				try {
					powerConsumption = cpu.getPowerInterface().getPowerConsumption(StandardPowerStateName.ON);
				} catch (NoSuchFieldException e1) {
					powerConsumption = 10;
				}
			}
		}
		return powerConsumption;
	}

}
