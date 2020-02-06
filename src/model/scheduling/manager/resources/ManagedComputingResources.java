package model.scheduling.manager.resources;

import java.util.ArrayList;

import model.resources.ResourceType;
import model.resources.StandardResourceType;
import model.resources.computing.ComputingResource;


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
