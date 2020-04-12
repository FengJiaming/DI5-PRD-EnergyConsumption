package controller.simulator.stats.implementation;

import java.awt.Paint;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.encoders.ImageFormat;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYStepAreaRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.title.Title;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.TextAnchor;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtilsExt;

import app.ConfigurationOptions;
import controller.ResourceController;
import controller.simulator.DataCenterWorkloadSimulator;
import controller.simulator.stats.GSSAccumulator;
import controller.simulator.stats.SimulationStatistics;
import model.ExecutablesList;
import model.exceptions.ResourceException;
import model.resources.ResourceType;
import model.resources.UserResourceType;
import model.resources.computing.ComputingResource;
import model.resources.computing.extensions.Extension;
import model.resources.computing.extensions.ExtensionList;
import model.resources.computing.extensions.ExtensionType;
import model.resources.computing.profiles.energy.EnergyExtension;
import model.resources.computing.profiles.energy.power.PowerUsage;
import model.resources.units.PEUnit;
import model.resources.units.ProcessingElements;
import model.resources.units.ResourceUnit;
import model.resources.units.ResourceUnitName;
import model.resources.units.StandardResourceUnitName;
import model.scheduling.ResourceHistoryItem;
import model.scheduling.Scheduler;
import model.scheduling.manager.tasks.JobRegistry;
import model.scheduling.manager.tasks.JobRegistryImpl;
import model.scheduling.tasks.Job;
import model.scheduling.tasks.JobInterface;
import controller.simulator.stats.implementation.out.AbstractStringSerializer;
import controller.simulator.stats.implementation.out.StringSerializer;
import csiro.mit.utils.jfreechart.timetablechart.TimetableChartFactory;
import csiro.mit.utils.jfreechart.timetablechart.data.Timetable;
import csiro.mit.utils.jfreechart.timetablechart.data.TimetableEventGroup;
import csiro.mit.utils.jfreechart.timetablechart.data.TimetableEventSource;
import csiro.mit.utils.jfreechart.timetablechart.renderer.TimetableRenderer;
import eduni.simjava.Sim_stat;
import gridsim.DCWormsConstants;
import gridsim.GenericUser;
import gridsim.schedframe.ExecTask;
import gridsim.schedframe.Executable;

public class DCWormsStatistics implements SimulationStatistics {

	private Log log = LogFactory.getLog(DCWormsStatistics.class);

	protected static float ALPHA = 0.5f;
	protected static int GANTT_WIDTH = 1200;
	
	protected static final int MILLI_SEC = 1000;

	protected static final String RAW_TASKS_STATISTICS_OUTPUT_FILE_NAME = "Tasks.txt";
	protected static final String JOBS_STATISTICS_OUTPUT_FILE_NAME = "Jobs.txt";

	protected static final String SIMULATION_STATISTICS_OUTPUT_FILE_NAME = "Simulation.txt";
	protected static final String RESOURCEUTILIZATION_STATISTICS_OUTPUT_FILE_NAME = "ResourceUtilization.txt";
	protected static final String ENERGYUSAGE_STATISTICS_OUTPUT_FILE_NAME = "EnergyUsage.txt";

	protected static final String DIAGRAMS_FILE_NAME_PREFIX = "Chart_";
	protected static final String STATS_FILE_NAME_PREFIX = "Stats_";

	protected static final String TASK_SEPARATOR = "	";
	protected static final String JOB_SEPARATOR = ";";

	protected String outputFolderName;

	protected String simulationIdentifier;
	protected ConfigurationOptions configuration;

	protected GSSAccumulatorsStats accStats;
	protected Map<String, GSSAccumulator> statsData;
	
	protected GenericUser users;
	protected ResourceController resourceController;
	protected boolean generateDiagrams = true;
	protected AbstractStringSerializer serializer;
	protected long startSimulationTime;
	protected long endSimulationTime;
	
	//RESOURCES
	protected Map<String, List<ResStat>> basicResStats;
	protected Map<String, Double> basicResLoad;
	
	protected Map<String, TimetableEventSource> peGanttMap;
	protected Map<String, TimetableEventGroup> taskGanttMap;
	
	protected Timetable ganttDiagramTimetable;
	protected Map<ResourceType, List<XYDataset>> resourcePowerUsageDiagrams;
	//TASKS
	protected int numOfdelayedTasks = 0;
	protected int numOfNotExecutedTasks = 0;
	protected double maxCj = 0;

	protected boolean allTasksFinished;
	protected HashMap<String, List<String>> task_processorsMap;

	protected JobRegistry jr;

	
	public DCWormsStatistics(String simulationIdentifier, ConfigurationOptions co, GenericUser users,
			String outputFolderName, ResourceController resourceController) {
		this.simulationIdentifier = simulationIdentifier;
		this.configuration = co;
		this.users = users;

		this.outputFolderName = outputFolderName;

		this.serializer = new StringSerializer();
		this.serializer.setDefaultNumberFormat(DataCenterWorkloadSimulator.DFAULT_NUMBER_FORMAT);

		this.resourceController = resourceController;
		this.jr = new JobRegistryImpl("");
		init();
	}

