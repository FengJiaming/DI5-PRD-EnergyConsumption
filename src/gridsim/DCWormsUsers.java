package gridsim;

import eduni.simjava.Sim_event;
import eduni.simjava.Sim_system;
import gridsim.GridSim;
import gridsim.GridSimTags;
import gridsim.IO_data;
import gridsim.net.InfoPacket;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import controller.simulator.utils.XsltTransformations;
import controller.workload.WorkloadLoader;
import qcg.shared.constants.BrokerConstants;
import model.JobDescription;
import model.TaskDescription;
import model.scheduling.tasks.Job;
import model.scheduling.tasks.JobInterface;
import model.scheduling.tasks.Task;
import model.scheduling.tasks.TaskInterface;

public class DCWormsUsers extends GridSim implements GenericUser {

	/**A job generator, which produces jobs and tasks. These jobs are then sent by this entity */
	protected WorkloadLoader workloadLoader;
	
	/** The name of the entity, to which the created tasks will be sent */
	protected String destName;
	
	/** Indicates, that all tasks that returned to this object are finished */
	protected boolean allTasksAreFinished;
	
	/** Stores the list of jobs, that have been returned to this object */
	protected List<JobInterface<?>> returnedJobs;
	protected Set<String> sendJobsIds;
	protected Set<String> returnedJobsIds;
	
	protected long submissionStartTime = Long.MAX_VALUE;
	
	protected XsltTransformations xsltTransformer;
	
	/**
	 * Indicates, that an error has occurred - it is used for debug purposes
	 */
	protected boolean error;
	
	private static Log log = LogFactory.getLog(DCWormsUsers.class);
	
	/**
	 * Constructs the users object with the given parameters
	 * @param name the name of the users entity (must be unique across all entities in the whole simulation)
	 * @param destinationName the name of the entity, to which the created tasks will be sent 
	 * @param jobGenerator the job generator, which produces jobs and tasks, that will be sent by this class
	 * @throws Exception if any occurs (see {@link GridSim#GridSim(String, double)})
	 */
	public DCWormsUsers(String name, String destinationName, WorkloadLoader workload) throws Exception {
		super(name, DCWormsConstants.DEFAULT_BAUD_RATE);
		this.workloadLoader = workload;
		destName = destinationName;
		allTasksAreFinished = true;
		error = false;
		
		sendJobsIds = new HashSet<String>();
		returnedJobsIds = new HashSet<String>();
		
		this.xsltTransformer = new XsltTransformations();
	}

	@Override
	public void body() {
		sendJobs();
		collectJobs();

		//GridSim dependent code for shutting down the simulation
		shutdownGridStatisticsEntity();
		terminateIOEntities();
		shutdownUserEntity();
	}

	/**
	 * Collects the jobs sent to broker(s)
	 */
	protected void collectJobs() {
		final int FACTOR = Math.min(10, workloadLoader.getTaskCount()); //the refresh rate of the gauge: at most 10 times
		final int denominator = workloadLoader.getTaskCount() / FACTOR;
		allTasksAreFinished = true;
		returnedJobs = new ArrayList<JobInterface<?>>();
		int counter = 0;
		int oldRemeinder = 0;
		boolean hundredPercent = false;
		
		Sim_event ev = new Sim_event();
		sim_get_next(ev);
		while (Sim_system.running()) {
			switch (ev.get_tag()) {
			case GridSimTags.END_OF_SIMULATION:
				//no action
				break;
			
			case GridSimTags.INFOPKT_SUBMIT:
				processPingRequest(ev);
				break;
				
			case GridSimTags.GRIDLET_RETURN:
				JobInterface<?> returnedJob = (JobInterface<?>) ev.get_data();
				
				String jobId = returnedJob.getId();

				
				if (returnedJobs.contains(returnedJob)) {
					if(log.isErrorEnabled())
						log.error("Received the same job twice (job ID " + jobId + ")");
					error = true;
					break;
				} 
				
				returnedJobs.add(returnedJob);
				returnedJobsIds.add(jobId);
				
				if(returnedJob.getStatus() == BrokerConstants.JOB_STATUS_FINISHED) {
					if(log.isDebugEnabled())
						log.debug("Received finished job " + jobId);
					
				} else {
					if(returnedJob.getStatus() == BrokerConstants.JOB_STATUS_CANCELED) {
						if(log.isWarnEnabled()){
							String str = "Warning! An uncomplished job (Job ID: "+jobId+") has returned to users. Job was canceled.";
							log.warn(str);
						}
					}
					
					allTasksAreFinished = false;
				}
				
				counter += returnedJob.getTaskCount();
				int remainder = (counter / denominator);
				if (remainder != oldRemeinder) {
					int gauge = ((counter * 100) / (workloadLoader.getTaskCount()));
					if(log.isInfoEnabled())
						log.info(gauge + "% ");
					
					oldRemeinder = remainder;
					if (gauge == 100)
						hundredPercent = true;
				}
			
				break;
			} 
			
			//if all the Gridlets have been collected
			if (counter == workloadLoader.getTaskCount()) {
				break;
			}
			
			sim_get_next(ev);
		}
		
		//if all the Gridlets have been collected
		if (counter == workloadLoader.getTaskCount()) {
			if (! hundredPercent) {
				if(log.isInfoEnabled())
					log.info("100%");
			}
			if(log.isInfoEnabled())
				log.info(get_name() + ": Received all "+workloadLoader.getJobCount()+" jobs and " + counter + " tasks");			
		} else {
			if(log.isErrorEnabled())
				log.error(get_name() + ": ERROR DID NOT RECEIVED all tasks - some tasks were not finished! (received "+counter+" of "+workloadLoader.getTaskCount()+")");
		}
		
		Iterator <String> itr = sendJobsIds.iterator();
		String jobId;
		if(log.isInfoEnabled()){
			log.info("Missing tasks: ");
			while(itr.hasNext()){
				jobId = itr.next();
				if(!returnedJobsIds.contains(jobId)){
					log.info(jobId + ", ");
				}
			}
		}
		
	}

