package controller.simulator.utils;


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;

import model.JobDescription;
import model.TaskDescription;

public class XsltTransformations {
	
	public static String INSTALLDIR = null;
	
	static {
		String key = "javax.xml.transform.TransformerFactory";
		String value = 
			//"org.apache.xalan.xsltc.trax.TransformerFactoryImpl";
			"org.apache.xalan.processor.TransformerFactoryImpl";
		Properties props = System.getProperties();
//		props.put(key, value);
//		System.setProperties(props);
		
		INSTALLDIR = System.getProperty("gssim.install.dir");
		if(INSTALLDIR != null)
			INSTALLDIR = INSTALLDIR+"/";
		else 
			INSTALLDIR = "";
	}
	
	
	public XsltTransformations() throws ParserConfigurationException, XPathExpressionException{
	}
	
	protected enum Transformers {
		EXTEND_JOB_DESC(INSTALLDIR+"simulator/xslt/ext_job_desc.xslt"),
		TASK2RESOURCE_REQUIREMENTS(INSTALLDIR+"simulator/xslt/task2resreq.xslt"),
		RESOURCE_REQUIREMENTS2RTG(INSTALLDIR+"simulator/xslt/resreq2rtg.xslt"),
		RTG2HOST_PARAMETERS(INSTALLDIR+"simulator/xslt/rtg2host_params.xslt"),
		HOST_PARAMETERS2RTG(INSTALLDIR+"simulator/xslt/host_params2rtg.xslt"),
		RTG2SCHEDULIING_PLAN(INSTALLDIR+"simulator/xslt/schedulingPlan.xslt"),
		EXTRACT_JOB_ID(INSTALLDIR+"simulator/xslt/extract_job_id.xslt"),
		JOB_POSTPROCESSING(INSTALLDIR+"simulator/xslt/job_postprocessing.xslt");
		
		private String fileName;
		private Transformer transformer;

		private Transformers(String arg){
			this.fileName = arg;
			this.transformer = null;
		}
		
		public Transformer geTransformer() throws TransformerConfigurationException{
			if(this.transformer == null){
				TransformerFactory tFactory = TransformerFactory.newInstance();
				StreamSource ss = new StreamSource(this.fileName);
				Templates translet = tFactory.newTemplates(ss);
				this.transformer = translet.newTransformer();
			}
			
			return this.transformer;
		}
	}
	
	public String extendJobDescription(String jobDesc){
		Transformer t = null;
		try {
			t = Transformers.EXTEND_JOB_DESC.geTransformer();
			return transform(t, jobDesc);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String taskToResourceRequirements(String task, String jobId, String userDN, org.joda.time.DateTime submitionTime){
		Transformer t = null;
		try {
			t = Transformers.TASK2RESOURCE_REQUIREMENTS.geTransformer();
			t.clearParameters();
			t.setParameter("JOB_ID", jobId);
			t.setParameter("USER_DN", userDN);
			
			org.exolab.castor.types.DateTime dt = new org.exolab.castor.types.DateTime(submitionTime.getMillis());
			short milis = 0;
			dt.setMilliSecond(milis);
			t.setParameter("SUBMISSION_TIME", dt);
			
			return transform(t, task);
			
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String resourceRequirementsToRTG(String resReq){
		Transformer t = null;
		try {
			t = Transformers.RESOURCE_REQUIREMENTS2RTG.geTransformer();
			return transform(t, resReq);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String RTGToHostParameters(String rtg){
		Transformer t = null;
		try {
			t = Transformers.RTG2HOST_PARAMETERS.geTransformer();
			return transform(t, rtg);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String hostParametersToRTG(String hostParameters){
		Transformer t = null;
		try {
			t = Transformers.HOST_PARAMETERS2RTG.geTransformer();
			return transform(t, hostParameters);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String RTGToSchedulingPlan(String rtg){
		Transformer t = null;
		try {
			t = Transformers.RTG2SCHEDULIING_PLAN.geTransformer();
			return transform(t, rtg);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public JobDescription splitJobToTasks(String xmlJob) throws Exception{

		int begin = 0;
		int end = 0;
		
		begin = xmlJob.indexOf("appId=") + 7;
		end = xmlJob.indexOf("\"", begin);
		String jobId = xmlJob.substring(begin, end);
		
		JobDescription jobDesc = new JobDescription(xmlJob);
	    jobDesc.setJobId(jobId);
		
		begin = 0;
		end = 0;
		int tb = 0;
		int te = 0;
		String taskId;
		while((begin = xmlJob.indexOf("<task", end)) != -1){
			end = xmlJob.indexOf("</task>", begin);
			end += 7;
			String task = xmlJob.substring(begin, end);
			tb = task.indexOf("taskId=") + 8;
			te = task.indexOf("\"", tb);
			taskId = task.substring(tb, te);
			
			
			TaskDescription taskDescription = new TaskDescription(task);
	    	taskDescription.setTaskId(taskId);
	    	jobDesc.add(taskDescription);
		}
		
		return jobDesc;
	}
	
	public String extractJobId(String job){
		Transformer t = null;
		
		try {
		
			t = Transformers.EXTRACT_JOB_ID.geTransformer();
			return transform(t, job);
			
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} 
		
		return null;
	}
	
	public String jobPostProcessing(String job, String taskId, String executionDuration){
		Transformer t = null;
		
		try {
		
			t = Transformers.JOB_POSTPROCESSING.geTransformer();
			t.setParameter("TASK_ID", taskId);
			t.setParameter("EXEC_DURATION_VALUE", executionDuration);
			
			return transform(t, job);
			
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} 
		
		return null;
	}
	
	protected String transform(Transformer t, String input){
		Writer writer = new StringWriter();
		Reader reader = new StringReader(input);
		
		try {
			writer = new StringWriter();
			reader = new StringReader(input);
			
			t.transform(new StreamSource(reader), new StreamResult(writer));
			

			return writer.toString();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return null;
	}
}
