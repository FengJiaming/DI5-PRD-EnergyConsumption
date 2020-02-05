package model;

import java.io.StringReader;
import java.util.ArrayList;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLContext;
import org.qcg.broker.schemas.jobdesc.QcgJob;


public class JobDescription extends ArrayList<TaskDescription> implements DescriptionContainer<QcgJob>{

	private static final long serialVersionUID = 7853990656207447619L;
	protected static Unmarshaller unmarshaller;
	static {
		XMLContext context = new XMLContext();
		try {
			context.addClass(org.qcg.broker.schemas.jobdesc.QcgJob.class);
			unmarshaller = context.createUnmarshaller();
		} catch (ResolverException e) {
			e.printStackTrace();
			unmarshaller = null;
		}
	}
	
	protected String xml;
	protected QcgJob job;
	protected String jobId;
	
	public JobDescription(String xmlJobDesc) throws MarshalException, ValidationException{
		xml = xmlJobDesc;
	}
	
	public String getJobId(){
		return this.jobId;
	}
	
	public void setJobId(String id){
		this.jobId = id;
	}
	
	public void discardUnused(){
		job = null;
		xml = null;
		for(int i = 0; i < size(); i++){
			get(i).discardUnused();
		}
	}
	
	public QcgJob getDescription() {
		if(job == null){
			try {
				job = (QcgJob) unmarshaller.unmarshal(new StringReader(xml));
			} catch (MarshalException e) {
				e.printStackTrace();
			} catch (ValidationException e) {
				e.printStackTrace();
			}
		}
		return job;
	}

	public String getDocument() throws Exception {
		return xml;
	}

}
