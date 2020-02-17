package controller.workload.generator.writer;

import java.io.IOException;


public interface SWFJobWriter<Job> extends JobWriter<Job>{

	public void writeFieldDescriptionHeader() throws IOException;
	
	public void writeComment(String key, String value) throws IOException;
	
	public void useTaskMapping(boolean use);
	
	public void setJobSubmitTime(long submitTime);
	
	public void addTaskLength(String taskId, long taskLength);
	
}
