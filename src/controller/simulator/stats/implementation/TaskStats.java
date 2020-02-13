package controller.simulator.stats.implementation;


import java.util.List;

import controller.simulator.stats.implementation.out.StatsSerializer;
import gridsim.schedframe.Executable;


public class TaskStats implements StatsInterface {
	protected List<String> processorsName;
	protected double reservationStartDate;
	protected double reservationFinishDate;

	protected Executable task;
	protected long startSimulationTimeSek;
	
	private String headers[] = { "JobID", "TaskID", "CpuCnt",
			"ExecStartDate", "ExecFinishDate", "ExecEndDate", "CompletionTime", "ExecStartTime",
			"ExecutionTime", "ReadyTime", "StartTime", "FlowTime",
			"WaitingTime", "ProcessorName"};

	public TaskStats(Executable task, long startSimulationTime) {
		this.task = task;
		this.startSimulationTimeSek = startSimulationTime / 1000;
	}

	public Object serialize(StatsSerializer serializer) {
		return serializer.visit(this);
	}

	public String getJobID() {
		return this.task.getJobId();
	}

	public String getTaskID() {
		return this.task.getId();
	}

	public String getUserDN() {
		return this.task.getUserDN();
	}

	public String getResName() {
		String resName = this.task.getSchedulerName();

		return resName;
	}

	public long getCpuCnt() {
		long cpuCnt = -1;
		try {
			cpuCnt = Double.valueOf(task.getCpuCntRequest()).longValue();
		} catch (NoSuchFieldException e) {
		}

		return cpuCnt;
	}

	public double getExecStartDate() {
		return task.getExecStartTime();
	}

	public double getExecFinishDate() {
		return task.getFinishTime();
	}

	public double getExecEndDate() {
		double ret = -1;
		try {
			ret = task.getExecutionEndTime().getMillis() / 1000;
		} catch (NoSuchFieldException e) {
			ret = task.getFinishTime();
		}
		return ret;
	}

	public double getGB_SubDate() {
		return task.getSubmissionTimeToBroker().getMillis() / 1000;
	}

	public double getLB_SubDate() {
		return task.getSubmissionTime();
	}

	public double getCompletionTime() {
		return task.getFinishTime() - startSimulationTimeSek;
	}

	public double getExecutionTime() {
		return task.getFinishTime() - task.getExecStartTime();
	}

	public double getExecStartTime() {
		return task.getExecStartTime() - startSimulationTimeSek;
	}

	public double getReadyTime() {
		double readyTime = 0;

		try {
			readyTime = task.getExecutionStartTime().getMillis() / 1000
					- startSimulationTimeSek;
		} catch (Exception ex) {
			readyTime = task.getSubmissionTimeToBroker().getMillis()/1000 - startSimulationTimeSek;
		}

		return readyTime;
	}

	public double getStartTime() {
		return task.getExecStartTime()
				- task.getSubmissionTimeToBroker().getMillis() / 1000;
	}

	public double getFlowTime() {
		return getCompletionTime() - getReadyTime();
	}

	public double getWaitingTime() {
		return task.getWaitingTime();
	}

	public double getGQ_WaitingTime() {
		return task.getSubmissionTime()
				- task.getSubmissionTimeToBroker().getMillis() / 1000;
	}

	public double getLateness() {
		double execEndDate = getExecEndDate();
		double lateness = 0;
		if (execEndDate != -1) {
			lateness = task.getFinishTime() - execEndDate;
		}
		return lateness;
	}

	public double getTardiness() {
		return Math.max(getLateness(), 0.0);
	}

	public void setProcessorsName(List<String> value) {
		this.processorsName = value;
	}

	public void setReservationStartDate(double value) {
		this.reservationStartDate = value;
	}

	public void setReservationFinishDate(double value) {
		this.reservationFinishDate = value;
	}

	public List<String> getProcessorsName() {
		return processorsName;
	}

	public double getReservationStartDate() {
		return reservationStartDate;
	}

	public double getReservationFinishDate() {
		return reservationFinishDate;
	}

	public String[] getHeaders() {
		return headers;
	}

}
