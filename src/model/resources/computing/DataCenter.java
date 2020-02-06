package model.resources.computing;

import java.util.List;

import model.resources.StandardResourceType;
import model.resources.computing.description.ComputingResourceDescription;
import model.resources.computing.profiles.energy.EnergyExtension;
import model.resources.computing.profiles.energy.power.PowerInterfaceFactory;
import model.resources.computing.profiles.energy.power.ui.PowerInterface;

public class DataCenter extends ComputingResource{

	public DataCenter(ComputingResourceDescription resDesc) {
		super(resDesc);
		//extensionList.add(new EnergyExtension(this, resDesc.getPowerInterface(), resDesc.getEnergyEstimationPlugin()));
		//PowerInterface pi = PowerInterfaceFactory.createPowerInterface(this, resDesc.getPowerProfile());
		//accept(new EnergyExtension(pi, resDesc.getPowerProfile()));
	}
	
	@SuppressWarnings("unchecked")
	public List<ComputingNode> getComputingNodes(){
		return (List<ComputingNode>) getDescendantsByType(StandardResourceType.ComputingNode);
	}
	
	/*private void accept(EnergyExtension e){
		extensionList.add(e);
		e.setResource(this);
	}*/
}
