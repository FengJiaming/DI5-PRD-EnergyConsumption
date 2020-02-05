package schedframe.scheduling.manager.resources;

import java.util.ArrayList;

import schedframe.resources.ResourceType;
import schedframe.resources.StandardResourceType;
import schedframe.resources.computing.ComputingResource;

public class ManagedComputingResources extends ArrayList<ComputingResource>{
	
	private static final long serialVersionUID = 1L;

	private ResourceType type;
	
	public boolean add(ComputingResource compRes){
		if(type == null){
			type = compRes.getType();
		}
		if(type.getName().equals(compRes.getType().getName()))
			return super.add(compRes);
		return false;
	}

	public ResourceType getType() {
		if (type == null)
			return StandardResourceType.Undefined;
		return type;
	}

}
