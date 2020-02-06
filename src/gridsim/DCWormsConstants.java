package gridsim;

import eduni.simjava.Sim_stat;

public class DCWormsConstants {

	public static final String START_TIME="start_time";
	
	public static final String END_TIME = "end_time";
	
	public static final String RESOURCES = "resources";

	
	/**
	 * the links have the maximal baud rate - no delays
	 */
	public static final double DEFAULT_BAUD_RATE = Double.MAX_VALUE;
	
	/**
	 * Default name suffix of the allocation policy for every resource
	 */
	public static final String DEFAULT_RESOURCE_MANAGER_NAME = "ResourceManager";
	
	/**
	 * The cost of processing per time unit
	 */
	public static final float DEFAULT_RESOURCE_PROCESSING_COST = 1f;
	
	/**
	 * The default number of tasks in a single job
	 */
	public static final int DEFAULT_TASK_COUNT_IN_SINGLE_JOB = 1;

	/**
	 * The name of the broker interface entity (the entity that invokes proper methods on the given plugin)
	 */
	public static final String BrokerInterfaceEntityName = "BrokerInterfaceEntity";
	
	/**
	 * The name of the output file with generated graphs
	 */
	public static final String GraphsFileName = "GssGraphs.sjg";
	
	/**
	 * The name of the file where the resources' statistical data are to be stored
	 */
	public static final String resourceStatisticsOutputFileName = "Stats_Resources.txt"; 
	
	/**
	 * The name of the file where the tasks' statistical data are to be stored
	 */
	public static final String tasksStatisitcsOutputFileName = "Stats_Tasks.txt";  

	
	public static final String MANAGEMENT_SYSTEM = "ManagementSystem";

	
/*
 *  
 * STATISTICAL INFORMATION VARIABLES AND METHODS 
 * 
 */
	
	/**
	 * The utilization measure name
	 */
	public static final String USAGE_MEASURE_NAME = "Resource's utilization";
	
	/**
	 * The waiting tasks queue length measure name
	 */
	public static final String TASKS_QUEUE_LENGTH_MEASURE_NAME = "Mean queue task count";
	
	/**
	 * The paused tasks list length measure name
	 */
	public static final String PAUSED_TASKS_LIST_LENGTH_MEASURE_NAME = "Paused tasks list length";
	
	/**
	 * @param queuesCount the number of queues for which the measures are to be added
	 * @return a Sim_stat object to be used in all resources
	 */
	public static Sim_stat getResourcesStatisticsObject(int queuesCount) {		
		Sim_stat stat = new Sim_stat();
		//stat.add_measure(Sim_stat.ARRIVAL_RATE);
		//stat.add_measure(Sim_stat.QUEUE_LENGTH);
		//stat.add_measure(Sim_stat.RESIDENCE_TIME);
		//stat.add_measure(Sim_stat.SERVICE_TIME);
		stat.add_measure(Sim_stat.THROUGHPUT);
		//stat.add_measure(Sim_stat.UTILISATION);
		//stat.add_measure(Sim_stat.WAITING_TIME);
		
		stat.add_measure(USAGE_MEASURE_NAME, Sim_stat.STATE_BASED, true); //continuous
		for (int i = 0; i < queuesCount; ++i) {
			stat.add_measure(TASKS_QUEUE_LENGTH_MEASURE_NAME+"_"+Integer.toString(i), Sim_stat.STATE_BASED, true); //continuous
		}
		stat.add_measure(PAUSED_TASKS_LIST_LENGTH_MEASURE_NAME, Sim_stat.STATE_BASED, true); //continuous
		//stat.add_measure(TASKS_AR_QUEUE_LENGTH_MEASURE_NAME, Sim_stat.STATE_BASED, true); //continuous
		return stat;
	}

}