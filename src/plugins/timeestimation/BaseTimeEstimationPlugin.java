package plugins.timeestimation;

import model.Parameters;
import model.PluginConfiguration;
import model.scheduling.plugin.estimation.ExecutionTimeEstimationPlugin;

public abstract class BaseTimeEstimationPlugin implements ExecutionTimeEstimationPlugin{
	
	public PluginConfiguration getConfiguration() {
		return null;
	}

	public String getName() {
		return getClass().getName();
	}

	public void init(Parameters parameters) {
	}
}
