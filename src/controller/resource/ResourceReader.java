package controller.resource;

import gridsim.DCWormsConstants;
import gridsim.ResourceCalendar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.qcg.broker.schemas.exception.UnknownParameter;

import app.ConfigurationOptions;
import controller.ResourceController;
import controller.simulator.utils.InstanceFactory;
import model.Initializable;
import model.Parameter;
import model.Parameters;
import model.exceptions.ResourceException;
import model.resources.Resource;
import model.resources.StandardResourceType;
import model.resources.computing.ComputingResource;
import model.resources.computing.ResourceFactory;
import model.resources.computing.description.AbstractResourceDescription;
import model.resources.computing.description.ComputingResourceDescription;
import model.resources.units.ResourceUnit;
import model.resources.units.ResourceUnitName;
import model.scheduling.Scheduler;
import model.scheduling.manager.resources.ManagedResources;
import model.scheduling.plugin.SchedulingPlugin;
import model.scheduling.plugin.estimation.ExecutionTimeEstimationPlugin;
import model.scheduling.policy.local.LocalManagementSystem;
import model.scheduling.queue.TaskQueue;
import model.scheduling.queue.TaskQueueList;
import schemas.Environment;
import schemas.ManagedComputingResources;
import schemas.StringValueWithUnit;

public class ResourceReader {
	
	private Log log = LogFactory.getLog(ResourceReader.class);
	
	protected String resdescFilePath;

	protected ResourceCalendar resourceCalendar;
	protected List<Initializable> toInit = new ArrayList<Initializable>();
	
	private ExecutionTimeEstimationPlugin execTimeEstimationPlugin;
	private String globalSchedulingPluginName;
	private String localSchedulingPluginName;
	
	private Set<String> compResLayers;
	
	public ResourceReader(ConfigurationOptions options) throws IOException {

		resdescFilePath = options.resdescFilePath;
		globalSchedulingPluginName = "plugins.globalplugin.GridFCFSRoundRobinPlugin";
		localSchedulingPluginName = matchSchedulingPlugin(options.schedulingplugin);
		
		prepareCalendar();
		compResLayers = new LinkedHashSet<String>();
	}

	public ResourceController read() throws MarshalException, ValidationException, FileNotFoundException, Exception,
			UnknownParameter {

		File file = new File(resdescFilePath);
		Environment env = Environment.unmarshal(new FileReader(file));
		
		log.info("started creating environment description");
		List<ComputingResourceDescription> mainCompResDescList = createEnvironmentDescription(env);
		log.info("finished creating environment description");

		log.info("started creating resources");
		List<ComputingResource> computingResources = createResources(mainCompResDescList);
		log.info("finished creating resource");

		log.info("started creating schedulers");
		Scheduler mainScheduler = createSchedulers(env.getResources().getScheduler(), computingResources);
		log.info("finished creating schedulers");

		ResourceController rc = new ResourceController(mainScheduler, computingResources);
		Collections.sort(toInit, new ResourceTypeComparator(new ArrayList<String>(compResLayers)));
		rc.setInitList(toInit);
		rc.setCompResLayers(compResLayers);
		return rc;
	}

