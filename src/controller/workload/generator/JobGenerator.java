package controller.workload.generator;

import java.io.FileNotFoundException;
import java.io.IOException;

import simulator.workload.generator.configuration.WorkloadConfiguration;

public interface JobGenerator {
	
	public void generateWorkload(WorkloadConfiguration workload, String outputDirectoryName, 
								boolean overwriteExistingFiles, String outputWorkloadFileName, Dependency dependencies) throws FileNotFoundException, IOException;
	
	public void generateXMLSupplement(String configurationFileName, String swfFileName, 
								String outputDirectoryName, boolean ignoreNonEmptyOutputDir) throws FileNotFoundException, IOException;
	
	public void performPostprocessing(String resDescriptionFileName, String outputDirectoryName, String outputWorkloadFileName);
	
}
