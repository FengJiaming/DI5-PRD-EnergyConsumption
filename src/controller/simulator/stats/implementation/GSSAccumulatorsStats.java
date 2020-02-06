package controller.simulator.stats.implementation;

import controller.simulator.stats.GSSAccumulator;
import controller.simulator.stats.implementation.out.StatsSerializer;

public class GSSAccumulatorsStats implements StatsInterface {

	public GSSAccumulator meanTotalLoad;
	public GSSAccumulator meanQueueLength;
	public GSSAccumulator meanEnergyUsage;
	public GSSAccumulator meanAirFlow;
	public GSSAccumulator meanTemperature;

	public GSSAccumulator meanTaskStartTime;
	public GSSAccumulator meanTaskCompletionTime;
	public GSSAccumulator meanTaskExecutionTime;
	public GSSAccumulator meanTaskWaitingTime;
	public GSSAccumulator meanTaskFlowTime;
	public GSSAccumulator lateness;
	public GSSAccumulator tardiness;
	public GSSAccumulator delayedTasks;
	public GSSAccumulator failedRequests;
	public GSSAccumulator makespan;

	private String[] headers = { "Resource name", "mean", "stdev", "variance",
			"minimum", "maximum", "sum", "count" };

	public GSSAccumulatorsStats() {
		meanTotalLoad = new GSSAccumulator();
		meanQueueLength = new GSSAccumulator();
		meanEnergyUsage = new GSSAccumulator(); 
		meanAirFlow = new GSSAccumulator(); 
		meanTemperature = new GSSAccumulator(); 
		
		meanTaskStartTime = new GSSAccumulator();
		meanTaskCompletionTime = new GSSAccumulator();
		meanTaskExecutionTime = new GSSAccumulator();
		meanTaskWaitingTime = new GSSAccumulator();
		meanTaskFlowTime = new GSSAccumulator();
		lateness = new GSSAccumulator();
		tardiness = new GSSAccumulator();
		delayedTasks = new GSSAccumulator();
		failedRequests = new GSSAccumulator();
		makespan = new GSSAccumulator();
	}

	/*
	 * public static String getAcccumulatorHeader() {
	 * 
	 * String accumulatorSeparator = ";";
	 * 
	 * StringBuffer buffer = new StringBuffer(260);
	 * 
	 * buffer.append(header[0]); buffer.append(accumulatorSeparator);
	 * buffer.append(header[1]); buffer.append(accumulatorSeparator);
	 * buffer.append(header[2]); buffer.append(accumulatorSeparator);
	 * buffer.append(header[3]); buffer.append(accumulatorSeparator);
	 * buffer.append(header[4]); buffer.append(accumulatorSeparator);
	 * buffer.append(header[5]); buffer.append(accumulatorSeparator);
	 * buffer.append(header[6]); buffer.append(accumulatorSeparator);
	 * buffer.append(header[7]); buffer.append(accumulatorSeparator);
	 * buffer.append(header[8]); buffer.append(accumulatorSeparator);
	 * 
	 * return buffer.toString(); }
	 */

	public Object serialize(StatsSerializer serializer) {
		return serializer.visit(this);
	}

	public String[] getHeaders() {
		return headers;
	}
}
