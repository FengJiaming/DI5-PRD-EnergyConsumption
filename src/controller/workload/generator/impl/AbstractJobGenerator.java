package controller.workload.generator.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.types.Duration;

import controller.simulator.utils.RandomNumbers;
import controller.workload.generator.RandomNumbersWrapper;
import controller.workload.generator.factory.JobRandomNubersFactory;
import controller.workload.generator.fileFilters.QcgJobFileNameFilter;
import controller.workload.generator.fileFilters.SWFFileNameFilter;
import simulator.workload.generator.configuration.JobCount;
import simulator.workload.generator.configuration.MultiDistribution;
import simulator.workload.generator.configuration.RandParams;
import simulator.workload.generator.configuration.RandParamsSequence;
import simulator.workload.generator.configuration.WorkloadConfiguration;


public abstract class AbstractJobGenerator {

	private Log log = LogFactory.getLog(AbstractJobGenerator.class);

	/**the description of the tasks that are to be generated*/
	protected WorkloadConfiguration workload;
	
	/**the number of tasks generated - one of the proceed conditions*/
	protected int generatedJobCount;
	protected int generatedTaskCount;
	protected int taskCountToBeGenerated;
	
	/**number of jobs to be generated*/
	protected int jobCountToBeGenerated;
	
	/**the virtual (logical) time of the simulation - one of the proceed conditions*/ 
	protected long simulationVirtualTime;
	
	protected JobCount jobCount;
	protected Duration simulationDuration;
	protected Date simulationEndTime;

	/**a random number generator objects*/
	//the values set in this "random numbers" are not copied to the TaskGridlet object
	protected RandomNumbers parameterRandomNumbers; 
	protected RandomNumbers hardConstraintsRandomNumbers;
	protected RandomNumbers softConstraintsRandomNumbers;
	protected RandomNumbers timeConstraintsRandomNumbers;
	protected RandomNumbers precedingConstraintsRandomNumbers;
	protected RandomNumbersWrapper randomNumbersWrapper;
	
	protected Random detailDistIntervalChooser;
	
	protected JobRandomNubersFactory randomNumbersFactory;
	

	/**
	 * 
	 */
	public AbstractJobGenerator(){
		generatedJobCount = 0;
		generatedTaskCount = 0;
		taskCountToBeGenerated = 0;
		randomNumbersWrapper = new RandomNumbersWrapper();
		detailDistIntervalChooser = new Random(System.currentTimeMillis());
	}
	
