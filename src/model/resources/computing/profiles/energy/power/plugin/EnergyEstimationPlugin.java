package model.resources.computing.profiles.energy.power.plugin;

import model.Plugin;
import model.resources.computing.ComputingResource;
import model.resources.computing.profiles.energy.EnergyEvent;
import model.scheduling.manager.tasks.JobRegistry;

/*
 * Estimated air flow and temperature may be achieved in the future
 */
public interface EnergyEstimationPlugin extends Plugin {
	
	public double estimatePowerConsumption(EnergyEvent event, JobRegistry jobRegistry, ComputingResource resource);

}
