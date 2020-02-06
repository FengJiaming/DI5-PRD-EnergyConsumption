package model.resources.computing.profiles.energy.power;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTimeUtils;

import model.Parameters;
import model.resources.computing.profiles.energy.power.plugin.EnergyEstimationPlugin;

public class PowerProfile {

	protected List<PowerUsage> powerUsage;
	protected EnergyEstimationPlugin energyEstimationPlugin;

	protected List<PowerState> supportedPowerStates;
	protected Map<String, PState> supportedPStates;
	
	protected Parameters parameters;
	

	public PowerProfile(EnergyEstimationPlugin energyEstimationPlugin, List<PowerState> powerStates) {
		this.energyEstimationPlugin = energyEstimationPlugin;
		this.powerUsage = new ArrayList<PowerUsage>();
		this.supportedPowerStates = powerStates;
	}
	
	public PowerProfile(EnergyEstimationPlugin energyEstimationPlugin, List<PowerState> powerStates,  List<PState> pStates) {
		this.energyEstimationPlugin = energyEstimationPlugin;
		this.supportedPowerStates = powerStates;
		this.powerUsage = new ArrayList<PowerUsage>();
		if(pStates.size() > 0)
			this.supportedPStates = new HashMap<String, PState>();
		for(PState pState: pStates){
			supportedPStates.put(pState.getName(), pState);
		}
	}
	
	/*public void initPlugin(String energyEstimationPluginClassName){
		if(energyEstimationPluginClassName != null) {
			energyEstimationPlugin = (EnergyEstimationPluginInterface) InstanceFactory.createInstance(
					energyEstimationPluginClassName, EnergyEstimationPluginInterface.class);
			if(energyEstimationPlugin == null){
				energyEstimationPlugin = new DefaultEnergyEstimationPlugin();
				log.info("Using default energy estimation plugin: " + DefaultEnergyEstimationPlugin.class.getName());
				
			} else {
				energyEstimationPlugin.init(null);
			}		
		} else {
			energyEstimationPlugin = new DefaultEnergyEstimationPlugin();
			log.info("Using default energy estimation plugin: " + DefaultEnergyEstimationPlugin.class.getName());
		}
	}*/
	
	public EnergyEstimationPlugin getEnergyEstimationPlugin() {
		return energyEstimationPlugin;
	}
	
	public List<PowerState> getSupportedPowerStates() throws NoSuchFieldException{
		if(supportedPowerStates == null)
			throw new NoSuchFieldException("Supported power states are not defined.");
		return supportedPowerStates;
	}
	
	public Map<String, PState> getSupportedPStates() {
		if(supportedPStates == null)
			return new HashMap<String, PState>();
		return supportedPStates;
	}
	
	public void addToPowerUsageHistory(double power) {

		if (powerUsage.size() == 0) {
			PowerUsage usage = new PowerUsage(DateTimeUtils.currentTimeMillis(), power);
			powerUsage.add(usage);
			return;
		}

		int lastIdx = powerUsage.size() - 1;
		double lastPower = powerUsage.get(lastIdx).getValue();
		if (lastPower != power) {
			PowerUsage usage = powerUsage.get(lastIdx);
			long currentTime = DateTimeUtils.currentTimeMillis();
			if (usage.getTimestamp() == currentTime) {
				usage.setValue(power);
				if(lastIdx > 0 && powerUsage.get(lastIdx - 1).getValue() == power)
					powerUsage.remove(usage);
			} else {
				usage = new PowerUsage(DateTimeUtils.currentTimeMillis(), power);
				powerUsage.add(usage);
			}
		}
	}

	public List<PowerUsage> getPowerUsageHistory() {
		return powerUsage;
	}

	public void init(Parameters params){
		this.parameters = params;
	}
	
	public Parameters getParameters() {
		return parameters;
	}


}
