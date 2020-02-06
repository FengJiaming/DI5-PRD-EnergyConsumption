package model.resources.computing;

import java.util.List;
import java.util.Properties;


import model.resources.ResourceStatus;
import model.resources.StandardResourceType;
import model.resources.computing.description.ComputingResourceDescription;
import model.resources.computing.extensions.ExtensionType;
import model.resources.computing.profiles.energy.EnergyExtension;
import model.resources.computing.profiles.energy.power.PowerInterfaceFactory;
import model.resources.computing.profiles.energy.power.ui.ComputingNodePowerInterface;
import model.resources.computing.profiles.energy.power.ui.PowerInterface;
import model.resources.computing.properties.ComputingNodePropertiesBuilder;
import model.resources.computing.properties.PropertiesDirector;
import model.resources.units.Cost;
import model.resources.units.Memory;
import model.resources.units.StandardResourceUnitName;

public class ComputingNode extends ComputingResource{
	

	public ComputingNode (ComputingResourceDescription resDesc) {
		super(resDesc);
		
		//extensionList.add(new EnergyExtension(this, resDesc.getPowerInterface(), resDesc.getEnergyEstimationPlugin()));
		//PowerInterface pi = PowerInterfaceFactory.createPowerInterface(this, resDesc.getPowerProfile());
		//AirThroughputInterface ai = AirThroughputInterfaceFactory.createAirThroughputInterface(this, resDesc.getAirThroughputProfile());
		//accept(new EnergyExtension(pi, resDesc.getPowerProfile()));
		//accept(new EnergyExtension(pi, resDesc.getPowerProfile(), ai, resDesc.getAirThroughputProfile()));	
	}
	
	/*public ComputingNode (String resourceName, ResourceCharacteristics resourceCharacteristic, Category cat, PowerInterface powerInterface) {
		super(resourceName, ResourceType.COMPUTING_NODE, resourceCharacteristic);
		category = cat;
		accept(powerInterface);
	//	extensionList.add(new EnergyExtension(this, "example.energy.ComputingNodeEnergyEstimationPlugin"));
	}*/
	
	
	public ComputingNodePowerInterface getPowerInterface(){
		ComputingNodePowerInterface powerInterface = null;
		if(extensionList.isExtensionAvailable(ExtensionType.ENERGY_EXTENSION)){
			EnergyExtension ee = (EnergyExtension)extensionList.getExtension(ExtensionType.ENERGY_EXTENSION);
			powerInterface = (ComputingNodePowerInterface)ee.getPowerInterface();
		}
		return powerInterface;
	}

	@SuppressWarnings("unchecked")
	public List<Processor> getProcessors(){
		return (List<Processor>) getDescendantsByType(StandardResourceType.Processor);
	}
	
	@SuppressWarnings("unchecked")
	public List<Processor> getFreeProcessors(){
		return (List<Processor>) getDescendantsByTypeAndStatus(StandardResourceType.Processor, ResourceStatus.FREE);
	}

	public int getProcessorsNumber() {
		return getProcessors().size();
	}

	public int getFreeProcessorsNumber() {
		return getFreeProcessors().size();
	}
	
	
	//MEMORY 
	public int getFreeMemory() throws NoSuchFieldException {
		return getMemory().getFreeAmount();
	}
	
	public int getTotalMemory() throws NoSuchFieldException {
		return getMemory().getAmount();
	}

	public boolean isMemRequirementSatisfied(int memoryReq) throws NoSuchFieldException {
		if (getFreeMemory() < memoryReq)
			return false;
		return true;
	}
	
	public Memory getMemory() throws NoSuchFieldException {
		return (Memory) resourceCharacteristic.getResourceUnit(StandardResourceUnitName.MEMORY);
	}
	
	//COST
	public int getProcessingCost() throws NoSuchFieldException {
		return getCost().getAmount();
	}
	
	private Cost getCost() throws NoSuchFieldException {
		return (Cost) resourceCharacteristic.getResourceUnit(StandardResourceUnitName.COST);
	}

	
	public Properties getProperties(){
		PropertiesDirector propDirector = new PropertiesDirector();
		propDirector.setPropertiesBuilder(new ComputingNodePropertiesBuilder());
		propDirector.constructProperties(this);
		return propDirector.getProperties();
	}
	
	
	/*private void accept(EnergyExtension e){
		extensionList.add(e);
		e.setResource(this);
	}*/
}
