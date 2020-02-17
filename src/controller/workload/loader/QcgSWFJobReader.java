package controller.workload.loader;

import org.qcg.broker.schemas.jobdesc.ComputingResourceBaseTypeChoice;
import org.qcg.broker.schemas.jobdesc.ComputingResourceType;
import org.qcg.broker.schemas.jobdesc.ComputingResourceBaseTypeChoiceItem;
import org.qcg.broker.schemas.jobdesc.ComputingResourceParameterType;
import org.qcg.broker.schemas.jobdesc.ExecutionTimeType;
import org.qcg.broker.schemas.jobdesc.ParameterTypeChoice;
import org.qcg.broker.schemas.jobdesc.ParameterTypeChoiceItem;
import org.qcg.broker.schemas.jobdesc.QcgJob;
import org.qcg.broker.schemas.jobdesc.ResourceRequirementsType;
import org.qcg.broker.schemas.jobdesc.Value;
import org.qcg.broker.schemas.jobdesc.RequirementsType;
import org.qcg.broker.schemas.jobdesc.types.ComputingParameterName;

import controller.workload.SWFFields;
import model.exceptions.NoSuchCommentException;

import org.qcg.broker.schemas.jobdesc.Task;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.types.Duration;
import org.exolab.castor.xml.XMLContext;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.ValidationException;

public class QcgSWFJobReader {

	private Log log = LogFactory.getLog(QcgSWFJobReader.class);

	private String currntJobID;
	private SWFParser waParser = null;
	protected Marshaller marshaller;
	protected String taskData[];

	public QcgSWFJobReader(String fileName) throws NullPointerException, IOException {
		if (fileName.endsWith("swf"))
			this.waParser = new SWFParser(fileName);
		this.init();
		this.waParser.loadHeader();
		taskData = null;
	}

	public QcgSWFJobReader(SWFParser parser) {
		this.waParser = parser;
		this.init();
	}

	protected void init() {
		XMLContext context = new XMLContext();
		try {
			context.addPackage("org.qcg.broker.schemas.jobdesc.QcgJob");
			this.marshaller = context.createMarshaller();
		} catch (ResolverException e) {
			e.printStackTrace();
			this.marshaller = null;
		}
	}

	public void close() throws IOException {
		if (waParser != null)
			waParser.close();
	}

	public static synchronized Date getStartTime(String workloadFileName) throws IOException, NoSuchCommentException {
		Date date = null;
		SWFParser parser = new SWFParser(workloadFileName);
		parser.loadHeader();

		String s = parser.getCommentValue(SWFFields.COMMENT_STARTTIME);

		parser.close();
		try {
			SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZ yyyy", Locale.ENGLISH);
			date = df.parse(s);
		} catch (ParseException e) {
			throw new IOException(e.getMessage());
		}

		return date;
	}

	public void reset() throws IOException {
		if (waParser != null)
			waParser.reset();
	}

	public SWFParser getParser() {
		return this.waParser;
	}

	public QcgJob read() throws IOException {
		Task task = null;

		if (taskData == null) {
			taskData = waParser.readTask();
			if (taskData == null)
				return null;

			currntJobID = taskData[SWFFields.DATA_JOB_NUMBER];
		}

		QcgJob job = new QcgJob();
		String appID = this.waParser.getIDMapping(currntJobID)[0];
		job.setAppId(appID);

		while (taskData != null && this.waParser.getIDMapping(currntJobID)[0].equals(appID)) {

			task = createTask(taskData);
			if (task != null)
				job.addTask(task);
			taskData = waParser.readTask();
			if (taskData != null)
				currntJobID = taskData[SWFFields.DATA_JOB_NUMBER];
		}

		return job;
	}

	public String readRaw() throws IOException {
		QcgJob job = null;

		if ((job = read()) == null)
			return null;

		Writer w = new StringWriter();
		try {
			this.marshaller.setWriter(w);
			this.marshaller.marshal(job);
		} catch (MarshalException e) {
			new IOException(e.getMessage());
		} catch (ValidationException e) {
			new IOException(e.getMessage());
		}

		return w.toString();
	}

	public String getCurrentPosition() {
		return currntJobID;
	}