	protected List<ComputingResourceDescription> createEnvironmentDescription(Environment environment) throws Exception {

		List<ComputingResourceDescription> mainCompResDescList = new ArrayList<ComputingResourceDescription>();
		
		LinkedList<ComputingResourceDescription> resDescStructure = new LinkedList<ComputingResourceDescription>();
		LinkedList<schemas.ComputingResource> toExamine = new LinkedList<schemas.ComputingResource>();
		EnvironmentWrapper environmentWrapper = new EnvironmentWrapper();
		environmentWrapper.wrap(environment);
		
		String execTimeEstimationPluginClassName = null;
		if(environmentWrapper.getTimeEstimationPlugin() != null){
			execTimeEstimationPluginClassName = environmentWrapper.getTimeEstimationPlugin().getName();
		}
		
		try{
			execTimeEstimationPlugin = (ExecutionTimeEstimationPlugin) InstanceFactory.createInstance(
					execTimeEstimationPluginClassName, ExecutionTimeEstimationPlugin.class);
			if(execTimeEstimationPlugin == null) {
				execTimeEstimationPluginClassName = "plugins.timeestimation.DefaultTimeEstimationPlugin";
				execTimeEstimationPlugin = (ExecutionTimeEstimationPlugin) InstanceFactory.createInstance(
						execTimeEstimationPluginClassName, ExecutionTimeEstimationPlugin.class);
			} else {
				Parameters params = EnvironmentWrapper.extractParameters(environmentWrapper.getTimeEstimationPlugin().getParameter());
				execTimeEstimationPlugin.init(params);
			}
		} catch (Exception e){
			if (execTimeEstimationPlugin == null) {
				throw new Exception("Can not create execution time estimation plugin instance.");
			}
		}

		if(environmentWrapper.getComputingResources() != null) {
			for(int i = 0; i < environmentWrapper.getComputingResources().length; i++){
				schemas.ComputingResource compResDef = environmentWrapper.getComputingResources()[i];
				toExamine.push(compResDef);
				
				ComputingResourceDescription compResDesc = new ComputingResourceDescription(compResDef);
				resDescStructure.push(compResDesc);
				mainCompResDescList.add(compResDesc);
			}
		}
		while (!toExamine.isEmpty()) {
			schemas.ComputingResource parentCompResDef = toExamine.pop();
			ComputingResourceDescription parentExecResDesc = resDescStructure.pop();
			if(parentCompResDef.getComputingResourceTypeChoiceSequence() != null)
			{
				int computingResourceCount = parentCompResDef.getComputingResourceTypeChoiceSequence().getComputingResourceCount();
				for (int i = 0; i < computingResourceCount; i++) {

					schemas.ComputingResource compResDef = parentCompResDef.getComputingResourceTypeChoiceSequence().getComputingResource(i);
					if(compResDef == null)
						continue;
					long compResCount = compResDef.getCount() > 1 ? compResDef.getCount() : 1;
					
					for(int j = 0; j < compResCount; j++){
						if(compResDef.getComputingResourceTypeChoiceSequence2() != null){
							String templateId = compResDef.getComputingResourceTypeChoiceSequence2().getTemplateId();
							schemas.ComputingResourceTemplate template = environmentWrapper.getTemplate(templateId);
							environmentWrapper.initWithCompResTemplate(compResDef, template);
						} 
						//toExamine.insertElementAt(compResDef, 0);
						toExamine.addLast(compResDef);
						ComputingResourceDescription resDesc = new ComputingResourceDescription(compResDef);
						parentExecResDesc.addChildren(resDesc);
						//resDescStructure.insertElementAt(resDesc, 0);
						resDescStructure.addLast(resDesc);
					}
				}
			} else
				continue;
		}
		return mainCompResDescList;
	}

	protected List<ComputingResource> createResources(List<ComputingResourceDescription> mainCompResDesList) {

		List<ComputingResource> mainCompResourceList = new ArrayList<ComputingResource>();
		Deque<ComputingResourceDescription> toExamine = new ArrayDeque<ComputingResourceDescription>();
		Deque<ComputingResource> resStructure = new ArrayDeque<ComputingResource>();
		
		for(ComputingResourceDescription mainExecResDes : mainCompResDesList){
			ComputingResource mainResource = ResourceFactory.createResource(mainExecResDes);
			toExamine.push(mainExecResDes);
			resStructure.push(mainResource);
			mainCompResourceList.add(mainResource);
		}

		while (!toExamine.isEmpty()) {
			ComputingResourceDescription parentResDesc = toExamine.pop();
			ComputingResource parentResource = resStructure.pop();
			toInit.add(parentResource);
			compResLayers.add(parentResource.getType().getName());
			List<AbstractResourceDescription> childrenResDesc = parentResDesc.getChildren();
			if (childrenResDesc == null){
				continue;
			}
			int compResCount = childrenResDesc.size();
			for (int i = 0; i < compResCount; i++) {
				ComputingResourceDescription compResDesc = (ComputingResourceDescription) childrenResDesc.get(i);
				toExamine.push(compResDesc);
				ComputingResource resource = ResourceFactory.createResource(compResDesc);
				parentResource.addChild(resource);
				resStructure.push(resource);
			}
		}
		return mainCompResourceList;
	}

