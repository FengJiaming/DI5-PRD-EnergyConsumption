package example.timeestimation;

import schedframe.Parameters;
import schedframe.PluginConfiguration;
import schedframe.scheduling.plugin.estimation.ExecutionTimeEstimationPlugin;

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