	protected Task createTask(String[] data) {
		RequirementsType requirements = null;
		ResourceRequirementsType taskReq = null;
		ComputingResourceType compResource = null;
		ComputingResourceBaseTypeChoice choice = null;
		ComputingResourceBaseTypeChoiceItem crItem = null;
		ComputingResourceParameterType param = null;

		Task task = null;
		long value = -1;

		task = new Task();
		requirements = new RequirementsType();
		taskReq = new ResourceRequirementsType();
		compResource = new ComputingResourceType();
		choice = new ComputingResourceBaseTypeChoice();

		compResource.setComputingResourceBaseTypeChoice(choice);
		taskReq.addComputingResource(compResource);
		requirements.setResourceRequirements(taskReq);
		task.setRequirements(requirements);

		for (int i = 0; i < SWFFields.DATA_FIELDS.length; i++) {
			try {

				value = Long.parseLong(data[SWFFields.DATA_FIELDS[i]]);

				switch (SWFFields.DATA_FIELDS[i]) {
				case SWFFields.DATA_NUMBER_OF_ALLOCATED_PROCESSORS:
					if (value <= 0) {
						if (log.isWarnEnabled())
							log.warn("Task " + data[0]
									+ " is omited. Number of allocated processors should be grather then 0.");
						return null;
					}
					break;
				case SWFFields.DATA_REQUESTED_NUMBER_OF_PROCESSORS:
					if (value <= 0) {
						if (log.isWarnEnabled())
							log.warn("Task " + data[0]
									+ " is omited. Number of requested processors should be grather then 0.");
						return null;
					}
					break;
				case SWFFields.DATA_RUN_TIME:
					if (value <= 0) {
						if (log.isWarnEnabled())
							log.warn("Task " + data[0] + " is omited. Task runtime should be grather then 0.");
						return null;
					}
					break;
				}

				if (value == -1)
					continue;

			} catch (NumberFormatException e) {
				if (SWFFields.DATA_FIELDS[i] != SWFFields.DATA_JOB_NUMBER
						&& SWFFields.DATA_FIELDS[i] != SWFFields.DATA_PRECEDING_JOB_NUMBER)
					continue;
			}

			switch (SWFFields.DATA_FIELDS[i]) {
			case SWFFields.DATA_JOB_NUMBER:
				currntJobID = data[SWFFields.DATA_FIELDS[i]];
				String tab[] = this.waParser.getIDMapping(currntJobID);
				task.setTaskId(tab[1]);
				break;

			case SWFFields.DATA_SUBMIT_TIME:
				/*
				 * This is information for simulator not for task description. See
				 * WorkloadLoader for details.
				 */
				break;

			case SWFFields.DATA_WAIT_TIME:
				break;

			case SWFFields.DATA_RUN_TIME:
				/*
				 * This is information for simulator not for task description. See
				 * WorkloadLoader for details.
				 */
				break;

			case SWFFields.DATA_NUMBER_OF_ALLOCATED_PROCESSORS:
				/*
				 * This is information for simulator not for task description. See
				 * WorkloadLoader for details.
				 */
				break;

			case SWFFields.DATA_AVERAGE_CPU_TIME_USED:
				break;

			case SWFFields.DATA_USED_MEMORY:
				/*
				 * This is information for simulator not for task description. See
				 * WorkloadLoader for details.
				 */
				break;

			case SWFFields.DATA_REQUESTED_NUMBER_OF_PROCESSORS:

				crItem = new ComputingResourceBaseTypeChoiceItem();
				param = new ComputingResourceParameterType();

				param.setName(ComputingParameterName.CPUCOUNT); {
				ParameterTypeChoice tpChoice = new ParameterTypeChoice();
				ParameterTypeChoiceItem item = new ParameterTypeChoiceItem();
				Value paramValue = new Value();

				paramValue.setContent(Long.valueOf(value).doubleValue());
				item.setValue(paramValue);
				tpChoice.addParameterTypeChoiceItem(item);
				param.setParameterTypeChoice(tpChoice);
			}
				crItem.setHostParameter(param);
				choice.addComputingResourceBaseTypeChoiceItem(crItem);

				break;

			case SWFFields.DATA_REQUESTED_TIME:
				ExecutionTimeType executionTime = new ExecutionTimeType();

				// requested time is expressed in swf file in seconds
				// multiply value * 1000 to get milliseconds
				Duration d = new Duration(value * 1000);

				executionTime.setExecutionDuration(d);
				task.setExecutionTime(executionTime);
				break;

			case SWFFields.DATA_REQUESTED_MEMORY:
				crItem = new ComputingResourceBaseTypeChoiceItem();
				param = new ComputingResourceParameterType();

				param.setName(ComputingParameterName.MEMORY); {
				ParameterTypeChoice tpChoice = new ParameterTypeChoice();
				ParameterTypeChoiceItem item = new ParameterTypeChoiceItem();
				Value paramValue = new Value();

				paramValue.setContent(Long.valueOf(value).doubleValue());
				item.setValue(paramValue);
				tpChoice.addParameterTypeChoiceItem(item);
				param.setParameterTypeChoice(tpChoice);
			}

				crItem.setHostParameter(param);
				choice.addComputingResourceBaseTypeChoiceItem(crItem);

				break;

			case SWFFields.DATA_STATUS:
				break;

			case SWFFields.DATA_USER_ID:
				break;

			case SWFFields.DATA_GROUP_ID:
				break;

			case SWFFields.DATA_EXECUTABLE_NUMBER:
				break;

			case SWFFields.DATA_QUEUE_NUMBER:
				break;

			case SWFFields.DATA_PARTITION_NUMBER:
				break;

			case SWFFields.DATA_PRECEDING_JOB_NUMBER:
				break;

			case SWFFields.DATA_THINK_TIME_FROM_PRECEDING_JOB:
				break;
			}
		}

		return task;
	}

}
