package model.scheduling.tasks;

public abstract class AbstractProcessesGroup {

	protected String groupId;
	
	protected AbstractProcessesGroup(){
	}
	
	protected AbstractProcessesGroup(String groupId){
		this.groupId = groupId;
	}
	
	public String getId(){
		return this.groupId;
	}
}