	/**
	 * 
	 * @param outputDirectoryName
	 * @param overwriteExistingFiles
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected File prepareWorkloadDirectory(String outputDirectoryName, boolean overwriteExistingFiles) 
	throws FileNotFoundException, IOException{
		File outputDirectory = new File (outputDirectoryName);
		
		if (!outputDirectory.exists()) {
			if(!outputDirectory.mkdir()){
				throw new FileNotFoundException("Output directory "+outputDirectoryName+" does not exist and can not be created.");
			}
		}
		
		if (!outputDirectory.isDirectory()) {
			throw new IOException("Output directory "+outputDirectoryName+" is not a directory");
		}
		
		if (outputDirectory.list().length > 0) {//the directory is not empty
			if (!overwriteExistingFiles)
				throw new IOException("The outputdirectory "+ outputDirectoryName +" is not empty.");
			
			File[] files = null;
			
			files = outputDirectory.listFiles(new QcgJobFileNameFilter());
			for (int i = 0; i < files.length; i++) {
				File file = files[i];					
				if(!file.delete()){
					if(log.isErrorEnabled())
						log.error("File "+file.getPath() + " can not be deleted.");
				}
			}
			
			files = outputDirectory.listFiles(new SWFFileNameFilter());
			for (int i = 0; i < files.length; i++) {
				File file = files[i];					
				if(!file.delete()){
					if(log.isErrorEnabled())
						log.error("File "+file.getPath() + " can not be deleted.");
				}
			}
			
		}
		
		return outputDirectory;
	}
	

	/**
	 * Initializes the randomNumbers field with appropriate random number generators. 
	 * @param workload 
	 */
	protected void init(WorkloadConfiguration workload) {
		
		randomNumbersFactory = new JobRandomNubersFactory();
		
		
		parameterRandomNumbers = 
					randomNumbersFactory.initParameterRandomNumbers(workload);
		randomNumbersWrapper.setParameterRandomNumbers(parameterRandomNumbers);

		/*
		 * Setting the hard constraints values
		 */		

		hardConstraintsRandomNumbers = 
					randomNumbersFactory.initHardConstraintsRandomNumbers(workload);
		randomNumbersWrapper.setHardConstraintsRandomNumbers(hardConstraintsRandomNumbers);
		/*
		 * Setting the soft constraints values
		 */
		softConstraintsRandomNumbers = 
					randomNumbersFactory.initSoftConstraintsRandomNumbers(workload);
		randomNumbersWrapper.setSoftConstraintsRandomNumbers(softConstraintsRandomNumbers);
		/*
		 * Setting time constraints values
		 */
		timeConstraintsRandomNumbers = 
					randomNumbersFactory.initTimeConstraintsRandomNumbers(workload);
		randomNumbersWrapper.setTimeConstraintsRandomNumbers(timeConstraintsRandomNumbers);
		/*
		 * Setting preceding constraints values
		 */
		precedingConstraintsRandomNumbers = 
					randomNumbersFactory.initPrecedingConstraintsRandomNumbers(workload);
		randomNumbersWrapper.setPrecedingConstraintsRandomNumbers(precedingConstraintsRandomNumbers);
	}
	
	
	/**
	 * 
	 * @return true if this entity should continue execution, false, if 
	 * this entity should stop
	 */
	protected boolean proceedConditionsAreFulfilled() {
		if (jobCount != null) {
			if (jobCountToBeGenerated < generatedJobCount) {
				return false; //cannot continue
			} 
				
			return true; //can continue

		} else if (simulationDuration != null) {
			if (simulationVirtualTime >= simulationEndTime.getTime() /* simulationDuration.toLong()*/) {
				return false; //cannot continue
			} 
			return true; //can continue
			
		} else { //if specification changes, one may add new conditions here
			return false; 
		}
	}
	
	protected double getRandomValueFor(RandParams randParams, RandomNumbers randNumbers) throws IllegalAccessException{
		return getRandomValueFor(randParams, JobRandomNubersFactory.getGeneratorName(randParams), randNumbers);
	}
	
	private double getRandomValueFor(RandParams randParams, String name, RandomNumbers randNumbers) throws IllegalAccessException{
		MultiDistribution md = null;
		RandParamsSequence rps = null;
		
		if(randParams == null)
			throw new IllegalAccessException("Specify RandParams object.");
		if(randNumbers == null)
			throw new IllegalAccessException("Specify random numbers map.");
		
		if(randParams.getRefElementId() != null){
			return randomNumbersWrapper.evalValueFor(randParams.getId(), randParams.getRefElementId());
		}
		
		md = randParams.getMultiDistribution();
		rps = randParams.getRandParamsSequence();
		if(md == null && rps == null){
			return randNumbers.getRandomValue(name);
		}
		
		if(md != null && rps == null){
			return randNumbers.getRandomValue(name, detailDistIntervalChooser.nextDouble());
		}
		
		if(md == null && rps != null){
			//return randNumbers.getRandomValue(name, workload.getSimulationStartTime().getTime() + (simulationVirtualTime*1000));
			return randNumbers.getRandomValue(name, simulationVirtualTime);
		}
		
		throw new IllegalAccessException("Use direct generator description, " +
				"reference to another element, multidistribution or periodic valid values for element "+name);
	}
	
}
