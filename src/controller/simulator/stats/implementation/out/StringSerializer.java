package controller.simulator.stats.implementation.out;


import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import controller.simulator.DataCenterWorkloadSimulator;
import controller.simulator.stats.implementation.AccumulatedResourceStats;
import controller.simulator.stats.implementation.GSSAccumulatorsStats;
import controller.simulator.stats.implementation.JobStats;
import controller.simulator.stats.implementation.ResourcePowerStats;
import controller.simulator.stats.implementation.ResourceStats;
import controller.simulator.stats.implementation.ResourceUsageStats;
import controller.simulator.stats.implementation.TaskStats;


public class StringSerializer extends AbstractStringSerializer {

	private Log log = LogFactory.getLog(StringSerializer.class);

	public String visit(TaskStats taskStats) {
		StringBuffer buffer = null;

		if(printedHeaders.add("taskStats")) {
			buffer = new StringBuffer(520);
			String[] headers = taskStats.getHeaders();
			if (headers.length >= 10) {

				for(int i = 0; i < 10; i++)
				{
					buffer.append(headers[i]);
					buffer.append(fieldSeparator);
				}
				
				if (this.useExtended) {
					
					for(int i = 10; i < headers.length; i++)
					{
						buffer.append(headers[i]);
						buffer.append(fieldSeparator);
					}
					
				}

			} else {
				buffer.append(headers.toString());
			}
			buffer.append(System.getProperty("line.separator"));
		} else {
			buffer = new StringBuffer(260);
		}
		
		buffer.append(taskStats.getJobID());
		buffer.append(fieldSeparator);
		buffer.append(taskStats.getTaskID());
		buffer.append(fieldSeparator);
		buffer.append(taskStats.getCpuCnt());
		buffer.append(fieldSeparator);
		buffer.append(Double.valueOf(taskStats.getExecStartDate()).longValue());
		buffer.append(fieldSeparator);
		buffer.append(Double.valueOf(taskStats.getExecFinishDate()).longValue());
		buffer.append(fieldSeparator);
		buffer.append(Double.valueOf(taskStats.getExecEndDate()).longValue());
		buffer.append(fieldSeparator);
		if (this.useExtended) {
			buffer.append(Double.valueOf(taskStats.getCompletionTime())
					.longValue());
			buffer.append(fieldSeparator);
			buffer.append(Double.valueOf(taskStats.getExecStartTime()).longValue());
			buffer.append(fieldSeparator);
			buffer.append(Double.valueOf(taskStats.getExecutionTime()).longValue());
			buffer.append(fieldSeparator);
			buffer.append(Double.valueOf(taskStats.getReadyTime()).longValue());
			buffer.append(fieldSeparator);
			buffer.append(Double.valueOf(taskStats.getStartTime()).longValue());
			buffer.append(fieldSeparator);
			buffer.append(Double.valueOf(taskStats.getFlowTime()).longValue());
			buffer.append(fieldSeparator);
			buffer.append(Double.valueOf(taskStats.getWaitingTime()).longValue());
			buffer.append(fieldSeparator);
			buffer.append(taskStats.getProcessorsName());
			buffer.append(fieldSeparator);
		}
		
		
		return buffer.toString();
	}

	public String visit(JobStats jobStats) {
		
		StringBuffer buffer = null;

		if(printedHeaders.add("jobStats")) {
			buffer = new StringBuffer(200);
			String[] headers = jobStats.getHeaders();
			for(int i = 0; i < headers.length; i++)
			{
				buffer.append(headers[i]);
				buffer.append(fieldSeparator);
			}
			buffer.append(System.getProperty("line.separator"));
		} else {
			buffer = new StringBuffer(100);
		}
		
		buffer.append(jobStats.getJobID());
		buffer.append(fieldSeparator);
		buffer.append(defaultNumberFormat
				.format(jobStats.getMeanTaskCompletionTime().getMean()));
		buffer.append(fieldSeparator);
		buffer.append(defaultNumberFormat
				.format(jobStats.getMeanTaskExecutionTime().getMean()));
		buffer.append(fieldSeparator);
		buffer.append(defaultNumberFormat
				.format(jobStats.getMeanTaskStartTime().getMean()));
		buffer.append(fieldSeparator);
		buffer.append(defaultNumberFormat
				.format(jobStats.getMeanTaskFlowTime().getMean()));
		buffer.append(fieldSeparator);
		buffer.append(defaultNumberFormat
				.format(jobStats.getMeanTaskWaitingTime().getMean()));
		buffer.append(fieldSeparator);
		buffer.append(defaultNumberFormat
				.format(jobStats.getMeanTaskGQ_WaitingTime().getMean()));
		buffer.append(fieldSeparator);
		buffer.append(defaultNumberFormat
				.format(jobStats.getTardiness().getMean()));
		buffer.append(fieldSeparator);
		buffer.append(defaultNumberFormat
				.format(jobStats.getLateness().getMean()));
		buffer.append(fieldSeparator);
		buffer.append(defaultNumberFormat
				.format(jobStats.getMakespan().getMean()));
		buffer.append(fieldSeparator);
		
		return buffer.toString();
	}
	
