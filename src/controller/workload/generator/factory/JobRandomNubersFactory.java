package controller.workload.generator.factory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import cern.jet.random.AbstractDistribution;
import controller.simulator.utils.RandomNumbers;
import eduni.simjava.distributions.PeriodicGenerator;

import simulator.workload.generator.configuration.ComputingResourceHostParameter;
import simulator.workload.generator.configuration.Dist;
import simulator.workload.generator.configuration.ExecDuration;
import simulator.workload.generator.configuration.ExecutionTime;
import simulator.workload.generator.configuration.ExecutionTimeChoice;
import simulator.workload.generator.configuration.Importance;
import simulator.workload.generator.configuration.JobCount;
import simulator.workload.generator.configuration.JobInterval;
import simulator.workload.generator.configuration.JobPackageLength;
import simulator.workload.generator.configuration.Max;
import simulator.workload.generator.configuration.Min;
import simulator.workload.generator.configuration.MultiDistribution;
import simulator.workload.generator.configuration.OutputFileSize;
import simulator.workload.generator.configuration.Parameter;
import simulator.workload.generator.configuration.ParentCard;
import simulator.workload.generator.configuration.ParentStates_OR;
import simulator.workload.generator.configuration.PeriodDuration;
import simulator.workload.generator.configuration.PeriodEnd;
import simulator.workload.generator.configuration.PeriodStart;
import simulator.workload.generator.configuration.PeriodicValidValues;
import simulator.workload.generator.configuration.PrecedingConstraints;
import simulator.workload.generator.configuration.Preferences;
import simulator.workload.generator.configuration.RandParams;
import simulator.workload.generator.configuration.RandParamsSequence;
import simulator.workload.generator.configuration.Range;
import simulator.workload.generator.configuration.RunSameHost;
import simulator.workload.generator.configuration.TaskCount;
import simulator.workload.generator.configuration.TaskLength;
import simulator.workload.generator.configuration.Trigger;
import simulator.workload.generator.configuration.Triggers;
import simulator.workload.generator.configuration.Value;
import simulator.workload.generator.configuration.WorkflowLevel;
import simulator.workload.generator.configuration.WorkloadConfiguration;


public class JobRandomNubersFactory {

	protected String triggerNames[];
	
	private static Log log = LogFactoryImpl.getLog(JobRandomNubersFactory.class);
	
	/**
	 * 
	 * @param workload
	 * @param jobCountDescriptor
	 * @param taskCountDescriptor
	 * @param taskLengthDescriptor
	 * @param tasksDistanceDescriptor
	 * @param tasksPackageLengthDescriptor
	 * @param taskWaitTimeDescriptor
	 * @return
	 */
	public RandomNumbers initParameterRandomNumbers(
							WorkloadConfiguration workload){
		
		RandomNumbers parameterRandomNumbers = new RandomNumbers();
		String name = null;

		if (workload.getWorkloadConfigurationChoice().getJobCount() != null) {
			JobCount jobCount = workload.getWorkloadConfigurationChoice().getJobCount();
			name = getGeneratorName(jobCount);
			if(log.isDebugEnabled()){
				log.debug("Creating genrator for JobCount. Generator id: " + name);
			}
			addRandomNumbers(parameterRandomNumbers, jobCount, name);
		}

		TaskCount taskCount = workload.getTaskCount();
		name = getGeneratorName(taskCount);
		if(log.isDebugEnabled()){
			log.debug("Creating genrator for TaskCount range.min. Generator id: " + name);
		}
		addRandomNumbers(parameterRandomNumbers, taskCount, name);
		
		TaskLength taskLength = workload.getTaskLength();
		name  = getGeneratorName(taskLength);
		if(log.isDebugEnabled()){
			log.debug("Creating genrator for TaskLength range.min. Generator id: " + name);
		}
		addRandomNumbers(parameterRandomNumbers, taskLength, name);

		JobInterval jobInterval = workload.getJobInterval();
		name = getGeneratorName(jobInterval);
		if(log.isDebugEnabled()){
			log.debug("Creating genrator for JobInterval range.min. Generator id: " + name);
		}
		addRandomNumbers(parameterRandomNumbers, jobInterval, name);

		JobPackageLength jobPackageLength = workload.getJobPackageLength();
		name = getGeneratorName(jobPackageLength);
		if(log.isDebugEnabled()){
			log.debug("Creating genrator for JobPackageLength range.min. Generator id: " + name);
		}
		addRandomNumbers(parameterRandomNumbers, jobPackageLength, name);

		return parameterRandomNumbers; 
	}
	
