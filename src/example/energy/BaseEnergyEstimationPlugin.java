package example.energy;

import model.Parameters;
import model.PluginConfiguration;
import model.resources.computing.profiles.energy.power.plugin.EnergyEstimationPlugin;

public abstract class BaseEnergyEstimationPlugin implements EnergyEstimationPlugin {

	@Override
	public PluginConfiguration getConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void init(Parameters parameters) {

	}

	public String getName() {
		return getClass().getName();
	}
}
