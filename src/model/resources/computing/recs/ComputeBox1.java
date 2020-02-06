package model.resources.computing.recs;

import java.util.List;

import model.resources.StandardResourceType;
import model.resources.UserResourceType;
import model.resources.computing.ComputingNode;
import model.resources.computing.ComputingResource;
import model.resources.computing.Processor;
import model.resources.computing.description.ComputingResourceDescription;

public class ComputeBox1 extends ComputingResource{
	
	public ComputeBox1(ComputingResourceDescription resDesc) {
		super(resDesc);
	}

	@SuppressWarnings("unchecked")
	public List<NodeGroup> getNodesGroups(){
		return (List<NodeGroup>) getDescendantsByType(new UserResourceType("NodeGroup"));
	}
	
	@SuppressWarnings("unchecked")
	public List<ComputingNode> getNodes(){
		return (List<ComputingNode>) getDescendantsByType(new UserResourceType("Node"));
	}
	
	@SuppressWarnings("unchecked")
	public List<Processor> getProcessors(){
		return (List<Processor>) getDescendantsByType(StandardResourceType.Processor);
	}

}