	public String visit(ResourceStats resourceStats){
		
		StringBuffer buffer = null;

		if(printedHeaders.add("resourceStats")) {
			buffer = new StringBuffer(600);
			String[] headers = resourceStats.getHeaders();
			for(int i = 0; i < headers.length; i++)
			{
				buffer.append(headers[i]);
				buffer.append(fieldSeparator);
			}
			buffer.append(System.getProperty("line.separator"));
		} else {
			buffer = new StringBuffer(300);
		}
		
		buffer.append(resourceStats.getResourceName());
		buffer.append(fieldSeparator);

		buffer.append(resourceStats.getMemory());
		buffer.append(fieldSeparator);

		buffer.append(resourceStats.getCpucnt());
		buffer.append(fieldSeparator);

		buffer.append(resourceStats.getCpuspeed());
		buffer.append(fieldSeparator);

		buffer.append(DataCenterWorkloadSimulator.DFAULT_NUMBER_FORMAT
				.format(resourceStats.getQueueLength()));
		buffer.append(fieldSeparator);

		/*Map<String, Double> processorsLoad = resourceStats.getProcessorsLoad();
		for (String processorName : processorsLoad.keySet()) {

			Double load = processorsLoad.get(processorName);
			buffer.append(processorName);
			buffer.append(" ");
			buffer.append(GridSchedulingSimulator.DFAULT_NUMBER_FORMAT
							.format(load));
			buffer.append(":");

		}*/

		buffer.append(fieldSeparator);
		Map<String, Double> processorsReservationLoad = resourceStats
				.getProcessorsReservationLoad();
		for (String processorName : processorsReservationLoad.keySet()) {

			Double reservationLoad = processorsReservationLoad
					.get(processorName);
			buffer.append(processorName);
			buffer.append(" ");
			buffer.append(DataCenterWorkloadSimulator.DFAULT_NUMBER_FORMAT
					.format(reservationLoad));
			buffer.append(":");
		}
		buffer.append(fieldSeparator);
		return buffer.toString();
	}
	
	public String visit(AccumulatedResourceStats accResStats){
		
		StringBuffer buffer = null;

		if(printedHeaders.add("accResStats")) {
			buffer = new StringBuffer(340);
			String[] headers = accResStats.getHeaders();
			for(int i = 0; i < headers.length; i++)
			{
				buffer.append(headers[i]);
				buffer.append(fieldSeparator);
			}
			buffer.append(System.getProperty("line.separator"));
		} else {
			buffer = new StringBuffer(170);
		}
		
		buffer.append(accResStats.getResourceName());
		buffer.append(fieldSeparator);
		buffer.append("load");
		buffer.append(accResStats.getResourceLoad().toString());
		buffer.append(System.getProperty("line.separator"));
		buffer.append(accResStats.getResourceName());
		buffer.append(fieldSeparator);
		buffer.append("reservationload");
		buffer.append(accResStats.getResourceReservationLoad().toString());
		buffer.append(System.getProperty("line.separator"));
		
		return buffer.toString();
	}

	public Object visit(ResourceUsageStats resourceUsageStats) {
		Map<Long, Double> resourceUsage = resourceUsageStats.getHistory();
		
		int mapSize = resourceUsage.size();
		/*
		 * FIXME:
		 * Integer.MAX_VALUE = 2147483647. We assume, that each line contains 
		 * max 30 signs - this gives max 71582788 lines. If resourceUsage map
		 * contains more elements then we have a problem, because content of
		 * resourceUsage map will not fit in the buffer.
		 * This will need further attention in the future.
		 */
		int maxSize = (Integer.MAX_VALUE / 30 ) - 1;
		if(mapSize >= maxSize){
			log.error("Resource usage data is to long to fit in the buffer.");
			return null;
		}
		
		int size = 30 * resourceUsage.size();
		
		StringBuffer buffer = null;

		if(printedHeaders.add(resourceUsageStats.getUsageType())) {
			buffer = new StringBuffer(size + 42);
			String[] headers = resourceUsageStats.getHeaders();
			for(int i = 0; i < headers.length; i++)
			{
				buffer.append(headers[i]);
				buffer.append(fieldSeparator);
			}
			buffer.append(System.getProperty("line.separator"));
		} else {
			buffer = new StringBuffer(size);
		}
		
		
		for (Long timestamp : resourceUsage.keySet()) {
			
			buffer.append(resourceUsageStats.getResourceName());
			buffer.append(fieldSeparator);
			Double value = resourceUsage.get(timestamp);
			buffer.append(timestamp);
			buffer.append(fieldSeparator);
			buffer.append(DataCenterWorkloadSimulator.DFAULT_NUMBER_FORMAT
					.format(value));
			buffer.append(fieldSeparator);
			buffer.append(System.getProperty("line.separator"));

		}
		if(resourceUsage.size() > 0){
			buffer.append("mean: " + resourceUsageStats.getMeanValue());
			buffer.append(System.getProperty("line.separator"));	
		}
		return buffer.toString();
	}