	protected Scheduler createSchedulers(schemas.Scheduler[] schedulersDef, List<ComputingResource> mainCompResourceList) throws Exception{

		List<Scheduler> mainSchedulers = new ArrayList<Scheduler>();
			
		Deque<schemas.Scheduler> toExamine = new ArrayDeque<schemas.Scheduler>();	
		Deque<Scheduler> schedulersStructure = new ArrayDeque<Scheduler>();
		for(int i = 0; i < schedulersDef.length; i++){
			schemas.Scheduler schedulerDef = schedulersDef[i];
			Scheduler mainScheduler = initScheduler(schedulerDef, mainCompResourceList);
			toExamine.push(schedulerDef);
			schedulersStructure.push(mainScheduler);
			mainSchedulers.add(mainScheduler);
		}	
	
		while (!toExamine.isEmpty()) {
			schemas.Scheduler parentSchedulerDef = toExamine.pop();
		
			Scheduler parentScheduler = null;
			if(!schedulersStructure.isEmpty())
				parentScheduler = schedulersStructure.pop();
			
			if(parentSchedulerDef.getSchedulerTypeChoice() == null)
				continue;
			int schedulerChoiceItemCount = parentSchedulerDef.getSchedulerTypeChoice().getSchedulerTypeChoiceItemCount();
			for (int i = 0; i < schedulerChoiceItemCount; i++) {
				schemas.Scheduler schedulerDef = parentSchedulerDef.getSchedulerTypeChoice().getSchedulerTypeChoiceItem(i).getScheduler();
				if(schedulerDef == null)
					continue;
				else
				{
					Scheduler scheduler = initScheduler(schedulerDef, mainCompResourceList);
					schedulersStructure.push(scheduler);
					parentScheduler.addChild(scheduler);
					toExamine.push(schedulerDef);
				}
			}
			//necessary if children list isn't initialized in Scheduler
//			parentScheduler.init();
		}

		//TODO - refactor (remove - create scheduler on the basis of resource description)
		Scheduler mainScheduler = null;
		if(mainSchedulers.size() == 1 /*&& mainSchedulers.get(0).get_name().equals("grid")*/){
			mainScheduler = mainSchedulers.get(0);
			mainScheduler.setSchedulingPlugin((SchedulingPlugin) InstanceFactory.createInstance(localSchedulingPluginName,
					SchedulingPlugin.class));
		}
		else{
			SchedulingPlugin schedulingPlugin = (SchedulingPlugin) InstanceFactory.createInstance(globalSchedulingPluginName,
					SchedulingPlugin.class);
			TaskQueueList queues = new TaskQueueList(1);
			TaskQueue queue = new TaskQueue(false);
			queues.add(queue);
			ManagedResources managedResources = new ManagedResources(mainCompResourceList, new HashMap<ResourceUnitName, List<ResourceUnit>>());
			mainScheduler = ResourceFactory.createScheduler(StandardResourceType.GS, "grid", schedulingPlugin , execTimeEstimationPlugin,  queues, managedResources);

			for(Scheduler lr: mainSchedulers){
				mainScheduler.addChild(lr);
			}
			//necessary if children list isn't initialized in Scheduler
//			mainScheduler.init(managedResources);
		}
		return mainScheduler;
	}

