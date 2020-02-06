package model.resources.computing.description;


import java.util.ArrayList;
import java.util.List;

import model.resources.ResourceType;


public abstract class AbstractResourceDescription {

	protected String id;
	protected ResourceType type;
	protected List<AbstractResourceDescription> children;
	
	public AbstractResourceDescription(ResourceType type){
		this.type = type;
		this.children = null;
	}

	public List<AbstractResourceDescription> getChildren() {
		return children;
	}
	
	public void setChildren(List<AbstractResourceDescription> children) {
		this.children = children;
	}
	
	public void addChildren(AbstractResourceDescription child) {
		//child.setParent(this);
		if(children == null)
			children = new ArrayList<AbstractResourceDescription> (1);
		this.children.add(child);
	}

	public String getId(){
		return this.id;
	}

	public ResourceType getType() {
		return type;
	}
	
	/*protected AbstractResourceDescription parent;
	/public AbstractResourceDescription getParent() {
		return parent;
	}

	public void setParent(AbstractResourceDescription parent) {
		this.parent = parent;
	}*/
	
	/*public enum ResourceType {
		COMPUTING_RESOURCE,
		QUEUING_SYSTEM;
	}
	*/
	
}
