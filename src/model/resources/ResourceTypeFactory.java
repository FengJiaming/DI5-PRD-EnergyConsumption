package model.resources;


public class ResourceTypeFactory {
	
	public static ResourceType createResourceType(String typeName){
		
		if(typeName.equals(StandardResourceType.Grid.getName()))
			return StandardResourceType.Grid;
		
		else if (typeName.equals(StandardResourceType.DataCenter.getName()))
			return StandardResourceType.DataCenter;
		
		else if (typeName.equals(StandardResourceType.Rack.getName()))
			return StandardResourceType.Rack;
		
		else if (typeName.equals(StandardResourceType.RECS.getName()))
			return StandardResourceType.RECS;
		
		else if (typeName.equals(StandardResourceType.ComputingNode.getName()))
			return StandardResourceType.ComputingNode;
		
		else if (typeName.equals(StandardResourceType.Processor.getName()))
			return StandardResourceType.Processor;
		
		else if (typeName.equals(StandardResourceType.Core.getName()))
			return StandardResourceType.Core;
		
		else if (typeName.equals(StandardResourceType.ResourceProvider.getName()))
			return StandardResourceType.ResourceProvider;
		
		else if (typeName.equals(StandardResourceType.GS.getName()))
			return StandardResourceType.GS;
		
		else if (typeName.equals(StandardResourceType.LS.getName()))
			return StandardResourceType.LS;
		
		else return new UserResourceType(typeName);
	}
}