	protected Scheduler initScheduler(schemas.Scheduler schedulerDef, List<ComputingResource> mainCompResourceList) throws Exception{
		Scheduler scheduler = null;
		
		ManagedResources managedResources = null;
		//List<ComputingResource> managedCompResources = new ArrayList<ComputingResource>();
		TaskQueueList queues = new TaskQueueList(1);
		
		if(schedulerDef.getQueues()!= null){
			int queueCount = schedulerDef.getQueues().getQueueCount();
			for(int i = 0; i < queueCount; i++){
				schemas.QueueType queueDef = schedulerDef.getQueues().getQueue(i);
				TaskQueue queue = new TaskQueue(queueDef.getReservation());
				queue.setName(queueDef.getName());
				queue.setPriority(queueDef.getPriority());
				queues.add(queue);
			}
		} else {
			TaskQueue queue = new TaskQueue(false);
			queues.add(queue);
		}
		
		if(schedulerDef.getSchedulerTypeChoice() != null) {
			int schedulerChoiceItemCount = schedulerDef.getSchedulerTypeChoice().getSchedulerTypeChoiceItemCount();
			for (int i = 0; i < schedulerChoiceItemCount; i++) {
				ManagedComputingResources mcr = schedulerDef.getSchedulerTypeChoice().getSchedulerTypeChoiceItem(i).getManagedComputingResources();
				if(mcr == null)
					continue;

				List<String> managedCompResourcesIds = new ArrayList<String>();
				int resourceNameCount = mcr.getResourceNameCount();
				for(int j = 0; j < resourceNameCount; j++) { 
					managedCompResourcesIds.add(mcr.getResourceName(j));
				}
				//To correct the bug
				String managedCompName = mainCompResourceList.get(0).getName();
				if(managedCompResourcesIds.size() > 0) {
					managedCompResourcesIds.set(0, managedCompName);
				} else {
					managedCompResourcesIds.add(managedCompName);
				}
				managedResources = matchResourcesForScheduler(mainCompResourceList, managedCompResourcesIds, mcr.getInclude());	
				//managedResources = new ManagedResources(managedCompResources, new HashMap<ResourceUnitName, List<ResourceUnit>>());
			}
		}

		SchedulingPlugin schedulingPlugin = null;
		if(schedulerDef.getSchedulingPlugin() != null){
			String schedulingPluginName = schedulerDef.getSchedulingPlugin().getName();
			schedulingPlugin = (SchedulingPlugin) InstanceFactory.createInstance(schedulingPluginName,
					SchedulingPlugin.class);
			Parameters params = EnvironmentWrapper.extractParameters(schedulerDef.getSchedulingPlugin().getParameter());
			if(schedulerDef.getSchedulingPlugin().getFrequency() != null)
			{
				Parameter param = new Parameter("frequency");
				StringValueWithUnit sv = new StringValueWithUnit();
				sv.setContent(String.valueOf(schedulerDef.getSchedulingPlugin().getFrequency().getContent()));
				sv.setUnit(schedulerDef.getSchedulingPlugin().getFrequency().getUnit());
				param.add(sv);
				
				if(params == null)
					params = new Parameters();
				params.put("frequency", param);
			}
			schedulingPlugin.init(params);
		}	

		//TODO - refactor (create scheduler in 1 line)
		if(schedulerDef.getClazz().equals("GridBroker")){
			scheduler = ResourceFactory.createScheduler(StandardResourceType.GS, "grid", schedulingPlugin, execTimeEstimationPlugin, queues, managedResources);
		} else {
			scheduler = ResourceFactory.createScheduler(StandardResourceType.LS, schedulerDef.getName(), schedulingPlugin, execTimeEstimationPlugin,  queues, managedResources);
		}
		return scheduler;
	}
	
	private String matchSchedulingPlugin(String policy) {
		String schedulingPluginName = null;
		switch (policy){
			case "FCFS_BestFit":
				schedulingPluginName = "plugins.localplugin.FCFSBF_LocalPlugin";
				break;
			case "LCFS_BestFit" :
				schedulingPluginName = "plugins.localplugin.LCFS_LocalPlugin";
				break;
			case "LJF_BestFit" :
				schedulingPluginName = "plugins.localplugin.LJF_LocalPlugin";
				break;
			case "SJF_BestFit" :
				schedulingPluginName = "plugins.localplugin.SJF_LocalPlugin";
				break;
			case "FCFS_BestFit_NodeManagement" :
				schedulingPluginName = "plugins.localplugin.FCFSBF_NodePowerManagementClusterPlugin";
		}
		return schedulingPluginName;
	}
	/*private List<ComputingResource> matchCompResourcesForScheduler(List<ComputingResource> mainCompResourceList, List<String> resIdList, boolean include){
		List<ComputingResource> compResources = new ArrayList<ComputingResource>();
		for(ComputingResource mainCompRes: mainCompResourceList){
			for(String resourceName : resIdList){
				ComputingResource computingResource;
				try {
					if(resourceName.equals(mainCompRes.getName()))
						computingResource = mainCompRes;
					else
						computingResource = mainCompRes.getDescendantByName(resourceName);
				} catch (ResourceException e) {
					computingResource = null;
				}
				if(computingResource != null)
				{
					if(include){
						compResources.add(computingResource);
					} else{
						compResources.addAll(computingResource.getChildren());
					}
				}
			}
		}
		return compResources;
	}*/
	
