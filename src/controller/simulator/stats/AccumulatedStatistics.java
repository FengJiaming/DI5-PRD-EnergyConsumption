package controller.simulator.stats;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import gridsim.Accumulator;


public class AccumulatedStatistics {

	protected static final String ACCUMULATED_SIMULATIONS_STATISTICS_OUTPUT_FILE_NAME = "Stats_Accumulated_Simulations.txt";  
	
	protected List<SimulationStatistics> simulations;

	public AccumulatedStatistics(int noOfSimulations) {
		simulations = new ArrayList<SimulationStatistics>(noOfSimulations);
	}

	public AccumulatedStatistics() {
		simulations = new ArrayList<SimulationStatistics>();
	}

	public void add(SimulationStatistics simulationStatistics) {
		simulations.add(simulationStatistics);
	}

	public void saveStatistics() {		

		SimulationStatistics simulationStatisticsNew = simulations.get(0);

		if(!simulationStatisticsNew.accumulatedStats())
			return;
		String dirName = simulationStatisticsNew.getOutputFolderName();
		if(dirName == null)
			return;

		PrintStream out = null;
		try {
			File file = new File(dirName + ACCUMULATED_SIMULATIONS_STATISTICS_OUTPUT_FILE_NAME);
			out = new PrintStream(new FileOutputStream(file));
		}  catch (IOException e) {
			e.printStackTrace();
		}
		
		GSSAccumulator resourceLoad = getAccumulatedResourceLoad();
		GSSAccumulator reservationLoad = getAccumulatedReservationLoad();
		GSSAccumulator makespan = getAccumulatedMakespan();
		GSSAccumulator taskExecutionTime = getAccumulatedJobExecutionTime();
		GSSAccumulator taskQueueLength = getAccumulatedQueueJobCount();
		GSSAccumulator taskCompletionTime = getAccumulatedCompletionTime();
		GSSAccumulator taskWaitingTime = getAccumulatedWaitingTime();
		GSSAccumulator taskFlowTime = getAccumulatedJobFlowTime();
		GSSAccumulator taskLateness = getAccumulatedLateness();
		GSSAccumulator delayedTasks = getAccumulatedDelayedTasks();
		GSSAccumulator taskTardiness = getAccumulatedTardiness();
		GSSAccumulator failedRequests = getAccumulatedFailedRequests();
		
		out.print("Delayed tasks (accumulated)");
		out.println(delayedTasks);
		out.print("Failed requests (accumulated)");
		out.println(failedRequests);
		out.print("Makespan (accumulated)");
		out.println(makespan);		
		out.print("Resources queue length (accumulated)");
		out.println(taskQueueLength);
		out.print("Mean (accumulated) resource load");
		out.println(resourceLoad);		
		out.print("Mean (accumulated) reservation load");
		out.println(reservationLoad);		
		out.print("Task completion time (accumulated)");
		out.println(taskCompletionTime);		
		out.print("Task execution time (accumulated)");
		out.println(taskExecutionTime);		
		out.print("Task flow time (accumulated)");
		out.println(taskFlowTime);
		out.print("Task waiting time (accumulated)");
		out.println(taskWaitingTime);
		out.print("Task lateness (accumulated)");
		out.println(taskLateness);		
		out.print("Task tardiness (accumulated)");
		out.println(taskTardiness);		
		out.println(); 
		out.close();
	}

	public GSSAccumulator getAccumulatedResourceLoad() {
		GSSAccumulator resourceMeanLoad = new GSSAccumulator();	
		for (SimulationStatistics simStat : simulations) {
			GSSAccumulator simulationResTotalLoad = simStat.getStats(SimulationStatistics.RESOURCES_TOTAL_LOAD); 
			resourceMeanLoad.add(simulationResTotalLoad.getMean());
		}
		return resourceMeanLoad; 
	}

	public GSSAccumulator getAccumulatedReservationLoad(){
		GSSAccumulator reservationMean = new GSSAccumulator();
		for (SimulationStatistics simStat : simulations) {
			Accumulator simulationResvTotalLoad = simStat.getStats(SimulationStatistics.RESOURCES_RESERVATION_LOAD);
			reservationMean.add(simulationResvTotalLoad.getMean());
		}
		return reservationMean;
	}

