package controller.workload.generator.writer;

import java.io.IOException;
import java.util.Random;

import org.exolab.castor.types.Duration;


import org.qcg.broker.schemas.exception.NoSuchParamException;
import org.qcg.broker.schemas.jobdesc.ExecutionTimeType;
import org.qcg.broker.schemas.jobdesc.QcgJob;
import org.qcg.broker.schemas.jobdesc.Task;
import org.qcg.broker.schemas.jobdesc.wrapper.TaskRequirements;
import org.qcg.broker.schemas.jobdesc.wrapper.impl.TaskRequirementsImpl;

import controller.workload.SWFFields;
import controller.workload.generator.writer.AbstractSWFJobWriter;

public class QcgSWFJobWriter extends AbstractSWFJobWriter<QcgJob>{

	protected int intTaskId;
	protected String workloadArray[];
	protected Random random;
	
	public QcgSWFJobWriter(String dirName, String fileName) throws IOException{
		super(dirName, fileName);
		this.intTaskId = 0;
		this.workloadArray = new String[18];
		this.random = new Random(System.currentTimeMillis());
	}
	
	public boolean write(QcgJob job) throws IOException {
		long taskLength;
		String idMapping;
		String taskId;
		TaskRequirements taskReqWrapper = new TaskRequirementsImpl();
		
		for(int i = 0; i < job.getTaskCount(); i++){
			Task task = job.getTask(i);
			taskReqWrapper.wrap(task);
			
			// prepare swf task id
			if(this.useTaskMapping){
				taskId = String.valueOf(intTaskId);
				idMapping = taskId+":"+job.getAppId()+":"+task.getTaskId();
				intTaskId++;
				this.taskMapping.add(idMapping);
			} else {
				taskId = task.getTaskId();
			}
			
			// prepare task length value
			if(tasksLength.containsKey(task.getTaskId())){
				taskLength = tasksLength.get(task.getTaskId());
			} else {
				taskLength = 0;
			}
			
			double tab[] = null;
			
			// prepare requested number of processors
			String reqCpuCnt = IRRELEVANT;
			try {
				double min = 0;
				double max = Double.MAX_VALUE;
				
				if((tab = taskReqWrapper.getCpucount()).length != 0){ // try single value
					reqCpuCnt = String.valueOf(Math.round(tab[0]));
					
				} else {
					double d[][] = taskReqWrapper.getRangeCpucount(); 
					if(d.length != 0 && (tab = d[0]).length != 0){ // try range value
						min = tab[0];
						max = tab[1];
						double r = random.nextDouble();
						reqCpuCnt = String.valueOf(Math.round(min + (max - min) * r));
					
					} else {										// try single min and max value
						boolean change = false;
						if((tab = taskReqWrapper.getMinCpucount()).length != 0) {
							min = tab[0];
							change = true;
						}
						if((tab = taskReqWrapper.getMaxCpucount()).length != 0){
							max = tab[0];
							change = true;
						}
						if(change) {
							double r = random.nextDouble();
							reqCpuCnt = String.valueOf(Math.round(min + (max - min) * r));
						}
					}
				}
				
			} catch(NoSuchParamException e){

			}
			
			// prepare requested memory
			String reqMemCnt = IRRELEVANT;
			try {
				double min = 0;
				double max = Double.MAX_VALUE;
				
				if((tab = taskReqWrapper.getMemory()).length != 0){ // try single value
					reqMemCnt = String.valueOf(Math.round(tab[0]));
					
				} else {
					double d[][] = taskReqWrapper.getRangeMemory(); 
					if(d.length != 0 && (tab = d[0]).length != 0){ // try range value
						min = tab[0];
						max = tab[1];
						double r = random.nextDouble();
						reqMemCnt = String.valueOf(Math.round(min + (max - min) * r));
						
					} else {										// try single min and max value
						boolean change = false;
						if((tab = taskReqWrapper.getMinMemory()).length != 0) {
							min = tab[0];
							change = true;
						}
						if((tab = taskReqWrapper.getMaxMemory()).length != 0){
							max = tab[0];
							change = true;
						}
						if(change){
							double r = random.nextDouble();
							reqMemCnt = String.valueOf(Math.round(min + (max - min) * r));
						}
					}
				} 
				
			} catch(NoSuchParamException e){

			}
			
			// prepare requested time
			long reqTaskLength = taskLength;
			ExecutionTimeType ett = task.getExecutionTime();
			if(ett != null){
				Duration duration = ett.getExecutionDuration();
				reqTaskLength = duration.toLong();
			}
			
			
			workloadArray[SWFFields.DATA_JOB_NUMBER] = taskId;
			workloadArray[SWFFields.DATA_SUBMIT_TIME] = String.valueOf(this.submitTime);
			workloadArray[SWFFields.DATA_WAIT_TIME] = IRRELEVANT;
			workloadArray[SWFFields.DATA_RUN_TIME] = String.valueOf(taskLength);
			workloadArray[SWFFields.DATA_NUMBER_OF_ALLOCATED_PROCESSORS] = reqCpuCnt;
			workloadArray[SWFFields.DATA_AVERAGE_CPU_TIME_USED] = IRRELEVANT;
			workloadArray[SWFFields.DATA_USED_MEMORY] = IRRELEVANT;
			workloadArray[SWFFields.DATA_REQUESTED_NUMBER_OF_PROCESSORS] = reqCpuCnt;
			workloadArray[SWFFields.DATA_REQUESTED_TIME] = String.valueOf(reqTaskLength);
			workloadArray[SWFFields.DATA_REQUESTED_MEMORY] = reqMemCnt;
			workloadArray[SWFFields.DATA_STATUS] = IRRELEVANT;
			workloadArray[SWFFields.DATA_USER_ID] = IRRELEVANT;
			workloadArray[SWFFields.DATA_GROUP_ID] = IRRELEVANT;
			workloadArray[SWFFields.DATA_EXECUTABLE_NUMBER]  = IRRELEVANT;
			workloadArray[SWFFields.DATA_QUEUE_NUMBER] = IRRELEVANT;
			workloadArray[SWFFields.DATA_PARTITION_NUMBER] = IRRELEVANT;
			workloadArray[SWFFields.DATA_PRECEDING_JOB_NUMBER] = IRRELEVANT;
			workloadArray[SWFFields.DATA_THINK_TIME_FROM_PRECEDING_JOB] = IRRELEVANT;
			
			writeLine(workloadArray);
			
		}
		
		this.tasksLength.clear();
		
		return true;
	}
	
	public void addTaskMapping(String swfJobId, String xmlJobId, String xmlTaskId){
		if(!this.useTaskMapping)
			return;
		this.taskMapping.add(swfJobId+":"+xmlJobId+":"+xmlTaskId);
	}
}
