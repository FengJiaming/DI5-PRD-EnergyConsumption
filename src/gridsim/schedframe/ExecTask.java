package gridsim.schedframe;

import java.util.List;

import model.scheduling.tasks.TaskInterface;
import model.scheduling.tasks.requirements.ResourceParameterName;

public interface ExecTask extends TaskInterface<org.qcg.broker.schemas.resreqs.Task> {

	public boolean expectSpecificResource(ResourceParameterName resourceName);	
	public Object getExpectedSpecificResource(ResourceParameterName resourceName);

	public void trackResource(String resName);
	public List<String> getVisitedResources();
}
