package model.scheduling.plugin.estimation;


import java.util.Map;

import gridsim.schedframe.ExecTask;
import model.Plugin;
import model.events.scheduling.SchedulingEvent;
import model.resources.units.ResourceUnit;
import model.resources.units.ResourceUnitName;

public interface ExecutionTimeEstimationPlugin extends Plugin {

	/**
	 * 
	 * @param allocatedResources resource units allocated for task execution
	 * @param task which will be executed
	 * @param remainingLength 
	 * @return estimated execution time of a task of specified length
	 */
	public double execTimeEstimation(SchedulingEvent event, ExecTask task, Map<ResourceUnitName, ResourceUnit> allocatedResources, 
									double completionPercentage);
	

}
