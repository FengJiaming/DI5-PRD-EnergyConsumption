package model.resources.units;


public class ResourceUnitNameFactory {

	public static ResourceUnitName createResourceUnitName(String unitName){

		if(unitName.equals(StandardResourceUnitName.MEMORY.getName()))
			return StandardResourceUnitName.MEMORY;
		
		else if (unitName.equals(StandardResourceUnitName.STORAGE.getName()))
			return StandardResourceUnitName.STORAGE;
		
		else if (unitName.equals(StandardResourceUnitName.PE.getName()))
			return StandardResourceUnitName.PE;
		
		else if (unitName.equals(StandardResourceUnitName.CPUSPEED.getName()))
			return StandardResourceUnitName.CPUSPEED;
		
		else if (unitName.equals(StandardResourceUnitName.GPU.getName()))
			return StandardResourceUnitName.GPU;
		
		else if (unitName.equals(StandardResourceUnitName.APPLICATION.getName()))
			return StandardResourceUnitName.APPLICATION;
		
		else if (unitName.equals(StandardResourceUnitName.COST.getName()))
			return StandardResourceUnitName.COST;

		else return new UserResourceUnitName(unitName);
	}
}
