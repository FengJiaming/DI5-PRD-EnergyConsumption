package model.scheduling.tasks.requirements;

import java.io.StringWriter;
import java.io.Writer;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.XMLContext;

import org.qcg.broker.schemas.resreqs.Task;

import model.scheduling.tasks.TaskInterface;

public class ResourceRequirements extends AbstractResourceRequirements<Task>{

	protected static Marshaller marshaller;
	static {
		XMLContext context = new XMLContext();
		try {
			context.addClass(org.qcg.broker.schemas.resreqs.Task.class);
			marshaller = context.createMarshaller();
		} catch (ResolverException e) {
			e.printStackTrace();
			marshaller = null;
		}
	}
	
	protected TaskInterface<?> task;
	
	public ResourceRequirements(Task task){
		this.task = new model.scheduling.tasks.Task(task);
		this.resourceRequirements = task;
	}
	
	public ResourceRequirements(TaskInterface<?> task){
		this.task = task;
		this.resourceRequirements = (Task) task.getDescription();
	}
	
	public double getParameterDoubleValue(ResourceParameterName parameterName)
			throws NoSuchFieldException, IllegalArgumentException {
		return task.getParameterDoubleValue(parameterName);
	}

	public String getParameterStringValue(ResourceParameterName parameterName)
			throws NoSuchFieldException, IllegalArgumentException {
		return task.getParameterStringValue(parameterName);
	}

	public Task getDescription() {
		return this.resourceRequirements;
	}

	public String getDocument() throws Exception {
		Writer w = new StringWriter();
		
		marshaller.marshal(this.resourceRequirements, w);

		return w.toString();
	}

}
