package controller.simulator.stats.implementation.out;

import controller.simulator.stats.implementation.AccumulatedResourceStats;
import controller.simulator.stats.implementation.GSSAccumulatorsStats;
import controller.simulator.stats.implementation.JobStats;
import controller.simulator.stats.implementation.ResourcePowerStats;
import controller.simulator.stats.implementation.ResourceStats;
import controller.simulator.stats.implementation.ResourceUsageStats;
import controller.simulator.stats.implementation.TaskStats;

public interface StatsSerializer {

	public Object visit(TaskStats arg);
	
	public Object visit(JobStats arg);
	
	public Object visit(ResourceStats arg);
	
	public Object visit(AccumulatedResourceStats arg);
	
	public Object visit(ResourceUsageStats arg);
	
	public Object visit(ResourcePowerStats arg);

	public Object visit(GSSAccumulatorsStats arg);
	
}
