package controller.simulator.stats;

public interface SimulationStatistics {
	
	public static final String TASK_WAITING_TIME = "Task waiting time";

	public static final String MAKESPAN = "Makespan";
	
	public static final String TASK_COMPLETION_TIME = "Task completion time";
	
	public static final String DELAYED_TASKS = "Delayed tasks";

	public static final String TASK_TARDINESS = "Task tardiness";

	public static final String TASK_LATENESS = "Task lateness";
	
	public static final String TASK_START_TIME = "Task start time";

	public static final String TASK_FLOW_TIME = "Task flow time";

	public static final String TASK_EXECUTION_TIME = "Task execution time";

	public static final String FAILED_REQUESTS = "Failed requests (tasks)";
	
	public static final String RESOURCES_TOTAL_LOAD = "Resources total load";

	public static final String RESOURCES_RESERVATION_LOAD = "Resources reservation load";
	
	public static final String ENERGY_USAGE = "Energy usage";

	public static final String RESOURCES_QUEUE_LENGTH = "Resources queue length";

	
	public void generateStatistics();
	
	public void gatherTaskStatistics();
	

	
	public void saveSimulationStatistics();
	


	public String getOutputFolderName();

	public GSSAccumulator getStats(String resourcesTotalLoad);

	public boolean accumulatedStats();

	
}
