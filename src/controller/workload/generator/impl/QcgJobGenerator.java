package controller.workload.generator.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.types.Duration;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.qcg.broker.schemas.jobdesc.Application;
import org.qcg.broker.schemas.jobdesc.ComputingResourceBaseTypeChoice;
import org.qcg.broker.schemas.jobdesc.ComputingResourceBaseTypeChoiceItem;
import org.qcg.broker.schemas.jobdesc.ComputingResourceParameterType;
import org.qcg.broker.schemas.jobdesc.ComputingResourceType;
import org.qcg.broker.schemas.jobdesc.Executable;
import org.qcg.broker.schemas.jobdesc.ExecutionTimeType;
import org.qcg.broker.schemas.jobdesc.ExecutionType;
import org.qcg.broker.schemas.jobdesc.ParameterBoundaryType;
import org.qcg.broker.schemas.jobdesc.ParameterChoice;
import org.qcg.broker.schemas.jobdesc.ParameterTypeChoice;
import org.qcg.broker.schemas.jobdesc.ParameterTypeChoiceItem;
import org.qcg.broker.schemas.jobdesc.ParameterValueType;
import org.qcg.broker.schemas.jobdesc.PreferencesType;
import org.qcg.broker.schemas.jobdesc.QcgJob;
import org.qcg.broker.schemas.jobdesc.RangeType;
import org.qcg.broker.schemas.jobdesc.RequirementsType;
import org.qcg.broker.schemas.jobdesc.ResourceRequirementsType;
import org.qcg.broker.schemas.jobdesc.StringValue;
import org.qcg.broker.schemas.jobdesc.Task;
import org.qcg.broker.schemas.jobdesc.TimePeriod;
import org.qcg.broker.schemas.jobdesc.TimePeriodChoice;
import org.qcg.broker.schemas.jobdesc.types.ComputingParameterName;
import org.qcg.broker.schemas.jobdesc.types.ExecutionTypeTypeType;
import org.qcg.broker.schemas.jobdesc.types.ParameterOptimizationTypeType;
import org.qcg.broker.schemas.jobdesc.types.PreferencesTypePreferenceTypeType;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import simulator.workload.generator.configuration.ComputingResourceHostParameter;
import simulator.workload.generator.configuration.ExecDuration;
import simulator.workload.generator.configuration.ExecutionTime;
import simulator.workload.generator.configuration.ExecutionTimeChoice;
import simulator.workload.generator.configuration.Importance;
import simulator.workload.generator.configuration.Max;
import simulator.workload.generator.configuration.Min;
import simulator.workload.generator.configuration.Parameter;
import simulator.workload.generator.configuration.PeriodDuration;
import simulator.workload.generator.configuration.PeriodEnd;
import simulator.workload.generator.configuration.PeriodStart;
import simulator.workload.generator.configuration.Preferences;
import simulator.workload.generator.configuration.Range;
import simulator.workload.generator.configuration.TaskLength;
import simulator.workload.generator.configuration.Value;
import simulator.workload.generator.configuration.WorkloadConfiguration;
import simulator.workload.generator.configuration.types.OptimizationTypeType;
import simulator.workload.generator.configuration.types.PreferencesPreferenceTypeType;
import controller.simulator.utils.GSSimXML;
import controller.simulator.utils.XsltTransformations;
import controller.workload.SWFFields;
import controller.workload.generator.JobGenerator;
import controller.workload.generator.fileFilters.QcgJobFileNameFilter;
import controller.workload.generator.writer.QcgSWFJobWriter;
import controller.workload.generator.writer.SWFJobWriter;
import controller.workload.loader.QcgSWFJobReader;
import controller.workload.loader.SWFParser;
import gridsim.DCWormsConstants;
import model.exceptions.NoSuchCommentException;

import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList;

public class QcgJobGenerator extends AbstractJobGenerator implements JobGenerator {

	private static Log log = LogFactory.getLog(QcgJobGenerator.class);
	protected static int TIME_OUT = 10;
	private File lastUpdatedFile = null;
	private String lastUpdatedJob = null;
	
