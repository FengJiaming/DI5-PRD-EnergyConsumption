package controller.workload.generator.writer;

import java.io.IOException;

public interface JobWriter<Job> {

	public boolean write(Job job) throws IOException;
	
	public void setOverwrite(boolean overwrite);
	
	public void close() throws IOException;
}