	private ManagedResources matchResourcesForScheduler(List<ComputingResource> mainCompResourceList, List<String> resIdList, boolean include){
		
		List<ComputingResource> compResources = new ArrayList<ComputingResource>();
		Map<ResourceUnitName, List<ResourceUnit>> resourceUnits = new HashMap<ResourceUnitName, List<ResourceUnit>>();
		
		for(ComputingResource mainCompRes: mainCompResourceList){
			for(String resourceName : resIdList){
				ComputingResource computingResource;
				try {
					if(resourceName.equals(mainCompRes.getName()))
						computingResource = mainCompRes;
					else
						computingResource = mainCompRes.getDescendantByName(resourceName);
				} catch (ResourceException e) {
					continue;
				}
				if(computingResource != null)
				{
					if(include){
						compResources.add(computingResource);
					} else{
						compResources.addAll(computingResource.getChildren());
						resourceUnits = getSharedResourceUnits(computingResource);
					}
				}
			}
		}
		return new ManagedResources(compResources, resourceUnits);
	}
	
	
	private Map<ResourceUnitName, List<ResourceUnit>> getSharedResourceUnits(ComputingResource compResources){
		Map<ResourceUnitName, List<ResourceUnit>> resourceUnits = new HashMap<ResourceUnitName, List<ResourceUnit>>();
		List<ResourceUnit> list;
		boolean resourceNotVisited = true;
		ComputingResource parent = compResources;
		while(parent != null && resourceNotVisited){
			Map<ResourceUnitName, List<ResourceUnit>> resUnits = parent.getResourceCharacteristic().getResourceUnits();
			for(ResourceUnitName run : resUnits.keySet()){
				for(ResourceUnit resUnit : resUnits.get(run)){
					if((resourceUnits.get(run) == null)){
						list = new ArrayList<ResourceUnit>(1);
						resourceUnits.put(resUnit.getName(), list);
						list.add(resUnit);
					} else if(!resourceUnits.get(run).contains(resUnit)){
						list = resourceUnits.get(resUnit.getName());
						list.add(resUnit);
					} else {
						resourceNotVisited = false;
					}
				}
			}
			parent = parent.getParent();
		}
		return resourceUnits;
	}
	
	private void prepareCalendar() {
		long seed = 11L * 13L * 17L * 19L * 23L + 1L;
		double timeZone = 0.0;
		double peakLoad = 0.0; // the resource load during peak hour
		double offPeakLoad = 0.0; // the resource load during off-peak hr
		double holidayLoad = 0.0; // the resource load during holiday

		// incorporates weekends so the grid resource is on 7 days a week
		LinkedList<Integer> Weekends = new LinkedList<Integer>();
		Weekends.add(java.util.Calendar.SATURDAY);
		Weekends.add(java.util.Calendar.SUNDAY);

		// incorporates holidays. However, no holidays are set in this example
		LinkedList<Integer> Holidays = new LinkedList<Integer>();
		resourceCalendar = new ResourceCalendar(timeZone, peakLoad, offPeakLoad, holidayLoad, Weekends, Holidays,
				seed);
	}
	
	class ResourceTypeComparator implements Comparator<Initializable>{
		
		List<String> order;
		ResourceTypeComparator(List<String> order) {
			this.order = order;
		}
		public int compare(Initializable init1, Initializable init2) {
			String type1 = ((Resource)init1).getType().getName();
			String type2 = ((Resource)init2).getType().getName();
			if(order.indexOf(type1) > order.indexOf(type2))
				return -1;
			else if (order.indexOf(type1) < order.indexOf(type2))
				return 1;
			else return 0;
			
		}
	}
	
}