	public Object visit(ResourcePowerStats resourceEnergyStats) {
		Map<Long, Double> resourceEnergy = resourceEnergyStats.getHistory();
		
		int mapSize = resourceEnergy.size();
		/*
		 * FIXME:
		 * Integer.MAX_VALUE = 2147483647. We assume, that each line contains 
		 * max 30 signs - this gives max 71582788 lines. If resourceUsage map
		 * contains more elements then we have a problem, because content of
		 * resourceUsage map will not fit in the buffer.
		 * This will need further attention in the future.
		 */
		int maxSize = (Integer.MAX_VALUE / 30 ) - 1;
		if(mapSize >= maxSize){
			log.error("Resource usage data is to long to fit in the buffer.");
			return null;
		}
		
		int size = 30 * resourceEnergy.size();
		
		StringBuffer buffer = null;

		if(printedHeaders.add(resourceEnergyStats.getUsageType())) {
			buffer = new StringBuffer(size + 42);
			String[] headers = resourceEnergyStats.getHeaders();
			for(int i = 0; i < headers.length; i++)
			{
				buffer.append(headers[i]);
				buffer.append(fieldSeparator);
			}
			buffer.append(System.getProperty("line.separator"));
		} else {
			buffer = new StringBuffer(size);
		}
		
		
		for (Long timestamp : resourceEnergy.keySet()) {
			
			buffer.append(resourceEnergyStats.getResourceName());
			buffer.append(fieldSeparator);
			Double value = resourceEnergy.get(timestamp);
			buffer.append(timestamp);
			buffer.append(fieldSeparator);
			buffer.append(DataCenterWorkloadSimulator.DFAULT_NUMBER_FORMAT
					.format(value));
			buffer.append(fieldSeparator);
			buffer.append(System.getProperty("line.separator"));

		}
		
		if(resourceEnergy.size() > 0) {
			buffer.append("mean: "+resourceEnergyStats.getMeanValue() + " sum: " +resourceEnergyStats.getSumValue());
			buffer.append(System.getProperty("line.separator"));	
		}

		return buffer.toString();
	}

	


	public Object visit(GSSAccumulatorsStats accStats) {

		StringBuffer buffer = null;

		if(printedHeaders.add("accStats")) {
			buffer = new StringBuffer(2800);
			String[] headers = accStats.getHeaders();
			for(int i = 0; i < headers.length; i++)
			{
				buffer.append(headers[i]);
				buffer.append(fieldSeparator);
			}
			buffer.append(System.getProperty("line.separator"));
		} else {
			buffer = new StringBuffer(1400);
		}
		
		buffer.append("Delayed tasks");
		buffer.append(accStats.delayedTasks);
		buffer.append(System.getProperty("line.separator"));
		buffer.append("Failed requests (tasks)");
		buffer.append(accStats.failedRequests);
		buffer.append(System.getProperty("line.separator"));
		buffer.append("Makespan");
		buffer.append(accStats.makespan);
		buffer.append(System.getProperty("line.separator"));
		buffer.append("Resources queue length");
		buffer.append(accStats.meanQueueLength);
		buffer.append(System.getProperty("line.separator"));
		buffer.append("Resources total load");
		buffer.append(accStats.meanTotalLoad);
		buffer.append(System.getProperty("line.separator"));
		buffer.append("Energy usage");
		buffer.append(accStats.meanEnergyUsage);
		buffer.append(System.getProperty("line.separator"));
		buffer.append("Task completion time");
		buffer.append(accStats.meanTaskCompletionTime);
		buffer.append(System.getProperty("line.separator"));
		buffer.append("Task execution time");
		buffer.append(accStats.meanTaskExecutionTime);
		buffer.append(System.getProperty("line.separator"));
		buffer.append("Task start time");
		buffer.append(accStats.meanTaskStartTime);
		buffer.append(System.getProperty("line.separator"));
		buffer.append("Task flow time");
		buffer.append(accStats.meanTaskFlowTime);
		buffer.append(System.getProperty("line.separator"));
		buffer.append("Task waiting time");
		buffer.append(accStats.meanTaskWaitingTime);
		buffer.append(System.getProperty("line.separator"));
		buffer.append("Task lateness");
		buffer.append(accStats.lateness);
		buffer.append(System.getProperty("line.separator"));
		buffer.append("Task tardiness");
		buffer.append(accStats.tardiness);
		
		return buffer.toString();
	}

}
