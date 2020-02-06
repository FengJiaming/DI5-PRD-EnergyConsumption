package controller.simulator.stats.implementation;

import java.io.PrintStream;

import controller.simulator.stats.GSSAccumulator;
import controller.simulator.stats.implementation.out.StatsSerializer;


public class JobStats implements StatsInterface {

	protected String jobID;
	protected GSSAccumulator meanTaskCompletionTime;
	protected GSSAccumulator meanTaskExecutionTime;
	protected GSSAccumulator meanTaskStartTime;
	protected GSSAccumulator meanTaskFlowTime;
	protected GSSAccumulator meanTaskWaitingTime;
	protected GSSAccumulator meanTaskGQ_WaitingTime;
	protected GSSAccumulator lateness;
	protected GSSAccumulator tardiness;
	protected GSSAccumulator makespan;

	JobStats(String jobID) {
		this.jobID = jobID;
		init();
	}

	private String[] headers = { "jobID", "meanTaskCompletionTime",
			"meanTaskExecutionTime", "meanTaskStartTime", "meanTaskFlowTime",
			"meanTaskWaitingTime", "meanTaskGQ_WaitingTime", "lateness",
			"tardiness", "makespan" };

	private void init() {
		this.meanTaskCompletionTime = new GSSAccumulator();
		this.meanTaskExecutionTime = new GSSAccumulator();
		this.meanTaskStartTime = new GSSAccumulator();
		this.meanTaskFlowTime = new GSSAccumulator();
		this.meanTaskWaitingTime = new GSSAccumulator();
		this.meanTaskGQ_WaitingTime = new GSSAccumulator();
		this.lateness = new GSSAccumulator();
		this.tardiness = new GSSAccumulator();
		this.makespan = new GSSAccumulator();
	}

	public String getJobID() {
		return jobID;
	}

	public GSSAccumulator getMeanTaskCompletionTime() {
		return meanTaskCompletionTime;
	}

	public GSSAccumulator getMeanTaskExecutionTime() {
		return meanTaskExecutionTime;
	}

	public GSSAccumulator getMeanTaskStartTime() {
		return meanTaskStartTime;
	}

	public GSSAccumulator getMeanTaskFlowTime() {
		return meanTaskFlowTime;
	}

	public GSSAccumulator getMeanTaskWaitingTime() {
		return meanTaskWaitingTime;
	}

	public GSSAccumulator getMeanTaskGQ_WaitingTime() {
		return meanTaskGQ_WaitingTime;
	}

	public GSSAccumulator getLateness() {
		return lateness;
	}

	public GSSAccumulator getTardiness() {
		return tardiness;
	}

	public GSSAccumulator getMakespan() {
		return makespan;
	}

	public Object serialize(StatsSerializer serializer) {
		return serializer.visit(this);
	}

	public String[] getHeaders() {
		return headers;
	}
}