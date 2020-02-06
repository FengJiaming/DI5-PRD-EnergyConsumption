package model.resources.units;


public class ResourceUnitFactory {
	
	
	public static ResourceUnit createUnit(String unitName, String resId, int totalAmount, int usedAmount){
		if(unitName.equalsIgnoreCase(StandardResourceUnitName.CPU.getName()))
			return new PEUnit(resId, totalAmount, usedAmount);
		if(unitName.equalsIgnoreCase(StandardResourceUnitName.PE.getName()))
			return new PEUnit(resId, totalAmount, usedAmount);
		else if(unitName.equalsIgnoreCase(StandardResourceUnitName.MEMORY.getName()))
			 return new Memory(resId, totalAmount, usedAmount);
		else if(unitName.equalsIgnoreCase(StandardResourceUnitName.STORAGE.getName()))
			 return new Storage(resId, totalAmount, usedAmount);
		else if(unitName.equalsIgnoreCase(StandardResourceUnitName.GPU.getName()))
			 return new GPU(resId, totalAmount, usedAmount);
		else if (unitName.equalsIgnoreCase(StandardResourceUnitName.CPUSPEED.getName()))
			return new CpuSpeed(resId, totalAmount, usedAmount); 
		else if (unitName.equalsIgnoreCase(StandardResourceUnitName.COST.getName()))
			return new Cost(resId, totalAmount);
		else if (unitName.equalsIgnoreCase(StandardResourceUnitName.APPLICATION.getName()))
			return new Applications(resId); 
		else
			return new SimpleResourceUnit(ResourceUnitNameFactory.createResourceUnitName(unitName), resId, totalAmount, usedAmount); 
	}
	
	public static ResourceUnit createUnit(String unitName, int totalAmount, int usedAmount){
		if(unitName.equalsIgnoreCase(StandardResourceUnitName.CPU.getName()))
			return new PEUnit(totalAmount, usedAmount, 1);
		else if(unitName.equalsIgnoreCase(StandardResourceUnitName.MEMORY.getName()))
			 return new Memory(totalAmount, usedAmount);
		else if (unitName.equalsIgnoreCase(StandardResourceUnitName.CPUSPEED.getName()))
			return new CpuSpeed(totalAmount, usedAmount); 
		else
			return new SimpleResourceUnit(ResourceUnitNameFactory.createResourceUnitName(unitName), totalAmount, usedAmount); 
	}
	
	
	public static ResourceUnit createUnit(ResourceUnitName unitName, int totalAmount, int usedAmount){
		if(unitName.equals(StandardResourceUnitName.CPU))
			return new PEUnit(totalAmount, usedAmount, 1);
		else if(unitName.equals(StandardResourceUnitName.MEMORY))
			 return new Memory(totalAmount, usedAmount);
		else if (unitName.equals(StandardResourceUnitName.CPUSPEED))
			return new CpuSpeed(totalAmount, usedAmount); 
		else
			return new SimpleResourceUnit(unitName, totalAmount, usedAmount); 
	}
	
	/*public static ResourceUnit creteUnit(String resourceClass, int totalAmount, int usedAmount){
		
		switch(ResourceUnitClass.valueOf(resourceClass)){
			case CPU: return new Processors(totalAmount, usedAmount, 1);
			case MEMORY: return new Memory(totalAmount, usedAmount);
			case STORAGE: return new Storage(totalAmount, usedAmount);
		default:
			
			throw new IllegalArgumentException("Paramter " + resourceClass + " is not supported.");
		}
	}*/
}