	public void generateXMLSupplement(String configurationFileName, String waFileName, 
							String outputDirectoryName, boolean ignoreNonEmptyOutputDir) 
												throws FileNotFoundException, IOException{
		if(log.isInfoEnabled())
			log.info("Generate xml suplement - start");
		
		QcgSWFJobReader swfReader = new QcgSWFJobReader(waFileName);
		SWFParser swfParser = new SWFParser(waFileName);
		QcgJob qcgJob = null;
		Task task = null;
		Writer writer = null;
		long startTime = 0;
		
		File outputDirectory = prepareWorkloadDirectory(outputDirectoryName, ignoreNonEmptyOutputDir);
		
//		try {
//			randomNumbersWrapper.findDependencies(configurationFileName);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		workload = (WorkloadConfiguration) GSSimXML.unmarshal(WorkloadConfiguration.class, new FileReader(configurationFileName));
		init(workload);
		this.randomNumbersWrapper.setExternalFileDpendencies(waFileName);
		
		swfParser.loadHeader();
		try {
			SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZ yyyy", Locale.ENGLISH);
			startTime = df.parse(swfParser.getCommentValue(SWFFields.COMMENT_STARTTIME)).getTime();
		} catch (NoSuchCommentException e) {
			if(log.isErrorEnabled())
				log.error("No start time filed in swf header. Start time = 0");
			startTime = 0;
		} catch (ParseException e) {
			if(log.isErrorEnabled())
				log.error("Start time parse exception. Start time = 0");
			startTime = 0;
		}
		
		try {
			
			while((qcgJob = swfReader.read()) != null){
				
				for(int i = 0; i < qcgJob.getTaskCount(); i++){
					task = qcgJob.getTask(i);
					this.randomNumbersWrapper.setCurrentJobId(qcgJob.getAppId());
					this.randomNumbersWrapper.setCurrentTaskId(task.getTaskId());
					RequirementsType hardConstraints = createResourceRequirements(workload, task.getRequirements());
					if(hardConstraints != null) {
						task.setRequirements(hardConstraints);
					}
					
					
					long submitTime = 0;
					String sTab[] = swfParser.readTask(qcgJob.getAppId(), task.getTaskId());
					if(sTab != null)
						submitTime = startTime + Long.parseLong(sTab[SWFFields.DATA_SUBMIT_TIME]) * 1000;

					ExecutionTimeType execTime = createTimeRequirements(workload, submitTime, task.getExecutionTime());
					if(execTime != null) {
						task.setExecutionTime(execTime);
					}
					
				}
				
				if(qcgJob.getTaskCount() > 0){
					writer = new FileWriter(outputDirectory.getPath() + 
											File.separator + 
											QcgJobFileNameFilter.FILE_NAME_PREFIX + 
											qcgJob.getAppId() + 
											".xml");
					
					GSSimXML.marshal(qcgJob, writer);
					writer.close();
				}
			}
			
			swfReader.close();
			
		} catch (IOException e) {
			throw e;
		} finally {
			swfReader.close();
			swfParser.close();
			this.randomNumbersWrapper.close();
		}
		
		if(log.isInfoEnabled())
			log.info("Generate xml suplement - done");
	}

	
	public void generateWorkload(WorkloadConfiguration workload,
			String outputDirectoryName, boolean overwriteExistingFiles,
			String outputWorkloadFileName) throws FileNotFoundException, IOException {
		
		if(log.isInfoEnabled())
			log.info("Generate workload - start");
		
		boolean creationResult = false;
		File outputDirectory = prepareWorkloadDirectory(outputDirectoryName, overwriteExistingFiles);

		SWFJobWriter<QcgJob> swfJobWriter = new QcgSWFJobWriter(outputDirectoryName, outputWorkloadFileName);
		swfJobWriter.writeFieldDescriptionHeader();
		
//		try {
//			randomNumbersWrapper.findDependencies(workloadConfigFileName);
//		} catch (Exception e2) {
//			e2.printStackTrace();
//		}

		// load workload configuration from xml file
//		try {
//			workload = WorkloadConfiguration.unmarshal(new FileReader(workloadConfigFileName));
//		} catch (MarshalException e2) {
//			e2.printStackTrace();
//		} catch (ValidationException e2) {
//			e2.printStackTrace();
//		}
		this.workload = workload;
		// beginning of time FIXME the SIMULATION VIRTUAL TIME has not yet been tested
		simulationVirtualTime = workload.getSimulationStartTime().getTime();
		long simulationStartTime = simulationVirtualTime;
		
		swfJobWriter.writeComment("StartTime", String.valueOf(workload.getSimulationStartTime()));
		
		jobCount = workload.getWorkloadConfigurationChoice().getJobCount();
		simulationDuration = workload.getWorkloadConfigurationChoice().getSimulationTime();
		if (simulationDuration != null)
			simulationEndTime = new Date(simulationVirtualTime+simulationDuration.toLong());
		
		init(workload);

		if (jobCount != null) {
			try {
				jobCountToBeGenerated = (int) getRandomValueFor(jobCount, parameterRandomNumbers);
				jobCountToBeGenerated--; // because we count jobs from 0
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		// the random length of the task group to be submitted at one moment
		long jobPackageLength = 0L;
		TaskLength taskLength;
		// we start with first job
		generatedJobCount = 0;
		
		while (proceedConditionsAreFulfilled()) {
			QcgJob QcgJob = new QcgJob();
			
			String jobID = Integer.toString(generatedJobCount);
			QcgJob.setAppId(jobID);
			
			//initially - a standard value
			taskCountToBeGenerated = DCWormsConstants.DEFAULT_TASK_COUNT_IN_SINGLE_JOB; 
			
			try {
				taskCountToBeGenerated = (int) getRandomValueFor(workload.getTaskCount(), parameterRandomNumbers);
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}

			// the end of current package of tasks, need to generate new package length
			if (jobPackageLength == 0) {
				try {
					// it is always a whole number
					jobPackageLength = (long) getRandomValueFor(workload.getJobPackageLength(), parameterRandomNumbers);
					// it is always a whole number
					long jobInterval = (long) getRandomValueFor(workload.getJobInterval(), parameterRandomNumbers);
					// all jobs (tasks) in a package have the same submit time 
					simulationVirtualTime = simulationVirtualTime + (jobInterval * 1000);
					if (simulationDuration != null) {
						simulationVirtualTime = Math.min(simulationVirtualTime, simulationEndTime.getTime());
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			
			for (int taskNum = 0; taskNum < taskCountToBeGenerated; taskNum++) {
				Task task = new Task();
				QcgJob.addTask(task);
				
				task.setTaskId(String.valueOf(taskNum));
				
				// create section execution
				//ExecutionType execution = createExecution(workload, null);
				//task.setExecution(execution);
				
				// create requirements section
				RequirementsType requirements = createResourceRequirements(workload, null);
				if(requirements != null) {
					task.setRequirements(requirements);
				}
				
				taskLength = workload.getTaskLength();
				if(taskLength != null) {
					try {
						long l_taskLength = (long) getRandomValueFor(taskLength, parameterRandomNumbers);
						swfJobWriter.addTaskLength(task.getTaskId(), l_taskLength);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				
				// create execution time section
				//long submitTime = workload.getSimulationStartTime().getTime() + simulationVirtualTime*1000;
				long submitTime = simulationVirtualTime;
				ExecutionTimeType execTime = createTimeRequirements(workload, submitTime, null);
				if(execTime != null) {
					task.setExecutionTime(execTime);
				}
				
			} // end of task generation


			// one job less from the package
			jobPackageLength--;
			
			// create workflow from generated tasks
			// createWorkflow(workload, QcgJob.getTask());
			
//			File taskFile = null;
//			if(generatedJobCount < 10)
//				taskFile = new File(outputDirectory, QcgJobFileNameFilter.FILE_NAME_PREFIX + "_0" + jobID + ".xml");
//			else
//				taskFile = new File(outputDirectory, QcgJobFileNameFilter.FILE_NAME_PREFIX + "_" + jobID + ".xml");
//			
//			try {
//				creationResult = taskFile.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			
//			// the file was not created
//			if (! creationResult) { 
//				throw new IOException("Cannot create the task file for taskid: "+jobID);
//			}

//			FileWriter taskWriter = new FileWriter(taskFile);
//			GSSimXML.marshal(QcgJob, taskWriter);
//			taskWriter.flush();
//			taskWriter.close();
			
			//swfJobWriter.setJobSubmitTime(simulationVirtualTime);
			swfJobWriter.setJobSubmitTime((simulationVirtualTime - simulationStartTime)/1000);
			swfJobWriter.write(QcgJob);
			
			generatedJobCount++;
		
		} // end of job generation

		swfJobWriter.close();
		
		if(log.isInfoEnabled())
			log.info("Generate workload - done");
	}

	protected ExecutionType createExecution(WorkloadConfiguration workload, ExecutionType exec){
		ExecutionType execution = new ExecutionType();
		Executable executable = new Executable();
				Application application = new Application();
				application.setName("app");
			executable.setApplication(application);
		execution.setExecutable(executable);
		execution.setType(ExecutionTypeTypeType.SINGLE);
		return execution;
	}


	protected RequirementsType createResourceRequirements(WorkloadConfiguration workload, RequirementsType hCons) {
		ResourceRequirementsType resRequirements = null;
		RequirementsType requirements = hCons;
		ComputingResourceHostParameter compResParameters[] = workload.getComputingResourceHostParameter();
		
		if (compResParameters != null && compResParameters.length > 0) {
			
			if(requirements == null) {
				requirements = new RequirementsType();
				
			}
			
			resRequirements = requirements.getResourceRequirements();
			if(resRequirements == null){
				resRequirements = new ResourceRequirementsType();
				requirements.setResourceRequirements(resRequirements);
			}
			
			ComputingResourceType computingResource = null;
			if(resRequirements.getComputingResourceCount() == 0){
				computingResource = new ComputingResourceType();
				resRequirements.addComputingResource(computingResource);
			} else {
				computingResource = resRequirements.getComputingResource(0);
			}
			
			ComputingResourceBaseTypeChoice choice = computingResource.getComputingResourceBaseTypeChoice();
			if(choice == null){
				choice = new ComputingResourceBaseTypeChoice();
				computingResource.setComputingResourceBaseTypeChoice(choice);
			}
					
			
			
			for (int i = 0; i < compResParameters.length; i++) {
				ComputingResourceHostParameter compResParameter = compResParameters[i];

				ComputingResourceBaseTypeChoiceItem item = new ComputingResourceBaseTypeChoiceItem();
				choice.addComputingResourceBaseTypeChoiceItem(item);
				
				ComputingResourceParameterType hostParameter = new ComputingResourceParameterType();
				item.setHostParameter(hostParameter);
				
				String name = compResParameter.getMetric();
				ComputingParameterName parameterName = ComputingParameterName.fromValue(name);
				hostParameter.setName(parameterName);
				
				Range[] ranges = compResParameter.getRange();
				ParameterTypeChoice ptc = new ParameterTypeChoice();
				hostParameter.setParameterTypeChoice(ptc);
				
				for (int r = 0; r < ranges.length; ++r) {
					ParameterTypeChoiceItem ptci = new ParameterTypeChoiceItem();
						org.qcg.broker.schemas.jobdesc.Range range = new org.qcg.broker.schemas.jobdesc.Range();
					ptci.setRange(range);
					ptc.addParameterTypeChoiceItem(ptci);
					
					Range workloadRange = ranges[r];
					if (workloadRange.getMax() != null) {
						
						try {
							Max maxRange = workloadRange.getMax();
							Double rangeMax = getRandomValueFor(maxRange, hardConstraintsRandomNumbers);
							
							ParameterBoundaryType max = new ParameterBoundaryType();
							max.setContent(Math.round(rangeMax));
							range.setMax(max);
							
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
						
					}
					if (workloadRange.getMin() != null) {
						try {
							Min minRange = workloadRange.getMin();
							Double rangeMin = getRandomValueFor(minRange, hardConstraintsRandomNumbers);
							
							ParameterBoundaryType min = new ParameterBoundaryType();
							min.setContent(Math.round(rangeMin));
							range.setMin(min);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}

					}
				}

				boolean useStringValue = useStringValue(parameterName);
				Value[] values = compResParameter.getValue();
				for (int v = 0; v < values.length; ++v) {
					
					try {
						
						
						Double dValue = getRandomValueFor(values[v], hardConstraintsRandomNumbers);
						
						
						if(useStringValue) {
							StringValue stringValue = new StringValue();
							stringValue.setValue(String.valueOf(Math.round(dValue)));
							hostParameter.addStringValue(stringValue);
						} else {
							ParameterTypeChoiceItem ptci = new ParameterTypeChoiceItem();
							org.qcg.broker.schemas.jobdesc.Value value = new org.qcg.broker.schemas.jobdesc.Value();
								value.setContent(Math.round(dValue));
							ptci.setValue(value);
							ptc.addParameterTypeChoiceItem(ptci);
						}

					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					
				}
			}
		}

		// create section preferences
		if(resRequirements != null){
			PreferencesType preferences = createPreferences(workload, resRequirements.getPreferences());
			if(preferences != null) {
				resRequirements.setPreferences(preferences);
			}
		}
		
		return requirements;
	}
	

	protected PreferencesType createPreferences(WorkloadConfiguration workload, PreferencesType sCons) {
		PreferencesType preferences = sCons;
		Preferences workloadPreferences = workload.getPreferences();
		
		if (workloadPreferences == null) 
			return preferences;
		
		if(preferences == null)
			preferences = new PreferencesType();
			
		PreferencesTypePreferenceTypeType softConstraintsPreferenceTypeType = null;
		
		switch (workloadPreferences.getPreferenceType().getType()) {
			case PreferencesPreferenceTypeType.PRIORITY_TYPE:
				softConstraintsPreferenceTypeType = PreferencesTypePreferenceTypeType.PRIORITY;
				break;
			case PreferencesPreferenceTypeType.RANKING_TYPE:
				softConstraintsPreferenceTypeType = PreferencesTypePreferenceTypeType.RANKING;
				break;
		}

		preferences.setPreferenceType(softConstraintsPreferenceTypeType);

		
		Parameter[] parameters = workloadPreferences.getParameter();
		for (int i = 0; i < parameters.length; ++i) {
			Parameter constraint = parameters[i];
			org.qcg.broker.schemas.jobdesc.Parameter parameter = new org.qcg.broker.schemas.jobdesc.Parameter();
			
			switch (constraint.getOptimizationType().getType()) {
				case OptimizationTypeType.COST_TYPE:
					parameter.setOptimizationType(ParameterOptimizationTypeType.COST);
					break;
				case OptimizationTypeType.GAIN_TYPE:
					parameter.setOptimizationType(ParameterOptimizationTypeType.GAIN);						
					break;
			}

			String parameterName = constraint.getName();
			parameter.setName(parameterName);

			String endpoint = constraint.getEndpoint();
			parameter.setEndpoint(endpoint);

			Importance importance = constraint.getImportance();
			try {
				Double impValue;
				impValue = getRandomValueFor(importance, softConstraintsRandomNumbers);
				
				if (impValue.equals(new Double(0)) && 
						softConstraintsPreferenceTypeType == PreferencesTypePreferenceTypeType.PRIORITY) {
					
					if(log.isWarnEnabled())
						log.warn("\nWARNING: the constraint "+ parameterName + 
										" has importance set to zero."+
										" Non deterministic simulation may occur!\n");
				}
				parameter.setImportance(impValue.intValue());
				
				ParameterChoice choice = new ParameterChoice();
				parameter.setParameterChoice(choice);
				
				Range rangeTab[] = constraint.getRange();
				if(rangeTab != null && rangeTab.length > 0){
					Range range = rangeTab[0];
					Min rangeMin = range.getMin();
					Max rangeMax = range.getMax();
					Double minValue = getRandomValueFor(rangeMin, softConstraintsRandomNumbers);
					Double maxValue = getRandomValueFor(rangeMax, softConstraintsRandomNumbers);
					
					RangeType jobDescRange = new RangeType();
					
					ParameterBoundaryType min = new ParameterBoundaryType();
					min.setContent(minValue);
					jobDescRange.setMin(min);
					
					ParameterBoundaryType max = new ParameterBoundaryType();
					max.setContent(maxValue);
					jobDescRange.setMax(max);
					
					choice.setRange(jobDescRange);
				}
				
				Value value = constraint.getValue();
				if(value != null){
					double v = getRandomValueFor(value, softConstraintsRandomNumbers);
					ParameterValueType vt = new ParameterValueType();
					vt.setContent(v);
					choice.setValue(vt);
				}
				
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			preferences.addParameter(parameter);
		}
		
		return preferences;
	}

	
	protected ExecutionTimeType createTimeRequirements(WorkloadConfiguration workload, long submitTime, ExecutionTimeType exeTime) {
		ExecutionTimeType execTime = exeTime;
		Double value = null;
		long l_execDuration = 0l;
		ExecutionTime timeConstraints = workload.getExecutionTime();
		
		try {
			if(timeConstraints != null) {
				if(execTime == null)
					execTime = new ExecutionTimeType();
				
				ExecDuration execDuration = timeConstraints.getExecDuration();
				if(execDuration != null) {
					value = getRandomValueFor(execDuration, timeConstraintsRandomNumbers);
					l_execDuration = value.longValue();
					execTime.setExecutionDuration(new Duration(l_execDuration));
				}
				
				PeriodStart periodStart = timeConstraints.getPeriodStart();
				ExecutionTimeChoice choice = timeConstraints.getExecutionTimeChoice();
				if(periodStart != null || choice != null){
					TimePeriod timePeriod = new TimePeriod();
						value = 0.0;
						if(periodStart != null){
							value = getRandomValueFor(periodStart, timeConstraintsRandomNumbers);
							timePeriod.setPeriodStart(new Date(submitTime + value.longValue()*1000));
						}
					
						if(choice != null){
							TimePeriodChoice timePeriodChoice = new TimePeriodChoice();
								
								PeriodDuration duration = choice.getPeriodDuration();
								if(duration != null){
									value = getRandomValueFor(duration, timeConstraintsRandomNumbers);
									timePeriodChoice.setPeriodDuration(new Duration(value.longValue()));
								}
								
								PeriodEnd periodEnd = choice.getPeriodEnd();
								if(periodEnd != null){
									value = getRandomValueFor(periodEnd, timeConstraintsRandomNumbers);
									
									timePeriodChoice.setPeriodEnd(
											new Date(submitTime + value.longValue()*1000));
								}
							
							timePeriod.setTimePeriodChoice(timePeriodChoice);
						}
					
					execTime.setTimePeriod(timePeriod);
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return execTime;
	}
	
	public void performPostprocessing(String resDescriptionFileName,
			String outputDirectoryName, String outputWorkloadFileName) {
		int PCT_STEP = 10;
		Long cpuSpeed = Long.MAX_VALUE;
		//XPathFactory factory = XPathFactory.newInstance();
		XPathFactory factory = new com.sun.org.apache.xpath.internal.jaxp.XPathFactoryImpl();
		XPath xpath = factory.newXPath();
		XPathExpression expression = null;
		Object obj = null;
		DTMNodeList nodeList = null;
		Node node = null;
		XsltTransformations transformer = null;
		
		log.info("Workload postprocessing - start");
		
		try {
			
			transformer = new XsltTransformations();
			
			expression = xpath.compile("//parameter[@name='speed']");
			InputSource s = new InputSource(new FileReader(resDescriptionFileName));
			obj = expression.evaluate(s, XPathConstants.NODESET);
			nodeList = (DTMNodeList) obj;
			long current = 0;
			for(int i = 0; i < nodeList.getLength(); i++){
				node = nodeList.item(i);
				current = Long.parseLong(node.getChildNodes().item(1).getTextContent());
				if(current < cpuSpeed)
					cpuSpeed = current;
			}

		
			if(cpuSpeed == Long.MAX_VALUE){
					log.error("minimum CpuSpeed not found.");
				return;
			}
			
			
			File dir = new File(outputDirectoryName);
			File files[] = dir.listFiles(new QcgJobFileNameFilter());
			
			HashMap<String, File> fileMap = new HashMap<String, File>();
			int base = files.length / PCT_STEP;
			int nextBase = base;
			
			log.info("Loading job ids...");
			log.info("0%");
			for(int i = 0; i < files.length; i++){
				File file = files[i];
				String job = readJob(file);
				String jobId = transformer.extractJobId(job);
				fileMap.put(jobId, file);
				if(i + 1 >= nextBase){
					log.info((nextBase / base) * PCT_STEP + "%");
					nextBase = nextBase + base;
				}
			}
			log.info("Loading job ids - DONE");
			
			File wafFile = new File(outputWorkloadFileName);
			if(!wafFile.exists()){
				wafFile = new File(outputDirectoryName, outputWorkloadFileName);
				if(!wafFile.exists())
					throw new IOException("No swf file in specified location.");
			}
			
			
			File originalWAFile = new File(wafFile.getAbsolutePath()+"_original.swf");
			if (originalWAFile.exists())
				if (! originalWAFile.delete())
						log.error("Cannot delete file " + originalWAFile.getAbsolutePath());
			if (! wafFile.renameTo(originalWAFile))
					log.error("Cannot remove file " + originalWAFile.getAbsolutePath());
			
			QcgSWFJobWriter swfWriter = new QcgSWFJobWriter(wafFile.getParent(), wafFile.getName());
			swfWriter.writeFieldDescriptionHeader();
			
			log.info("Updating swf header...");
			SWFParser waParser = new SWFParser(originalWAFile.getAbsolutePath());
			HashMap<String, String> swfHeader = waParser.loadHeader();
			swfHeader.put(SWFFields.COMMENT_PUSPEED, String.valueOf(cpuSpeed));
			
			Set<String> keys = swfHeader.keySet();
			Iterator<String> itr = keys.iterator();
			String key;
			while(itr.hasNext()){
				key = itr.next();
				swfWriter.writeComment(key, swfHeader.get(key));
			}
			log.info("Updating swf header - DONE");
			
			String task[] = null;
			
			long taskLength;
			long value;
			Duration duration;
			HashMap<String, String[]> idMapping = waParser.getIDMapping();
			base = idMapping.size() / PCT_STEP;
			nextBase = base;
			int taskCnt = 0;
			log.info("Processing jobs...");
			log.info("0%");
			while((task = waParser.readTask()) != null){
				taskCnt++;
				
				String ids[] = idMapping.get(task[SWFFields.DATA_JOB_NUMBER]);
				String jobId = ids[0];
				String taskId = ids[1];
				
				taskLength = Long.parseLong(task[SWFFields.DATA_RUN_TIME]);
				
				// value = how many seconds this task was executed on processing unit with speed = cpuSpeed
				value = taskLength / cpuSpeed;
				task[SWFFields.DATA_RUN_TIME] = String.valueOf(value);
				
				taskLength = Long.parseLong(task[SWFFields.DATA_REQUESTED_TIME]);
				value = taskLength / cpuSpeed;
				task[SWFFields.DATA_REQUESTED_TIME] = String.valueOf(value); 
				
				value = value * 1000;			// taskLenght in milliseconds
				duration = new Duration(value);
				
//				File file = fileMap.get(jobId);
//				String job = readJob(file);
//				String updatedJob = transformer.jobPostProcessing(job, taskId, duration.toString());
//				writeJob(file, updatedJob);
				
				swfWriter.addTaskMapping(task[SWFFields.DATA_JOB_NUMBER], jobId, taskId);
				swfWriter.writeLine(task);
				if(taskCnt >= nextBase){
					log.info((nextBase / base) * PCT_STEP + "%");
					nextBase = nextBase + base;
				}
			}
			
			flushCache();
			
			waParser.close();
			swfWriter.close();
			log.info("Processing jobs - DONE");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (DOMException e) {
			e.printStackTrace();
		}
		
		log.info("Workload postprocessing - done");
	}
	
	/**
	 * Reads job description from specified file. If the argument file 
	 * was updated by previous writeJob() method, then readJob returns updated 
	 * value without physical disk access.
	 * 
	 * @param file
	 * @return
	 */
	protected String readJob(File file){
		if(file.equals(lastUpdatedFile)){
			return lastUpdatedJob;
		}
		
		flushCache();
		this.lastUpdatedJob = null;
		
		String doc = "";
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null){
				doc += line;
			}
			reader.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return doc;
	}
	
	/**
	 * This implementation does not really writes file to disk, instead job is saved
	 * in buffer and file is marked as last update file. To force writing task do disk
	 * call flushCache() method.
	 * @param file
	 * @param job
	 */
	protected void writeJob(File file, String job){
		this.lastUpdatedFile = file;
		this.lastUpdatedJob = job;
	}
	
	/**
	 * Force writing job description to file specified in last/previous call of
	 * writeJob method.
	 */
	protected void flushCache(){
		if(lastUpdatedFile == null || lastUpdatedJob == null)
			return;
		
		if(lastUpdatedFile.exists()){
			lastUpdatedFile.delete();
		}
		
		Writer writer = null;
		try {
			
			writer = new FileWriter(lastUpdatedFile);
			writer.write(lastUpdatedJob);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	protected void createWorkflow(WorkloadConfiguration workload, Task tasks[]){
		throw new RuntimeException("Not implemented.");
	}
	
	protected boolean useStringValue(ComputingParameterName parameterName){
		// odblokowac te metode, jak tylko skoncza sie testy Kikasa i Ariela
//		if(1 == 1)
//			return false;
		
		switch (parameterName){
			case OSNAME:
			case OSTYPE:
			case OSVERSION:
			case CPUARCH:
			case OSRELEASE:
			case APPLICATION:
			case HOSTNAME:
			case REMOTESUBMISSIONINTERFACE:
			case LOCALRESOURCEMANAGER: return true;
			
			default : return false;
		}
	}	
}
