package controller.workload.loader;

import org.qcg.broker.schemas.jobdesc.QcgJob;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import model.JobDescription;
import model.TaskDescription;
import model.exceptions.NoSuchCommentException;
import controller.simulator.utils.XsltTransformations;
import controller.workload.SWFFields;

public class WorkloadLoader {

	private static Log log = LogFactory.getLog(WorkloadLoader.class);

	protected QcgSWFJobReader waReader;
	protected SWFParser localWAParser;
	protected int generatedJobsCnt;
	protected int generatedTasksCnt;

	protected Date simulationStartTime;
	protected boolean simStartTimeDefined = true;

	protected XsltTransformations xsltTransformation = null;
	protected Map<String, JobDescription> jobGridletsMap;

	public WorkloadLoader(QcgSWFJobReader swfReader) {
		if (swfReader == null) {
			throw new RuntimeException("Swf reader is required to build proper gridlerts.");
		}
		this.waReader = swfReader;
		this.generatedJobsCnt = 0;
		this.generatedTasksCnt = 0;
		this.jobGridletsMap = new TreeMap<String, JobDescription>();
		try {
			this.xsltTransformation = new XsltTransformations();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	protected JobDescription createJobDescription(String jobDesc, long puSpeed) throws IOException {

		JobDescription jobDescription = null;
		try {
			jobDescription = this.xsltTransformation.splitJobToTasks(jobDesc);
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}

		// look for any

		if (jobDescription.size() == 0) {
			if (log.isWarnEnabled())
				log.warn("Omiting job gridlet creation for job " + jobDescription.getDescription().getAppId()
						+ ". This job contains no tasks.");
			return null;
		}

		// needed for Gridlet class

		String jobId = jobDescription.getJobId();

		for (int i = 0; i < jobDescription.size(); i++) {
			TaskDescription taskDesc = jobDescription.get(i);

			String waTaskDesc[] = localWAParser.readTask(jobId, taskDesc.getTaskId());
			if (waTaskDesc != null) {

				taskDesc.setUserDn(waTaskDesc[SWFFields.DATA_USER_ID]);

				long timeValue = Long.parseLong(waTaskDesc[SWFFields.DATA_SUBMIT_TIME]);
				taskDesc.setSubmissionTime(timeValue);

				long waitTime = Long.parseLong(waTaskDesc[SWFFields.DATA_WAIT_TIME]);
				taskDesc.setWorkloadLogWaitTime(waitTime);

				timeValue = Long.parseLong(waTaskDesc[SWFFields.DATA_RUN_TIME]);
				if (timeValue <= 0)
					return null;

				long allocProcesors = Long.parseLong(waTaskDesc[SWFFields.DATA_NUMBER_OF_ALLOCATED_PROCESSORS]);
				if (allocProcesors <= 0)
					return null;

				timeValue = timeValue * puSpeed * allocProcesors;
				taskDesc.setTaskLength(timeValue);

			}

			this.generatedTasksCnt++;
		}

		this.generatedJobsCnt++;

		return jobDescription;
	}

	public boolean load() throws IOException, MarshalException, ValidationException {

		// prepare local swf parser
		this.localWAParser = new SWFParser(this.waReader.getParser().getFileName());
		this.localWAParser.loadHeader();

		long puSpeed = getPUSpeed();
		this.simulationStartTime = getSimulationStartTimeValue();

		this.localWAParser.reset();

		// determine which reader should be used
		String jobDesc = null;
		JobDescription job = null;
		// use swf job reader. Job created by this reader does not require xslt
		// transformation
		while ((jobDesc = this.waReader.readRaw()) != null) {
			job = createJobDescription(jobDesc, puSpeed);
			if (job != null)
				this.jobGridletsMap.put(job.getJobId(), job);
		}

		this.localWAParser.close();

		return true;
	}

	protected long getPUSpeed() {
		long puSpeed = 1;
		try {
			puSpeed = Long.parseLong(this.localWAParser.getCommentValue(SWFFields.COMMENT_PUSPEED));
		} catch (NoSuchCommentException e1) {
			if (log.isWarnEnabled())
				log.warn("Assuming default processing unit speed = 1");
		}
		return puSpeed;
	}

	protected Date getSimulationStartTimeValue() {
		Date simulationStartTime = null;
		try {

			DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZ yyyy", Locale.ENGLISH);
			simulationStartTime = df.parse(this.localWAParser.getCommentValue(SWFFields.COMMENT_STARTTIME));
		} catch (NoSuchCommentException e) {
			simStartTimeDefined = false;
			simulationStartTime = new Date(0);
			if (log.isWarnEnabled())
				log.warn("Assuming default simulation start time - " + simulationStartTime);
		} catch (ParseException e) {
			simStartTimeDefined = false;
			simulationStartTime = new Date(0);
			if (log.isWarnEnabled())
				log.warn("Wrong format of simulation start time comment.\nAssuming default simulation start time - "
						+ simulationStartTime);
		}
		return simulationStartTime;
	}

	public int getJobCount() {
		return generatedJobsCnt;
	}

	public int getTaskCount() {
		return generatedTasksCnt;
	}

	public Date getSimulationStartTime() {
		return this.simulationStartTime;
	}

	public List<JobDescription> getJobs() {
		ArrayList<JobDescription> list = new ArrayList<JobDescription>(this.jobGridletsMap.values());
		return list;
	}

	public boolean isSimStartTimeDefined() {
		return simStartTimeDefined;
	}

}
