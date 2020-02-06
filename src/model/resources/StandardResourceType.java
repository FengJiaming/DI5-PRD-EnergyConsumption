package model.resources;


public enum StandardResourceType implements ResourceType {

	Grid,
	DataCenter,
	Rack,
	RECS,
	ComputingNode,
	Processor,
	Core,
	Undefined,
	ResourceProvider,
	GS,
	LS;

	public String getName() {
		return toString();
	}
	
}
