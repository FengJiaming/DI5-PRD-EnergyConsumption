package model.resources.computing;

import java.util.List;
import java.util.Properties;

import model.resources.ResourceStatus;
import model.resources.StandardResourceType;
import model.resources.computing.description.ComputingResourceDescription;
import model.resources.computing.extensions.ExtensionType;
import model.resources.computing.profiles.energy.EnergyExtension;
import model.resources.computing.profiles.energy.power.PowerInterfaceFactory;
import model.resources.computing.profiles.energy.power.ui.PowerInterface;
import model.resources.computing.profiles.energy.power.ui.ProcessorPowerInterface;
import model.resources.computing.properties.CpuPropertiesBuilder;
import model.resources.computing.properties.PropertiesDirector;
import model.resources.units.CpuSpeed;
import model.resources.units.StandardResourceUnitName;

public class Processor extends ComputingResource{
	

	public Processor (ComputingResourceDescription resDesc) {
		super(resDesc);
		//extensionList.add(new EnergyExtension(this, resDesc.getPowerInterface(), resDesc.getEnergyEstimationPlugin()));
		//PowerInterface pi = PowerInterfaceFactory.createPowerInterface(this, resDesc.getPowerProfile());
		//accept(new EnergyExtension(pi, resDesc.getPowerProfile()));	
	}


	public ComputingNode getComputingNode(){
		return (ComputingNode)parent;
	}
	
	@SuppressWarnings("unchecked")
	public List<Core> getCores(){
		return (List<Core>) getDescendantsByType(StandardResourceType.Core);
	}
	
	@SuppressWarnings("unchecked")
	public List<Core> getFreeCores(){
		return (List<Core>) getDescendantsByTypeAndStatus(StandardResourceType.Core, ResourceStatus.FREE);
	}

	public int getMIPS(){
		int mips;
		try {
			mips = getSpeedUnit().getAmount();
		} catch (NoSuchFieldException e) {
			mips = 1;
		}
		return mips;
	}
	
	public int getAvailableMIPS(){
		int mips;
		try {
			mips = getSpeedUnit().getFreeAmount();
		} catch (NoSuchFieldException e) {
			mips = 1;
		}
		return mips;
	}

	private CpuSpeed getSpeedUnit() throws NoSuchFieldException{
		return (CpuSpeed) resourceCharacteristic.getResourceUnit(StandardResourceUnitName.CPUSPEED);
	}
	
	public ProcessorPowerInterface getPowerInterface(){
		if (extensionList != null) {
			if(extensionList.isExtensionAvailable(ExtensionType.ENERGY_EXTENSION)){
				EnergyExtension ee = (EnergyExtension)extensionList.getExtension(ExtensionType.ENERGY_EXTENSION);
				return (ProcessorPowerInterface)ee.getPowerInterface();
			}
		}
		return null;
	}
	
	public void initCharacteristics(ComputingResourceDescription resDesc){
		//resourceCharacteristic = new ResourceCharacteristics.Builder().resourceUnits().build();
		super.initCharacteristics(resDesc);
		try{
			resourceCharacteristic.addResourceUnit(new CpuSpeed(name, Integer.valueOf(resDesc.getCompResourceParameterValue("speed")) * 1, 0));
		} catch(Exception e){
			resourceCharacteristic.addResourceUnit(new CpuSpeed(name,  1, 0));
		}
	}
	
	public Properties getProperties(){
		PropertiesDirector propDirector = new PropertiesDirector();
		propDirector.setPropertiesBuilder(new CpuPropertiesBuilder());
		propDirector.constructProperties(this);
		return propDirector.getProperties();
	}

	/*private void accept(EnergyExtension e){
		extensionList.add(e);
		e.setResource(this);
	}*/
}

