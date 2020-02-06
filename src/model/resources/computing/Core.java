package model.resources.computing;

import model.resources.computing.description.ComputingResourceDescription;
import model.resources.computing.profiles.energy.EnergyExtension;
import model.resources.computing.profiles.energy.power.PowerInterfaceFactory;
import model.resources.computing.profiles.energy.power.ui.PowerInterface;
import model.resources.units.CpuSpeed;
import model.resources.units.StandardResourceUnitName;

public class Core extends ComputingResource{
	

	public Core (ComputingResourceDescription resDesc) {
		super(resDesc);
		//PowerInterface pi = PowerInterfaceFactory.createPowerInterface(this, resDesc.getPowerProfile());
		//accept(new EnergyExtension(pi, resDesc.getPowerProfile()));	
	}

	public Processor getProcessor(){
		return (Processor)parent;
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

	public void initCharacteristics(ComputingResourceDescription resDesc){
		super.initCharacteristics(resDesc);
		try{
			resourceCharacteristic.addResourceUnit(new CpuSpeed(name, Integer.valueOf(resDesc.getCompResourceParameterValue("speed")) * 1, 0));
		} catch(Exception e){
			resourceCharacteristic.addResourceUnit(new CpuSpeed(name,  1, 0));
		}
	}

	/*private void accept(EnergyExtension e){
		extensionList.add(e);
		e.setResource(this);
	}*/
}

