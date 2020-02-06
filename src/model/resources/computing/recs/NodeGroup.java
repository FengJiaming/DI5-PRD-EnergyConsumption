package model.resources.computing.recs;

import java.util.List;

import model.resources.StandardResourceType;
import model.resources.UserResourceType;
import model.resources.computing.ComputingResource;
import model.resources.computing.Processor;
import model.resources.computing.description.ComputingResourceDescription;

public class NodeGroup extends ComputingResource{
	
	public NodeGroup(ComputingResourceDescription resDesc) {
		super(resDesc);
	}

	@SuppressWarnings("unchecked")
	public List<Node> getNodes(){
		return (List<Node>) getDescendantsByType(new UserResourceType("Node"));
	}
	
	@SuppressWarnings("unchecked")
	public List<Processor> getProcessors(){
		return (List<Processor>) getDescendantsByType(StandardResourceType.Processor);
	}

}