	/**
	 * Sends jobs to broker entities
	 */
	protected void sendJobs() {
		
		Map<Long, List<Job>> jobTimes = new TreeMap<Long, List<Job>>();
		int destID = GridSim.getEntityId(destName);
		//destID=GridSim.getEntityId("BROKER@COMPUTING_GRID_0");
		List<JobDescription> jobs = workloadLoader.getJobs();
		//TaskRequirements taskReq = new TaskRequirementsImpl();
		//double values[] = null;

		for (JobDescription job : jobs) {
			long l_submissionTime = Long.MAX_VALUE;
			//pick the lowest submission time
			for (TaskDescription task : job) {
				if (task.getSubmissionTime() < l_submissionTime)
					l_submissionTime = task.getSubmissionTime();
			}

			//store the submission time expressed in seconds after the simulation start time
			long submissionTime = l_submissionTime;
			
			Job newJob = prepareJob(job, submissionTime);
			
			List<Job> list = jobTimes.get(submissionTime);
			if (list == null) {
				list = new ArrayList<Job>();
				jobTimes.put(submissionTime, list);
			}
			list.add(newJob);
		}
		for (Long submissionTime : jobTimes.keySet()) {
			
			if(submissionTime < submissionStartTime)
				submissionStartTime = submissionTime;

			List<Job> list = jobTimes.get(submissionTime);
			for(int i = 0; i < list.size(); i++){
				this.sendJobsIds.add(list.get(i).getId());
				send(destID, submissionTime, GridSimTags.GRIDLET_SUBMIT, list.get(i));
			}
			
			//send(output, submissionTime, GridSimTags.GRIDLET_SUBMIT, new IO_data(list, GssConstants.DEFAULT_GRIDLET_SIZE, destID));
			//send(destID, submissionTime, GridSimTags.GRIDLET_SUBMIT, list);
			
		}
		System.out.println("finished sending jobs");
	}
	
	public List<JobDescription> getAllSentJobs() {
		return (List<JobDescription>) workloadLoader.getJobs();
	}
	
	public List<TaskDescription> getAllSentTasks() {
		List<TaskDescription> result = new ArrayList<TaskDescription>();
		List<JobDescription> sentJobs = getAllSentJobs();
		for (JobDescription job : sentJobs) {
			result.addAll(job);
		}
		return result;
	}
	
	public List<JobInterface<?>> getAllReceivedJobs() {
		return returnedJobs;
	}

	public int getFinishedTasksCount() {
		int result = 0;
		for (JobInterface<?> job : returnedJobs) {
			for (TaskInterface<?> task: job.getTask()) {
				if(task.getStatus() == DCWormsTags.SUCCESS)
					result++;
			}
		}
		return result;
	}
	
	public String getUserName() {
		return get_name();
	}
	
	public boolean isError() {
		return error;
	}
	
	/**
	 * Performs action concerning a ping request to this entity
	 * @param ev the event object
	 */
	protected void processPingRequest(Sim_event ev) {
        InfoPacket pkt = (InfoPacket) ev.get_data();
        pkt.setTag(GridSimTags.INFOPKT_RETURN);
        pkt.setDestID(pkt.getSrcID());

		// sends back to the sender
		send(output, GridSimTags.SCHEDULE_NOW, GridSimTags.INFOPKT_RETURN,
				new IO_data(pkt, pkt.getSize(), pkt.getSrcID()));
    }
		
	protected Job prepareJob(JobDescription jobDescription, long submissionTime){

		Job newJob = new Job(jobDescription.getJobId());
		DateTime submitionTime = new DateTime();
        submitionTime = submitionTime.plusMillis((int)submissionTime*1000);
			try {
				
				// transform job description to resource requirements

				newJob.setSenderId(this.get_id());
				
				for (TaskDescription taskDescription : jobDescription) {
					
					String xmlResReq = this.xsltTransformer.taskToResourceRequirements(
										taskDescription.getDocument(),
										jobDescription.getJobId(),
										taskDescription.getUserDn(),
										submitionTime);

					Task newTask = new Task(xmlResReq);
					newTask.setSenderId(this.get_id());
					newTask.setStatus((int)BrokerConstants.TASK_STATUS_UNSUBMITTED);
					newTask.setLength(taskDescription.getTaskLength());
					newTask.setWorkloadLogWaitTime(taskDescription.getWorkloadLogWaitTime());
			//		newTask.setSubmissionTime(taskDescription.getSubmissionTime());
					
					newJob.add(newTask);
				}

				jobDescription.discardUnused();
				
			} catch (Exception e){
				log.error(e.getMessage());
				e.printStackTrace();
			}
		
		return newJob;
	}

	public long getSubmissionStartTime() {
		return submissionStartTime;
	}
	
	public boolean isSimStartTimeDefined(){
		return workloadLoader.isSimStartTimeDefined();
	}
}