	/**
	 * 
	 * @param workload
	 * @param maxDescriptor
	 * @param minDescriptor
	 * @param valueDescriptor
	 * @return
	 */
	public RandomNumbers initHardConstraintsRandomNumbers(WorkloadConfiguration workload){
		
		RandomNumbers hardConstraintsRandomNumbers = new RandomNumbers();
		
		ComputingResourceHostParameter[] hardConstraints = workload.getComputingResourceHostParameter();
		if (hardConstraints != null && hardConstraints.length > 0) {
			for (int i = 0; i < hardConstraints.length; ++i) {
				ComputingResourceHostParameter hardConstraint = hardConstraints[i];
				String name = hardConstraint.getMetric();

				Range[] ranges = hardConstraint.getRange();
				for (int r = 0; r < ranges.length; ++r) {
					Range range = ranges[r];
					Max max = range.getMax();
					if (max != null) {
						String maxName = getGeneratorName(max);
						if(log.isDebugEnabled()){
							log.debug("Creating genrator for " + name + " " + r + " range.min. Generator id: " + maxName);
						}
						addRandomNumbers(hardConstraintsRandomNumbers, max, maxName);
					}
					Min min = range.getMin();
					if (min != null) {
						String minName = getGeneratorName(min);
						if(log.isDebugEnabled()){
							log.debug("Creating genrator for " + name + " " + r + " range.min. Generator id: " + minName);
						}
						addRandomNumbers(hardConstraintsRandomNumbers, min, minName);
					}
				}

				Value[] values = hardConstraint.getValue();
				for (int v = 0; v < values.length; ++v) {
					Value value = values[v];
					String valueName = getGeneratorName(value);
					if(log.isDebugEnabled()){
						log.debug("Creating genrator for " + name + " " + v + " value. Generator id: "+valueName);
					}
					addRandomNumbers(hardConstraintsRandomNumbers, value, valueName);
				}			
			}
		}
		
		return hardConstraintsRandomNumbers;
	}
	