	/*public GSSAccumulator getAccumulatedBokrerThroughput() {
		GSSAccumulator meanThroughput = new GSSAccumulator();
		
		for (SimulationStatisticsNew simStat : simulations) {
			double simulationMeanThroughput = simStat.getBrokerStats().average(Sim_stat.THROUGHPUT);
			meanThroughput.add(simulationMeanThroughput);
		}
		return meanThroughput;
	}*/

	public GSSAccumulator getAccumulatedJobFlowTime() {
		GSSAccumulator meanJobFlowTime = new GSSAccumulator();
		for (SimulationStatistics simStat : simulations) {
			Accumulator simulationJobFlowTime = simStat.getStats(SimulationStatistics.TASK_FLOW_TIME);
			meanJobFlowTime.add(simulationJobFlowTime.getMean());
		}
		return meanJobFlowTime;
	}

	public GSSAccumulator getAccumulatedLateness() {
		GSSAccumulator meanLateness = new GSSAccumulator();
		for (SimulationStatistics simStat : simulations) {
			Accumulator simulationLateness = simStat.getStats(SimulationStatistics.TASK_LATENESS);
			meanLateness.add(simulationLateness.getMean());
		}
		return meanLateness;
	}

	public GSSAccumulator getAccumulatedFailedRequests() {
		GSSAccumulator meanFailedRequests = new GSSAccumulator();
		for (SimulationStatistics simStat : simulations) {
			Accumulator simulationFailedRequestes = simStat.getStats(SimulationStatistics.FAILED_REQUESTS);
			meanFailedRequests.add(simulationFailedRequestes.getMean());
		}
		return meanFailedRequests;
	}

	public GSSAccumulator getAccumulatedTardiness() {
		GSSAccumulator meanTardiness = new GSSAccumulator();
		for (SimulationStatistics simStat : simulations) {
			Accumulator simulationJobTardiness = simStat.getStats(SimulationStatistics.TASK_TARDINESS);
			meanTardiness.add(simulationJobTardiness.getMean());
		}
		return meanTardiness;
	}

	public GSSAccumulator getAccumulatedDelayedTasks() {
		GSSAccumulator delayedTasks = new GSSAccumulator();
		for (SimulationStatistics simStat : simulations) {
			Accumulator simulationDelayedTasks = simStat.getStats(SimulationStatistics.DELAYED_TASKS);
			delayedTasks.add(simulationDelayedTasks.getMean());
		}
		return delayedTasks;
	}

	public GSSAccumulator getAccumulatedCompletionTime() {
		GSSAccumulator meanJobCompletionTime = new GSSAccumulator();
		
		for (SimulationStatistics simStat : simulations) {
			Accumulator simulationJobTurnaroundTime = simStat.getStats(SimulationStatistics.TASK_COMPLETION_TIME);
			meanJobCompletionTime.add(simulationJobTurnaroundTime.getMean());
		}
		return meanJobCompletionTime;
	}

	public GSSAccumulator getAccumulatedMakespan() {
		GSSAccumulator meanMakespan = new GSSAccumulator();
		for (SimulationStatistics simStat : simulations) {
			meanMakespan.add(simStat.getStats(SimulationStatistics.MAKESPAN).getMean());
		}
		return meanMakespan;
	}

	public GSSAccumulator getAccumulatedWaitingTime() {
		GSSAccumulator meanWaitingTime = new GSSAccumulator();
		for (SimulationStatistics simStat : simulations)
			meanWaitingTime.add(simStat.getStats(SimulationStatistics.TASK_WAITING_TIME).getMean());
		return meanWaitingTime;
	}

	public GSSAccumulator getAccumulatedJobExecutionTime() {
		GSSAccumulator meanJobExecutionTime = new GSSAccumulator();
		for (SimulationStatistics simStat : simulations)
			meanJobExecutionTime.add(simStat.getStats(SimulationStatistics.TASK_EXECUTION_TIME).getMean());
		return meanJobExecutionTime;
	}

	public GSSAccumulator getAccumulatedQueueJobCount() {
		GSSAccumulator meanQueuJobCount = new GSSAccumulator();
		for (SimulationStatistics simStat : simulations)
			meanQueuJobCount.add(simStat.getStats(SimulationStatistics.RESOURCES_QUEUE_LENGTH).getMean());
		return meanQueuJobCount;
	}

}