	public void generateStatistics() {

		if(users.isSimStartTimeDefined())
			this.startSimulationTime = DateTimeUtilsExt.getOffsetTime().getTimeInMillis();
		else
			this.startSimulationTime = new DateTime(users.getSubmissionStartTime() * 1000l).getMillis();
		this.endSimulationTime = DateTimeUtilsExt.currentTimeMillis();

		long s = 0;
		long e = 0;

		log.info("gatherResourceStatistics");
		s = System.currentTimeMillis();
		gatherResourceStatistics();
		e = System.currentTimeMillis();
		log.info("time in sec: " + ((e - s) / MILLI_SEC));
		
		log.info("gatherTaskStatistics");
		s = System.currentTimeMillis();
		gatherTaskStatistics();
		e = System.currentTimeMillis();
		log.info("time in sec: " + ((e - s) / MILLI_SEC));
		
		log.info("saveSimulationStatistics");
		s = System.currentTimeMillis();
		saveSimulationStatistics();
		e = System.currentTimeMillis();
		log.info("time in sec: " + ((e - s) / MILLI_SEC));

	}


	public String getOutputFolderName() {
		return outputFolderName;
	}

	private void init() {
		task_processorsMap = new HashMap<String, List<String>>();
		accStats = new GSSAccumulatorsStats();
		statsData = new HashMap<String, GSSAccumulator>();
	}