	/**
	 * 
	 * @param workload
	 * @param indiffThresholdDescriptor
	 * @param importanceDescriptor
	 * @return
	 */
	public RandomNumbers initSoftConstraintsRandomNumbers(WorkloadConfiguration workload){
		
		RandomNumbers softConstraintsRandomNumbers = new RandomNumbers();
		String name;
		String detailName;
		Preferences softConstraints = workload.getPreferences();
		if (softConstraints != null) {
			Parameter[] constraints = softConstraints.getParameter();
			for (int i = 0; i < constraints.length; ++i) {
				Parameter constraint = constraints[i];
				//the name is a big concatenation
				name = constraint.getName();
				
				Importance importance = constraint.getImportance();
				detailName = getGeneratorName(importance);
				
				if(log.isDebugEnabled()){
					log.debug("Creating genrator for parameter " + name + " importance. Generator id: " + detailName);
				}
				addRandomNumbers(softConstraintsRandomNumbers, importance, detailName);
				
				Range rangeTab[] = constraint.getRange();
				if(rangeTab != null && rangeTab.length > 0){
					Range range = rangeTab[0];
					Min min = range.getMin();
					detailName = getGeneratorName(min);
					if(log.isDebugEnabled()){
						log.debug("Creating genrator for parameter " + name + " range.min. Generator id: " + detailName);
					}
					addRandomNumbers(softConstraintsRandomNumbers, min, detailName);
					
					Max max = range.getMax();
					detailName = getGeneratorName(max);
					if(log.isDebugEnabled()){
						log.debug("Creating genrator for parameter " + name + " range.max. Generator id: " + detailName);
					}
					
					addRandomNumbers(softConstraintsRandomNumbers, max, detailName);
				}
				
				Value value = constraint.getValue();
				if(value != null){
					detailName = getGeneratorName(value);
					if(log.isDebugEnabled()){
						log.debug("Creating genrator for parameter " + name + " value. Generator id: " + detailName);
					}
					
					addRandomNumbers(softConstraintsRandomNumbers, value, detailName);
				}
			}
		}

		return softConstraintsRandomNumbers;
	}
	
	
	/**
	 * 
	 * @param workload
	 * @return
	 */
	public RandomNumbers initTimeConstraintsRandomNumbers(WorkloadConfiguration workload){
		RandomNumbers timeConstraintsRandomNumbers = new RandomNumbers();
		ExecutionTime timeConstraints = workload.getExecutionTime();
		
		if(timeConstraints != null) {
			String name = null;
			PeriodStart periodStart = timeConstraints.getPeriodStart();
			if(periodStart != null){
				name = getGeneratorName(periodStart);
				if(log.isDebugEnabled()){
					log.debug("Creating genrator for parameter ExecutionTime.PeriodStart Generator id: " + name);
				}
				addRandomNumbers(timeConstraintsRandomNumbers, periodStart, name);
			}
			
			ExecDuration execDuration = timeConstraints.getExecDuration();
			if(execDuration != null){
				name = getGeneratorName(execDuration);
				if(log.isDebugEnabled()){
					log.debug("Creating genrator for parameter ExecutionTime.ExecutionDuration Generator id: " + name);
				}
				addRandomNumbers(timeConstraintsRandomNumbers, execDuration, name);
			}
			
			ExecutionTimeChoice choice = timeConstraints.getExecutionTimeChoice();
			if(choice != null) {
		
				PeriodDuration periodDuration = choice.getPeriodDuration();
				if(periodDuration != null){
					name = getGeneratorName(periodDuration);
					if(log.isDebugEnabled()){
						log.debug("Creating genrator for parameter ExecutionTime.PeriodDuration Generator id: " + name);
					}
					addRandomNumbers(timeConstraintsRandomNumbers, periodDuration, name);
				}
				
				PeriodEnd periodEnd = choice.getPeriodEnd();
				if(periodEnd != null) {
					name = getGeneratorName(periodEnd);
					if(log.isDebugEnabled()){
						log.debug("Creating genrator for parameter ExecutionTime.PeriodEnd Generator id: " + name);
					}
					addRandomNumbers(timeConstraintsRandomNumbers, periodEnd, name);
				}
			}
		}
		return timeConstraintsRandomNumbers;
	}
	
	
	/**
	 * 
	 * @param workload
	 * @return
	 */
	public RandomNumbers initPrecedingConstraintsRandomNumbers(WorkloadConfiguration workload){
		RandomNumbers precedingConstraintsRandomNumbers = new RandomNumbers();
		PrecedingConstraints constraints = workload.getPrecedingConstraints();
		
		if(constraints == null) 
			return precedingConstraintsRandomNumbers;
		
		ParentCard parentCard = constraints.getParentCard();
		String name = null;
		
		if(parentCard != null){
			name = getGeneratorName(parentCard);
			if(log.isDebugEnabled()){
				log.debug("Creating genrator for PrecedingConstraints.ParentCard Generator id: " + name);
			}
			addRandomNumbers(precedingConstraintsRandomNumbers, parentCard, name);
		}
		
		WorkflowLevel workflowLevel = constraints.getWorkflowLevel();
		if(workflowLevel != null){
			name = getGeneratorName(workflowLevel);
			if(log.isDebugEnabled()){
				log.debug("Creating genrator for PrecedingConstraints.WorkflowLevel Generator id: " + name);
			}
			addRandomNumbers(precedingConstraintsRandomNumbers, workflowLevel, name);
		}
		
		OutputFileSize outputFileSize = constraints.getOutputFileSize();
		if(outputFileSize != null){
			name = getGeneratorName(outputFileSize);
			if(log.isDebugEnabled()){
				log.debug("Creating genrator for PrecedingConstraints.OutputFileSize Generator id: " + name);
			}
			addRandomNumbers(precedingConstraintsRandomNumbers, outputFileSize, name);
		}
		
		ParentStates_OR parentStates = constraints.getParentStates_OR();
		if(parentStates != null){
			name = getGeneratorName(parentStates);
			if(log.isDebugEnabled()){
				log.debug("Creating genrator for PrecedingConstraints.ParentStates_OR Generator id: " + name);
			}
			addRandomNumbers(precedingConstraintsRandomNumbers, parentStates, name);
		}
		
		RunSameHost runSameHost = constraints.getRunSameHost();
		if(runSameHost != null){
			name = getGeneratorName(runSameHost);
			if(log.isDebugEnabled()){
				log.debug("Creating genrator for PrecedingConstraints.RunSameHost Generator id: " + name);
			}
			addRandomNumbers(precedingConstraintsRandomNumbers, runSameHost, name);
		}
		
		Trigger trigger;
		Triggers triggers = constraints.getTriggers();
		String tname;
		if(triggers != null){
			triggerNames = new String[triggers.getTriggerCount()];
			
			for(int i = 0; i < triggers.getTriggerCount(); i++){
				trigger = triggers.getTrigger(i);
				triggerNames[i] = (String)trigger.getName();
				tname = getGeneratorName(trigger);
				if(log.isDebugEnabled()){
					log.debug("Creating genrator for PrecedingConstraints.Trigger " + triggerNames[i] + " Generator id: " + tname);
				}
				addRandomNumbers(precedingConstraintsRandomNumbers, trigger, tname);
			}
		}
		
		return precedingConstraintsRandomNumbers;
	}
	
