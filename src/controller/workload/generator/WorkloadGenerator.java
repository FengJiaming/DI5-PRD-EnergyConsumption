package controller.workload.generator;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import app.ConfigurationOptions;
import controller.workload.generator.impl.QcgJobGenerator;
import simulator.workload.generator.configuration.WorkloadConfiguration;


public class WorkloadGenerator {
	
	private Log log = LogFactory.getLog(WorkloadGenerator.class);
	
	public void run(ConfigurationOptions configurationOptions, WorkloadConfiguration workload){
		log.info("Start workload generator.");
		
		
		ConfigurationOptions co = configurationOptions;
		
		JobGenerator jobGenerator = new QcgJobGenerator();
		
		try {
			
			log.info("Generating workload.");
			jobGenerator.generateWorkload(workload, co.outputFolder, true, co.outputWorkloadFileName);
			
			log.info("Performing postprocessing.");
//			jobGenerator.performPostprocessing(co.resdescFileName, co.outputFolder, co.outputWorkloadFileName);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		log.info("End workload generator.");
	}
}
