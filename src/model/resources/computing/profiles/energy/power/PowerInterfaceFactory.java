package model.resources.computing.profiles.energy.power;

import model.resources.StandardResourceType;
import model.resources.UserResourceType;
import model.resources.computing.ComputingResource;
import model.resources.computing.profiles.energy.power.ui.PowerInterface;

public class PowerInterfaceFactory {



	//new1
	/*public static PowerInterface createPowerProfile(ResourceType resourceType, List<PowerState> powerStates, List<PState> pStates, String eepn){

		PowerInterface powerProfile;

		switch(resourceType){
			case DataCenter: powerProfile = new DataCenterPowerInterfaceNew(eepn, powerStates); break;
			case ComputingNode: powerProfile = new ComputingNodePowerInterfaceNew(eepn, powerStates); break;
			case Processor: powerProfile = new CPUPowerInterfaceNew(eepn, powerStates, pStates); break;
			default:
				throw new IllegalArgumentException("ResourceType " + resourceType + " is not supported.");
		}

		return powerProfile;
	}*/
	
	public static PowerInterface createPowerInterface(ComputingResource resource, PowerProfile pp){
		if(pp == null)
			return null;
		PowerInterface powerInterface;

		if(resource.getType().getName().equals(StandardResourceType.DataCenter.getName()))
			powerInterface = new model.resources.computing.profiles.energy.power.ui.DataCenterPowerInterface(resource, pp);
		else if (resource.getType().getName().equals(StandardResourceType.ComputingNode.getName()))
			powerInterface = new model.resources.computing.profiles.energy.power.ui.ComputingNodePowerInterface(resource, pp);
		else if (resource.getType().getName().equals(StandardResourceType.Processor.getName()))
			powerInterface = new model.resources.computing.profiles.energy.power.ui.ProcessorPowerInterface(resource, pp);
		else if (resource.getType().getName().equals(new UserResourceType("Node").getName()))
			powerInterface = new model.resources.computing.profiles.energy.power.ui.ComputingNodePowerInterface(resource, pp);
		else 
			powerInterface = new model.resources.computing.profiles.energy.power.ui.ComputingResourcePowerInterface(resource, pp);

		/*switch(resource.getType()){
			case DataCenter: powerInterface = new test.rewolucja.energy.profile.implementation.alternative2.DataCenterPowerInterfaceNew(resource, pp); break;
			case ComputingNode: powerInterface = new test.rewolucja.energy.profile.implementation.alternative2.ComputingNodePowerInterfaceNew(resource, pp); break;
			case Processor: powerInterface = new test.rewolucja.energy.profile.implementation.alternative2.CPUPowerInterfaceNew(resource, pp); break;
			default:
				throw new IllegalArgumentException("ResourceType " + resource.getType() + " is not supported.");
		}*/

		return powerInterface;
	}
}