	protected RandomNumbers addPeriodicValidValues(RandomNumbers randomNumbers, RandParams params, String name){
		PeriodicValidValues values[];
		int len = 0;
		
		if(params.getRandParamsSequence() == null)
			return randomNumbers;
		
		len = params.getRandParamsSequence().getPeriodicValidValuesCount();
		if(len == 0)
			return randomNumbers;
		
		values = params.getRandParamsSequence().getPeriodicValidValues();
		for(int i = 0; i < values.length; i++){
			if(randomNumbers.addRandomGenerator(name, values[i])){
				if(values[i].getId() != null){
					randomNumbers.addIdGeneratorNameMapping(values[i].getId(), name);
				}
			}
		}
		return randomNumbers;
	}
	
	protected RandomNumbers addDetailDistributions(RandomNumbers randomNumbers, RandParams params, String name){
		MultiDistribution md = null;
		Dist distTable[] = null;
		
		md = params.getMultiDistribution();
		if(md == null)
			return randomNumbers;
		
		distTable = md.getDist();
		for(int i = 0; i < distTable.length; i++){
			if(randomNumbers.addRandomGenerator(name, distTable[i])){
				if(distTable[i].getId() != null)
					randomNumbers.addIdGeneratorNameMapping(distTable[i].getId(), name);
			}
		}
		return randomNumbers;
	}
	
	public boolean addRandomNumbers(RandomNumbers rn, RandParams rp, String name) {
		MultiDistribution md = null;
		RandParamsSequence randParamSeq = null;
		AbstractDistribution generator;
		
		md = rp.getMultiDistribution();
		randParamSeq = rp.getRandParamsSequence();
		
		// if simple generator definition is used
		if(md == null && randParamSeq == null){
		
			if(rn.addRandomGenerator(rp.getDistribution().getType(), rp.hasAvg(), rp.getAvg(), rp.getStdev(), 
								rp.hasMin(), rp.getMin(), rp.hasMax(), rp.getMax(), rp.hasSeed(), rp.getSeed(), name)) {
				if(rp.getId() != null)
					rn.addIdGeneratorNameMapping(rp.getId(), name);
				return true;
			} else {
				return false;
			}
				
		
		} else if(md != null && randParamSeq == null){ // if multidistribution is used
			/*
			 * TODO Marcin: dopelnienie do 1 (do 100%) moze zostac zdefiniowane w glownym elemencie
			 * Sprawdzic czy faktycznie potrzeba uzupelnienia do 100%, a nastepnie
			 * trzeba by dodac tak opisany generator za pomoca rn.addRandomGenerator(parameterName, detailDist)
			 */ 
			addDetailDistributions(rn, rp, name);
			if(rp.getId() != null)
				rn.addIdGeneratorNameMapping(rp.getId(), name);
			
		} else if(md == null && randParamSeq != null){ // if periodic valid values are used
			
			generator = rn.createRandomGenerator(rp.getDistribution().getType(), rp.hasAvg(), rp.getAvg(), 
								rp.getStdev(), rp.hasMin(), rp.getMin(), rp.hasMax(), rp.getMax(), rp.hasSeed(), rp.getSeed(), name);
			if(generator == null)
				throw new RuntimeException("Define default generator (which will be valid for whole simulation time) for "+name);
			
			PeriodicGenerator pg = new PeriodicGenerator(generator);
			rn.addRandomGenerator(name, pg);
			
			addPeriodicValidValues(rn, rp, name);
			if(rp.getId() != null)
				rn.addIdGeneratorNameMapping(rp.getId(), name);
			
		} else {
			throw new RuntimeException("Use Multidistribution _OR_ Periodic valid values. This two things can not be used togeter.");
		}
		
		return true; 
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public String[] getTriggerNames(){
		return triggerNames;
	}
	
	public static String getGeneratorName(Object object){
		return String.valueOf(object.hashCode());
	}
	
}

