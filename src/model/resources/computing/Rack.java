package model.resources.computing;

import java.util.List;

import model.resources.StandardResourceType;
import model.resources.computing.description.ComputingResourceDescription;
import model.resources.computing.profiles.energy.EnergyExtension;
import model.resources.computing.profiles.energy.power.PowerInterfaceFactory;
import model.resources.computing.profiles.energy.power.ui.PowerInterface;

public class Rack extends ComputingResource{
	

	public Rack (ComputingResourceDescription resDesc) {
		super(resDesc);
		//PowerInterface pi = PowerInterfaceFactory.createPowerInterface(this, resDesc.getPowerProfile());
		//accept(new EnergyExtension(pi, resDesc.getPowerProfile()));	
	}

	@SuppressWarnings("unchecked")
	public List<ComputingNode> getComputingNodes(){
		return (List<ComputingNode>) getDescendantsByType(StandardResourceType.ComputingNode);
	}
	
	@SuppressWarnings("unchecked")
	public List<Processor> getProcessors(){
		return (List<Processor>) getDescendantsByType(StandardResourceType.Processor);
	}
	
	/*private void accept(EnergyExtension e){
		extensionList.add(e);
		e.setResource(this);
	}*/
}
