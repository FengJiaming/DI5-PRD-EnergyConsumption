package model.scheduling.tasks;

import org.qcg.broker.schemas.resreqs.Group;

public class ProcessesGroup extends AbstractProcessesGroup {

	public ProcessesGroup(Group group){
		super(group.getGroupId());
	}
	
}
