package example.timeestimation;


import java.util.Map;

import gridsim.schedframe.ExecTask;
import model.events.scheduling.SchedulingEvent;
import model.resources.units.PEUnit;
import model.resources.units.ResourceUnit;
import model.resources.units.ResourceUnitName;
import model.resources.units.StandardResourceUnitName;



public class DefaultTimeEstimationPlugin extends BaseTimeEstimationPlugin{

	/*
	 * This method should return an estimation of time required to execute the task.
	 * Requested calculation should be done based on the resources allocated for the task,
	 * task description and task completion percentage.
	 * 
	 * Example implementation calculate the estimation based on cpu processing power.
	 * There is also a simple assumption, that cpu processing power is a linear function
	 * of number of allocated cpus and their speed.
	 */
	public double execTimeEstimation(SchedulingEvent event, ExecTask task,
			Map<ResourceUnitName, ResourceUnit> allocatedResources,
			double completionPercentage) {
		
		// collect all information necessary to do the calculation
		PEUnit peUnit = (PEUnit) allocatedResources.get(StandardResourceUnitName.PE);

		// obtain single pe speed
		int speed = peUnit.getSpeed();

		// number of used pe  
		int cnt = peUnit.getUsedAmount();

		// estimate remainingTaskLength
		double remainingLength =  task.getLength() * (1 - completionPercentage/100);
		
		// do the calculation
		double execTime = (remainingLength / (cnt * speed));

		// if the result is very close to 0, but less then one millisecond then round this result to 0.001
		if (Double.compare(execTime, 0.001) < 0) {
			execTime = 0.001;
		}

		// time is measured in integer units, so get the nearest execTime int value.
		execTime = Math.round(execTime);
		return execTime;
	}

}