	public void saveSimulationStatistics() {
		PrintStream simulationStatsFile = null;
		if (configuration.createsimulationstatistics) {
			try {
				File file = new File(outputFolderName + STATS_FILE_NAME_PREFIX + simulationIdentifier + "_"
						+ SIMULATION_STATISTICS_OUTPUT_FILE_NAME);
				simulationStatsFile = new PrintStream(new FileOutputStream(file));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (simulationStatsFile != null) {
			Object txt = accStats.serialize(this.serializer);
			simulationStatsFile.println(txt);
		}

		if (simulationStatsFile != null) {
			simulationStatsFile.close();
		}
	}
	
	/************* RESOURCE STATISTICS SECTION **************/
	private void gatherResourceStatistics() {
		
		HashMap<String, List<Stats>> type_stats = new HashMap<String, List<Stats>>();
		
		for(String resourceName: resourceController.getComputingResourceLayers()){
			List<Stats> cStats = new ArrayList<Stats>();
			cStats.add(Stats.textLoad);
			cStats.add(Stats.textEnergy);
			if(ArrayUtils.contains(configuration.resForEnergyChart, resourceName))
				cStats.add(Stats.chartEnergy);
			type_stats.put(resourceName, cStats);
		}		
		
		peGanttMap = new HashMap<String, TimetableEventSource>();
		taskGanttMap = new HashMap<String, TimetableEventGroup>();
		
		resourcePowerUsageDiagrams = new HashMap<ResourceType, List<XYDataset>>();
		
		ganttDiagramTimetable = new Timetable(new FixedMillisecond(
				startSimulationTime), new FixedMillisecond(endSimulationTime));
		
//		PrintStream resourceLoadStatsFile = null;
//		try {
//			File file = new File(outputFolderName + STATS_FILE_NAME_PREFIX
//					+ simulationIdentifier + "_"
//					+ RESOURCEUTILIZATION_STATISTICS_OUTPUT_FILE_NAME);
//			resourceLoadStatsFile = new PrintStream(new FileOutputStream(file));
//		} catch (IOException e) {
//			resourceLoadStatsFile = null;
//		}
		
		PrintStream energyStatsFile = null;
		try {
			File file = new File(outputFolderName + STATS_FILE_NAME_PREFIX
					+ simulationIdentifier + "_"
					+ ENERGYUSAGE_STATISTICS_OUTPUT_FILE_NAME);
			energyStatsFile = new PrintStream(new FileOutputStream(file));
		} catch (IOException e) {
			energyStatsFile = null;
		}
		

		basicResStats = gatherPEStats(jr.getExecutableTasks());
		peStatsPostProcessing(basicResStats);
		basicResLoad = calculatePELoad( basicResStats);

		if (configuration.creatediagrams_gantt) {
			createPEGanttDiagram(basicResStats);
		}
		
		for(String resourceName: resourceController.getComputingResourceLayers()){
			List<ComputingResource> resources = null;

			resources = new ArrayList<ComputingResource>();
			for(ComputingResource compRes: resourceController.getComputingResources() ){
				resources.addAll(compRes.getDescendantsByType(new UserResourceType(resourceName)));
			}
			if(resourceController.getComputingResources().get(0).getType().getName().equals(resourceName))
				resources.addAll(resourceController.getComputingResources());
		
			if(type_stats.containsKey(resourceName)){
				for(ComputingResource resource: resources){
					ResourceUsageStats resourceUsage = null;
					ResourcePowerStats energyUsage = null;
//					if(type_stats.get(resourceName).contains(Stats.textLoad)){
//						resourceUsage = gatherResourceLoadStats(resource, basicResStats);
//						resourceUsage.setMeanValue(calculateMeanValue(resourceUsage));
//						if (resourceLoadStatsFile != null) {
//							Object txt = resourceUsage.serialize(serializer);
//							resourceLoadStatsFile.println(txt);
//						}
//					}
					if(type_stats.get(resourceName).contains(Stats.textEnergy)){
						energyUsage = gatherResourcePowerConsumptionStats(resource);
						energyUsage.setMeanValue(calculateMeanValue(energyUsage));
						energyUsage.setSumValue(energyUsage.getMeanValue() * (endSimulationTime - startSimulationTime) / (3600 * MILLI_SEC));
						
						EnergyExtension een = (EnergyExtension)(resource.getExtensionList().getExtension(ExtensionType.ENERGY_EXTENSION));
						if(resourceController.getComputingResources().contains(resource)) {
							if( een != null && een.getPowerProfile() != null &&  een.getPowerProfile().getEnergyEstimationPlugin() != null){
								accStats.meanEnergyUsage.add(energyUsage.getSumValue());
							}

						} else if( een != null && een.getPowerProfile() != null &&  een.getPowerProfile().getEnergyEstimationPlugin() != null){
							ComputingResource parent = resource.getParent();
							boolean top = true;
							while(parent != null){
								een = (EnergyExtension)(parent.getExtensionList().getExtension(ExtensionType.ENERGY_EXTENSION));
								if(een != null &&  een.getPowerProfile() != null &&  een.getPowerProfile().getEnergyEstimationPlugin() != null) {
									top = false;
									break;
								}
								parent = parent.getParent();
							}
							if(top == true){
								accStats.meanEnergyUsage.add(energyUsage.getSumValue());
							}
						}
						if (energyStatsFile != null) {
							Object txt = energyUsage.serialize(serializer);
							energyStatsFile.println(txt);
						}
					}
					if(type_stats.get(resourceName).contains(Stats.chartEnergy)){

						if (configuration.creatediagrams_respowerusage) {
							createResourceEnergyDiagramData(energyUsage);
						}
					}
					
				}
			}
		}
		
		saveResourceGanttDiagrams();
		createAccumulatedResourceSimulationStatistic();
		
		if (energyStatsFile != null) {
			energyStatsFile.close();
		}
//		if (resourceLoadStatsFile != null) {
//			resourceLoadStatsFile.close();
//		}
	}

	
	private void peStatsPostProcessing(Map<String, List<ResStat>> basicResStats){
		ResourceType resType = null;

		for(String key: basicResStats.keySet()){
			List<ResStat> resStats = basicResStats.get(key);
			resType = resStats.get(0).getResType();
			break;
		}
		List<ComputingResource> resources = null;
		try {
			resources = new ArrayList<ComputingResource>();
			for(ComputingResource compRes: resourceController.getComputingResources() ){
				resources.addAll(compRes.getDescendantsByType(resType));
			}
		} catch (Exception e) {
			return;
		}
		for(ComputingResource resource: resources){
			if(!basicResStats.containsKey(resource.getName())){
				basicResStats.put(resource.getName(), new ArrayList<ResStat>());
			}
		}
	}
	
	private Map<String, List<ResStat>> gatherPEStats( ExecutablesList executables) {
		
		Map<String, List<ResStat>> basicResStats = new TreeMap<String, List<ResStat>>(new MapPEIdComparator());

		for (ExecTask execTask:executables) {
			Executable exec = (Executable) execTask;

			List<ResourceHistoryItem> resHistItemList = exec.getUsedResources();
			if(resHistItemList.size() == 0)
				return basicResStats;
			Map<ResourceUnitName, ResourceUnit> res = resHistItemList.get(resHistItemList.size() - 1).getResourceUnits();
			ResourceUnit resUnit = res.get(StandardResourceUnitName.PE);
			//ProcessingElements pes = (ProcessingElements) resUnit ;
			if(resUnit instanceof ProcessingElements){
				ProcessingElements pes = (ProcessingElements) resUnit;
				for(ComputingResource pe: pes){
					String peName = pe.getName();
					long startDate = Double.valueOf(exec.getExecStartTime()).longValue() * MILLI_SEC;
					long endDate = Double.valueOf(exec.getFinishTime()).longValue() * MILLI_SEC;

					String uniqueTaskID = getUniqueTaskId(execTask);
					
					ResStat resStat = new ResStat(peName, pe.getType(), startDate, endDate, uniqueTaskID);
					
					List<ResStat> resStats = basicResStats.get(peName);
					if (resStats == null) {
						resStats = new ArrayList<ResStat>();
						resStats.add(resStat);
						basicResStats.put(peName, resStats);
					} else {
						resStats.add(resStat);
					}

					List<String> peNames = task_processorsMap.get(uniqueTaskID);
					if (peNames == null) {
						peNames = new ArrayList<String>();
						peNames.add(peName);
						task_processorsMap.put(uniqueTaskID, peNames);
					} else {
						peNames.add(peName);
					}
				}
			} else if (resUnit instanceof PEUnit){
				PEUnit peUnit = (PEUnit) resUnit ;
			}

		}
		return basicResStats;
	}
	
	//change this method to adjust the colors of gantt chart
	private String getUniqueTaskId(ExecTask execTask){
		return execTask.getJobId() + "_" + execTask.getId();
	}
	
	private ResourceUsageStats gatherResourceLoadStats(ComputingResource resource, Map<String, List<ResStat>> basicStats) {
		ResourceUsageStats usageStats = new ResourceUsageStats(resource.getName(), resource.getType(), "resourceLoadStats");
		int cnt = 0;
		for(String resName: basicStats.keySet()){
			try {
				if(resource.getDescendantByName(resName) != null || resource.getName().compareTo(resName) == 0){
					createResourceLoadData(usageStats, basicStats.get(resName));
					cnt++;
				}
			} catch (ResourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(Long key: usageStats.getHistory().keySet()){
			Double value = usageStats.getHistory().get(key)/cnt;
			usageStats.getHistory().put(key, value);
		}

		return usageStats;
	}
	
	private Map<Long, Double> createResourceLoadData(ResourceUsageStats usageStats, List<ResStat> resStats) {

		TreeMap<Long, Double> ganttData = (TreeMap<Long, Double>)usageStats.getHistory();
		for (ResStat resStat : resStats) {
			
			long start_time = resStat.getStartDate();
			long end_time = resStat.getEndDate();
			if (end_time > start_time) {
				Double end = ganttData.get(end_time);
				if (end == null) {
					ganttData.put(end_time, 0.0);
					Long left = getLeftKey(ganttData, end_time);
					if (left != null) {
						Double leftValue = ganttData.get(left);
						ganttData.put(end_time, leftValue);
					}
				}

				Double start = ganttData.get(start_time);
				if (start == null) {
					ganttData.put(start_time, 0.0);
					Long left = getLeftKey(ganttData, start_time);
					if (left != null) {
						Double leftValue = ganttData.get(left);
						ganttData.put(start_time, leftValue);
					}
				}

				SortedMap<Long, Double> sm = ganttData.subMap(start_time,
						end_time);
				for (Long key : sm.keySet()) {
					Double keyVal = ganttData.get(key);
					ganttData.put(key, keyVal + 1);
				}
			}
		}

		return ganttData;
	}

	private Long getLeftKey(TreeMap<Long, Double> ganttData, Long toKey) {

		SortedMap<Long, Double> sm = ganttData.headMap(toKey);
		if (sm.isEmpty()) {
			return null;
		}
		return sm.lastKey();
	}
	
	
	private ResourcePowerStats gatherResourcePowerConsumptionStats(ComputingResource resource) {
		double power = 0;
		ResourcePowerStats resEnergyUsage = new ResourcePowerStats(resource.getName(), resource.getType(), "resourcePowerConsumptionStats");
		Map<Long, Double> usage = resEnergyUsage.getHistory();
		
		ExtensionList extensionList = resource.getExtensionList();
		if(extensionList != null){
			for (Extension extension : extensionList) {
				if (extension.getType() == ExtensionType.ENERGY_EXTENSION) {
					EnergyExtension ee = (EnergyExtension)extension;
					if(ee.getPowerProfile() == null)
						break;
					List<PowerUsage> powerUsage = ee.getPowerProfile().getPowerUsageHistory();
					if(powerUsage.size() == 0)
						break;
					long lastTime = DateTimeUtilsExt.getOffsetTime().getTimeInMillis();
					long endSimulationTime = DateTimeUtilsExt.currentTimeMillis();
					double lastPower = 0;
					powerUsage.add(new PowerUsage(endSimulationTime, ee.getPowerProfile().getPowerUsageHistory().get(ee.getPowerProfile().getPowerUsageHistory().size()-1).getValue()));
					for(PowerUsage pu:powerUsage){
						usage.put(pu.getTimestamp(), pu.getValue());
						
					///	System.out.println(resource.getName() + ":"+new DateTime(pu.getTimestamp())+";"+pu.getValue());
						power = power + (pu.getTimestamp() - lastTime) * lastPower/ (3600 * MILLI_SEC);
						lastPower = pu.getValue();
						lastTime = pu.getTimestamp();
					}
				}
			}
		}
		//System.out.println(power);
		return resEnergyUsage;
	}

	private void createResourceEnergyDiagramData(ResourceDynamicStats powerConsumptionStats) {

		XYDataset dataset = createResourceChartDataSet(powerConsumptionStats,
				startSimulationTime, endSimulationTime);
		
		List<XYDataset> energyDiagramData = resourcePowerUsageDiagrams.get(powerConsumptionStats.getResourceType());
		if(energyDiagramData == null){
			energyDiagramData = new ArrayList<XYDataset>();
			energyDiagramData.add(dataset);
			resourcePowerUsageDiagrams.put(powerConsumptionStats.getResourceType(), energyDiagramData);
		} else {
			energyDiagramData.add(dataset);
		}
	}
	
	private XYDataset createResourceChartDataSet(
			ResourceDynamicStats dynamicStats, long start, long end) {

		Map<Long, Double> chartData = getResourceChartRawData(dynamicStats);
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries data = new XYSeries(dynamicStats.getResourceName(), false, true);
		data.add(start, 0);
		for (Long key : chartData.keySet()) {
			Double val = chartData.get(key);
			if(key >= startSimulationTime)
				data.add(key, val);
		}
		data.add(end, 0);
		dataset.addSeries(data);
		return dataset;
	}
	
	private Map<Long, Double> getResourceChartRawData(
			ResourceDynamicStats dynamicStats) {

		Map<Long, Double> history = dynamicStats.getHistory();
		Map<Long, Double> chartData = new TreeMap<Long, Double>();
		for (Long key : history.keySet()) {
			chartData.put(key, history.get(key));
		}
		return chartData;
	}
	
	private boolean saveResourceGanttDiagrams() {

		if (!generateDiagrams)
			return false;

		String fileName = new File(outputFolderName, DIAGRAMS_FILE_NAME_PREFIX
				+ simulationIdentifier + "_").getAbsolutePath();

		String chartName = "Gantt diagram for "
				+ DataCenterWorkloadSimulator.SIMULATOR_NAME;
		String simulationTime = "Simulation time";
		Title subtitle = new TextTitle("created for \"" + simulationIdentifier
				+ "\" at " + Calendar.getInstance().getTime().toString());

		JFreeChart peDiagram = null;
		if (configuration.creatediagrams_gantt) {
			peDiagram = getPEGanttDiagram(chartName, subtitle,
					simulationTime);
			if (!saveCategoryChart(peDiagram, fileName + "Gantt",
					"{0}"))
				return false;
		}
		
		chartName = "Energy usage diagram for "
			+ DataCenterWorkloadSimulator.SIMULATOR_NAME;
		JFreeChart resourceEnergyDiagram = null;
		if (configuration.creatediagrams_respowerusage) {
			String axisName = "POWER [W]";
			for(ResourceType resType: resourcePowerUsageDiagrams.keySet()){
				resourceEnergyDiagram = getResourceDynamicDiagram(resourcePowerUsageDiagrams.get(resType), simulationTime, chartName,
						subtitle, axisName);
				if (!saveXYPlotChart(resourceEnergyDiagram, fileName + "Energy - "+resType.getName()))
					return false;
			}
		}
		
		return true;
	}
	
	private JFreeChart getPEGanttDiagram(String chartName, Title subtitle,
			String simulationTime) {
		String processors = "Processing Elements";
		boolean tooltip = true;
		boolean legend = true;
		JFreeChart chart = TimetableChartFactory.createTimetableChart(
				chartName, processors, simulationTime,
				ganttDiagramTimetable, legend, tooltip);
		chart.addSubtitle(subtitle);
		TimetableRenderer rend = (TimetableRenderer) chart.getCategoryPlot()
				.getRenderer();
		rend.setBackgroundBarPaint(null);
		return chart;
	}
	
	private JFreeChart getResourceDynamicDiagram(List<XYDataset> diagramData, String simulationTime, String chartName, Title subtitle,
			String axisName) {
		boolean urls = false;
		boolean tooltip = true;
		boolean legend = true;
		CombinedDomainXYPlot cPlot = new CombinedDomainXYPlot();
		DateAxis xAxis = new DateAxis(simulationTime);
		JFreeChart chart = null;

		for (XYDataset dataset : diagramData) {
			JFreeChart tChart = ChartFactory.createXYStepAreaChart("", "",
				null, dataset, PlotOrientation.VERTICAL, legend, tooltip, urls);
			XYPlot tPlot = tChart.getXYPlot();
			NumberAxis yAxis = new NumberAxis(axisName);
			yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
			tPlot.setRangeAxis(yAxis);
			XYStepAreaRenderer rend = (XYStepAreaRenderer) tPlot.getRenderer();
			rend.setShapesVisible(false);
			cPlot.add(tPlot);
		}
		cPlot.setDomainAxis(xAxis);
		cPlot.setDomainAxisLocation(AxisLocation.TOP_OR_LEFT);
		chart = new JFreeChart(chartName, cPlot);
		chart.addSubtitle(subtitle);
		return chart;
	}
	
	private boolean saveXYPlotChart(JFreeChart c, String fileName) {
		c.setNotify(false);
		c.setAntiAlias(true);
		c.setTextAntiAlias(true);
		c.setBorderVisible(false);

		int height = 900;

		CombinedDomainXYPlot cPlot = (CombinedDomainXYPlot) c.getPlot();
		int nPlots = cPlot.getSubplots().size();

		if (nPlots > 3) {
			height = nPlots * 300;
		}
		int width = GANTT_WIDTH;

		if (configuration.creatediagrams_resources_scale != -1)
			width = (int) ((endSimulationTime - startSimulationTime) * configuration.creatediagrams_resources_scale);
		ChartRenderingInfo info = new ChartRenderingInfo();
		File f = new File(fileName + "." + ImageFormat.PNG);
		final String dcwormsSubtitle = "Created by "
				+ DataCenterWorkloadSimulator.SIMULATOR_NAME
				+ " http://www.gssim.org/";
		c.addSubtitle(new TextTitle(dcwormsSubtitle));
		boolean encodeAlpha = false;
		int compression = 9;// values 0-9
		try {
			ChartUtilities.saveChartAsPNG(f, c, width, height, info,
					encodeAlpha, compression);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void createPEGanttDiagram(Map<String,List<ResStat>> basicResStats) {
		for(String peName: basicResStats.keySet()){
			TimetableEventSource pe = new TimetableEventSource(peName);
			ganttDiagramTimetable.addEventSource(pe);
			peGanttMap.put(peName, pe);
			for(ResStat resStat: basicResStats.get(peName)){

				pe = peGanttMap.get(resStat.getPeName());
				if(pe == null){
					//pe = new TimetableEventSource(resStat.getPeName());
				//	ganttDiagramPeTimetable.addEventSource(pe);
				//	peGanttMap.put(resStat.getPeName(), pe);
				}
				TimetableEventGroup task = taskGanttMap.get(resStat.getTaskID());
				long startDate = resStat.getStartDate();
				long endDate = resStat.getEndDate();
				if (task == null) {
					task = new TimetableEventGroup(resStat.getTaskID());
					taskGanttMap.put(resStat.getTaskID(), task);
					ganttDiagramTimetable.addEventGroup(task);
				}
				ganttDiagramTimetable.addEvent(pe, task,
						new FixedMillisecond(startDate), new FixedMillisecond(endDate));
			}
		}
	}
	
	//TO DO
	private void createAccumulatedResourceSimulationStatistic() {

		List<ComputingResource> resources = resourceController.getComputingResources();
		for(ComputingResource compRes: resources){
			//for(ComputingResource child :compRes.getChildren()){
			
				//ResourceUsageStats resourceUsage = gatherResourceLoadStats(compRes, basicResStats);
				//double load = calculateMeanValue(resourceUsage);
				double load = calculateResourceLoad(compRes, basicResLoad);
				accStats.meanTotalLoad.add(load);
				/*ResourceEnergyStats energyUsage = gatherResourceEnergyStats(compRes);
				energyUsage.setMeanUsage(calculateEnergyLoad(energyUsage));
				accStats.meanEnergyUsage.add(energyUsage.meanUsage);*/
			//}	
		}
	}

	private Map<String, Double> calculatePELoad(Map<String, List<ResStat>> basicResStats){
		Map<String, Double> peLoad = new HashMap<String, Double>();
		for(String resName: basicResStats.keySet()){
			List<ResStat> resStats = basicResStats.get(resName);
			double sum = 0;
			for(ResStat resStat:resStats){
				sum += (resStat.endDate - resStat.startDate);
			}
			double load = sum / (endSimulationTime - startSimulationTime);
			peLoad.put(resName, load);
		}
		return peLoad;
	}
	
	private  Double calculateResourceLoad(ComputingResource resource, Map<String, Double> peLoad ){
		int peCnt = 0;
		double sum = 0;
		for(String resName: peLoad.keySet()){

			try {
				if(resource.getDescendantByName(resName) != null || resource.getName().compareTo(resName) == 0){
					Double load = peLoad.get(resName);
					sum += load;
					peCnt++;
				}
			} catch (ResourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return sum/peCnt;
	}

	
	private double calculateMeanValue(ResourceDynamicStats resDynamicStats ){
		double meanValue = 0;
		long time = -1;
		double value = 0;
		
		Map<Long, Double> history = resDynamicStats.getHistory();
		for (Long key : history.keySet()) {
			
			if(key >= startSimulationTime){
				if (time != -1) {
					meanValue += (value * (key - time)) / (endSimulationTime - startSimulationTime);
					time = key;
				} else {
					time = key;
				}	
			}
			value = history.get(key);
		}
		return meanValue;
	}
	

	/************* TASK STATISTICS SECTION **************/
	public void gatherTaskStatistics() {

		List<JobInterface<?>> jobs = users.getAllReceivedJobs();
		Collections.sort(jobs, new JobIdComparator());

		PrintStream taskStatsFile = null;
		try {
			File file = new File(outputFolderName + STATS_FILE_NAME_PREFIX + simulationIdentifier + "_"
					+ RAW_TASKS_STATISTICS_OUTPUT_FILE_NAME);
			taskStatsFile = new PrintStream(new FileOutputStream(file));
		} catch (IOException e) {
			taskStatsFile = null;
		}


		for (int i = 0; i < jobs.size(); i++) {
			Job job = (Job) jobs.get(i);

			List<Executable> execList = jr.getExecutableTasks().getJobExecutables(job.getId());
			List<TaskStats> taskStatsList = new ArrayList<TaskStats>();

			this.serializer.setFieldSeparator(TASK_SEPARATOR);

			for (int j = 0; j < execList.size(); j++) {

				Executable task = (Executable) execList.get(j);

				TaskStats taskStats = createTaskStats(task);
				if (taskStats != null && taskStatsFile != null) {
					Object txt = taskStats.serialize(serializer);
					taskStatsFile.println(txt);
				}
				if (taskStats != null && taskStats.getExecFinishDate() != -1) {
					if (configuration.createsimulationstatistics) {
						createAccumulatedTaskSimulationStatistic(taskStats);
					}
					allTasksFinished &= task.isFinished();
					taskStatsList.add(taskStats);
				}
			}
		}

		if (configuration.createsimulationstatistics) {
			accStats.makespan.add(maxCj);
			accStats.delayedTasks.add(numOfdelayedTasks);
			accStats.failedRequests.add(users.getAllSentTasks().size() - users.getFinishedTasksCount());
			if(resourceController.getScheduler().getType().getName().equals("GS")){
				for(Scheduler sched: resourceController.getScheduler().getChildren()){
					accStats.meanQueueLength.add(getSchedulerQueueStats(sched));
				}
			}
			else {
				accStats.meanQueueLength.add(getSchedulerQueueStats(resourceController.getScheduler()));
			}
		}

		if (taskStatsFile != null) {
			taskStatsFile.close();
		}
	}

	private double getSchedulerQueueStats(Scheduler scheduler) {

		Sim_stat stats = scheduler.get_stat();
		List<Object[]> measures = stats.get_measures();
		for (Object[] info : measures) {
			String measure = (String) info[0];
			if (measure
					.startsWith(DCWormsConstants.TASKS_QUEUE_LENGTH_MEASURE_NAME)) {
				return stats.average(measure);
			}
		}
		return 0;
	}
	
	private TaskStats createTaskStats(Executable task) {
		TaskStats taskStats = new TaskStats(task, startSimulationTime);
		String uniqueTaskID = taskStats.getJobID() + "_" + taskStats.getTaskID();
		taskStats.setProcessorsName(task_processorsMap.get(uniqueTaskID));
		return taskStats;
	}

	private JobStats createJobStats(List<TaskStats> tasksStats) {

		String jobID = ((TaskStats) tasksStats.get(0)).getJobID();
		JobStats jobStats = new JobStats(jobID);
		double maxCj = 0;

		for (int i = 0; i < tasksStats.size(); i++) {

			TaskStats task = (TaskStats) tasksStats.get(i);
			jobStats.meanTaskStartTime.add(task.getStartTime());
			jobStats.meanTaskCompletionTime.add(task.getCompletionTime());
			maxCj = Math.max(maxCj, task.getCompletionTime());
			jobStats.meanTaskExecutionTime.add(task.getExecutionTime());
			jobStats.meanTaskWaitingTime.add(task.getWaitingTime());
			jobStats.meanTaskFlowTime.add(task.getFlowTime());
			jobStats.meanTaskGQ_WaitingTime.add(task.getGQ_WaitingTime());
			jobStats.lateness.add(task.getLateness());
			jobStats.tardiness.add(task.getTardiness());
		}
		jobStats.makespan.add(maxCj);

		return jobStats;
	}

	private void createAccumulatedTaskSimulationStatistic(TaskStats taskStats) {
		accStats.meanTaskFlowTime.add(taskStats.getFlowTime());
		accStats.meanTaskExecutionTime.add(taskStats.getExecutionTime());
		accStats.meanTaskCompletionTime.add(taskStats.getCompletionTime());
		accStats.meanTaskWaitingTime.add(taskStats.getWaitingTime());
		accStats.meanTaskStartTime.add(taskStats.getStartTime());

		accStats.lateness.add(taskStats.getLateness());
		accStats.tardiness.add(taskStats.getTardiness());

		if (taskStats.getExecFinishDate() == -1) {
			numOfNotExecutedTasks++;
		}
		if (taskStats.getTardiness() > 0.0) {
			numOfdelayedTasks++;
		}

		maxCj = Math.max(maxCj, taskStats.getCompletionTime());
	}

	private boolean saveCategoryChart(JFreeChart c, String fileName, String labelFormat) {
		c.setNotify(false);
		c.setAntiAlias(true);
		c.setTextAntiAlias(true);
		c.setBorderVisible(false);

		CategoryPlot categoryplot = (CategoryPlot) c.getPlot();
		categoryplot.setDomainGridlinesVisible(true);

		if (labelFormat != null) {
			CategoryItemRenderer rend = categoryplot.getRenderer();
			rend.setBaseItemLabelsVisible(true);
			ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER);
			rend.setBasePositiveItemLabelPosition(position);
			NumberFormat nf = NumberFormat.getInstance();
			CategoryItemLabelGenerator gen = new StandardCategoryItemLabelGenerator(labelFormat, nf);
			rend.setBaseItemLabelGenerator(gen);
		}

		int rows = c.getCategoryPlot().getDataset().getColumnKeys().size();

		int height = 900;
		if (rows > 45) {
			height = rows * 20;
		}
		int width = calculateChartWidth(c);
		CategoryItemRenderer rend = c.getCategoryPlot().getRenderer();

		for (int i = 0; i < c.getCategoryPlot().getDataset().getRowKeys().size(); i++) {
			Paint collor = c.getCategoryPlot().getDrawingSupplier().getNextPaint();
			rend.setSeriesOutlinePaint(i, collor);
			rend.setSeriesPaint(i, collor);
		}

		ChartRenderingInfo info = new ChartRenderingInfo();
		// info.setEntityCollection(null);
		File f = new File(fileName + "." + ImageFormat.PNG);
		final String dcwormsSubtitle = "Created by " + DataCenterWorkloadSimulator.SIMULATOR_NAME + " http://www.gssim.org/";
		c.addSubtitle(new TextTitle(dcwormsSubtitle));
		boolean encodeAlpha = false;
		int compression = 9;
		try {
			ChartUtilities.saveChartAsPNG(f, c, width, height, info, encodeAlpha, compression);

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			if (log.isErrorEnabled())
				log.error("The png file (" + fileName
						+ ")\nwill not be generated (It can be too large data size problem)", e);
		} catch (Throwable t) {
			log.error("The png file (" + fileName + ")\nwill not be generated (It can be too large data size problem)",
					t);
		}

		return true;
	}

	private int calculateChartWidth(JFreeChart c) {

		int ganttWidth = GANTT_WIDTH;
		int rows = c.getCategoryPlot().getDataset().getColumnKeys().size();

		if (rows > 45) {
			int height = rows * 20;
			ganttWidth = Math.max((int) (height * 1.5), GANTT_WIDTH);
		}
		return ganttWidth;
	}
	
	private static class JobIdComparator implements Comparator<JobInterface<?>> {

		public int compare(JobInterface<?> o1, JobInterface<?> o2) {
			Integer o1int;
			Integer o2int;
			try{
				o1int = Integer.parseInt(o1.getId());
				o2int = Integer.parseInt(o2.getId());
				return o1int.compareTo(o2int);
			} catch(NumberFormatException e){
				return o1.getId().compareTo(o2.getId());
			}
		}
	}
	
	private static class MapPEIdComparator implements Comparator<String> {

		public int compare(String o1, String o2)  {
			Integer o1int;
			Integer o2int;
			String o1string;
			String o2string;
			o1string = o1.substring(0, o1.indexOf("_"));
			o2string = o2.substring(0, o2.indexOf("_"));
			o1int = Integer.parseInt(o1.substring(o1.indexOf("_")+1));
			o2int = Integer.parseInt(o2.substring(o2.indexOf("_")+1));
			if(o1string.compareTo(o2string) != 0)
				return o1string.compareTo(o2string);
			else
				return o1int.compareTo(o2int);
		}
	}

	public GSSAccumulator getStats(String name) {
		return statsData.get(name);
	}

	public boolean accumulatedStats() {
		return configuration.createsimulationstatistics;
	}

}

class ResStat{
	
	public long getStartDate() {
		return startDate;
	}
	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}
	public long getEndDate() {
		return endDate;
	}
	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}
	public String getTaskID() {
		return taskID;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public ResStat( String peName, long startDate, long endDate, String taskID) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.taskID = taskID;
		this.peName = peName;
	}
	public String getPeName() {
		return peName;
	}
	public void setPeName(String peName) {
		this.peName = peName;
	}
	public ResStat(String peName, ResourceType resType, long startDate, long endDate, String taskID) {
		super();
		this.peName = peName;
		this.resType = resType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.taskID = taskID;
	}
	String peName;
	ResourceType resType;
	long startDate;
	long endDate;
	String taskID;

	public ResourceType getResType() {
		return resType;
	}
	public void setResType(ResourceType resType) {
		this.resType = resType;
	}
}

enum Stats{
	textLoad,
	textEnergy,
	chartEnergy;
}
