package model.resources.computing.profiles.energy;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import model.events.Event;
import model.resources.computing.ComputingResource;
import model.resources.computing.extensions.Extension;
import model.resources.computing.extensions.ExtensionException;
import model.resources.computing.extensions.ExtensionType;
import model.resources.computing.profiles.energy.power.PowerInterfaceFactory;
import model.resources.computing.profiles.energy.power.PowerProfile;
import model.resources.computing.profiles.energy.power.ui.PowerInterface;
import model.scheduling.manager.tasks.JobRegistryImpl;

public class EnergyExtension implements Extension{

	private Log log = LogFactory.getLog(EnergyExtension.class);

	protected PowerInterface powerInterface;
	protected PowerProfile powerProfile;
	
  	protected ComputingResource computingResource;


	public EnergyExtension(ComputingResource computingResource, PowerInterface powerInterface, PowerProfile powerProfile) {
		this.powerProfile = powerProfile;
		this.powerInterface = PowerInterfaceFactory.createPowerInterface(computingResource, powerProfile);
	}
	
	public EnergyExtension(ComputingResource computingResource, PowerProfile powerProfile) {
		super();
		this.computingResource = computingResource;
		this.powerProfile = powerProfile;
		this.powerInterface = PowerInterfaceFactory.createPowerInterface(computingResource, powerProfile);
	}

	public boolean supportsEvent(Event event) {

		if(powerProfile == null || powerProfile.getEnergyEstimationPlugin() == null)
			return false;
		if(event.getType().getName().equals(EnergyEventType.POWER_STATE_CHANGED.getName()))
			return true;
		else if(event.getType().getName().equals(EnergyEventType.FREQUENCY_CHANGED.getName()))
			return true;
		else if(event.getType().getName().equals(EnergyEventType.TASK_STARTED.getName()))
			return true;
		else if(event.getType().getName().equals(EnergyEventType.TASK_FINISHED.getName()))
			return true;
		else if(event.getType().getName().equals(EnergyEventType.RESOURCE_UTILIZATION_CHANGED.getName()))
			return true;
	
		if(event.getType().getName().equals(EnergyEventType.AIRFLOW_STATE_CHANGED.getName()))
			return true;
		
		else return false;
	}

	public void handleEvent(Event event) {
		
		EnergyEvent enEvent = (EnergyEvent)event;
		double power = 0;
		double temperature = 0;
		try{
			switch (enEvent.getType()) {
			
			case POWER_STATE_CHANGED:
				power = powerProfile.getEnergyEstimationPlugin().estimatePowerConsumption(enEvent, new JobRegistryImpl(computingResource.getName()), computingResource);
				/*if(computingResource instanceof ComputingNode){
					ComputingNode node = (ComputingNode)computingResource;
					if(event.getData() instanceof PowerState){
						PowerState newState = (PowerState)event.getData();
						if(newState == PowerState.ON) {
							addToPowerUsageHistory(power+node.getPowerInterface().START_COST);
							addToPowerUsageHistory(DateTimeUtils.currentTimeMillis() + node.getPowerInterface().START_TIME, power);
						}else if(newState == PowerState.OFF){
							addToPowerUsageHistory(power+node.getPowerInterface().SHUTDOWN_COST);
							addToPowerUsageHistory(DateTimeUtils.currentTimeMillis() + node.getPowerInterface().SHUTDOWN_TIME, power);
						}
					}
				}
				else*/ powerProfile.addToPowerUsageHistory(power);
				break;
				
			case FREQUENCY_CHANGED:
				power = powerProfile.getEnergyEstimationPlugin().estimatePowerConsumption(enEvent, new JobRegistryImpl(computingResource.getName()), computingResource);
				powerProfile.addToPowerUsageHistory(power);
				break;
				
			case TASK_STARTED:
				power = powerProfile.getEnergyEstimationPlugin().estimatePowerConsumption(enEvent, new JobRegistryImpl(computingResource.getName()), computingResource);
				powerProfile.addToPowerUsageHistory(power);
				break;
	
			case TASK_FINISHED:
				//System.out.println(this.resource.getName() + " - ENERGY EXTENSION: TASK FINISHED");
				power = powerProfile.getEnergyEstimationPlugin().estimatePowerConsumption(enEvent, new JobRegistryImpl(computingResource.getName()), computingResource);
				//System.out.println(this.resource.getName() + " - ESTIMATED ENERGY:" + power);
				powerProfile.addToPowerUsageHistory(power);
				break;
				
			case RESOURCE_UTILIZATION_CHANGED:
				power = powerProfile.getEnergyEstimationPlugin().estimatePowerConsumption(enEvent, new JobRegistryImpl(computingResource.getName()), computingResource);
				powerProfile.addToPowerUsageHistory(power);
				break;
				
			}
		}catch(Exception e){
			
		}
	}

	public void init(Properties properties) throws ExtensionException {
		// TODO Auto-generated method stub
	}

	public ExtensionType getType() {
		return ExtensionType.ENERGY_EXTENSION;
	}

	/*public ComputingResource getResource() {
		return computingResource;
	}

	public void setResource(ComputingResource compRes){
		this.computingResource = compRes;
	}*/

	public PowerInterface getPowerInterface() {
		return powerInterface;
	}

	public PowerProfile getPowerProfile() {
		return powerProfile;
	}


}
