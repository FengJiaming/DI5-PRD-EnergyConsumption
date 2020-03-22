package controller.workload.generator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import app.ConfigurationOptions;
import controller.simulator.utils.LogErrStream;
import controller.workload.generator.impl.QcgJobGenerator;
import simulator.workload.generator.configuration.WorkloadConfiguration;


public class WorkloadGenerator {
	
	private Log log = LogFactory.getLog(WorkloadGenerator.class);
	
	public void run(ConfigurationOptions configurationOptions, WorkloadConfiguration workload, Dependency dependencies){
		log.info("Start workload generator.");
		System.out.println("Start workload generator.");
//		System.setErr(new PrintStream(new LogErrStream(log)));
		ConfigurationOptions co = configurationOptions;
		
		JobGenerator jobGenerator = new QcgJobGenerator();
		
		try {
			System.out.println("Generating workload.");
			log.info("Generating workload.");
			jobGenerator.generateWorkload(workload, co.outputFolder, true, co.outputWorkloadFileName, dependencies);
			
			log.info("Performing postprocessing.");
//			jobGenerator.performPostprocessing(co.resdescFileName, co.outputFolder, co.outputWorkloadFileName);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("End workload generator.");
		log.info("End workload generator.");
	}
}
