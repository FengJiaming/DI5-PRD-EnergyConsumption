package model.resources.computing.description;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import controller.simulator.utils.InstanceFactory;
import controller.simulator.utils.ResourceIdGenerator;
import model.Parameter;
import model.Parameters;
import model.Property;
import model.resources.ResourceTypeFactory;
import model.resources.computing.location.Location;
import model.resources.computing.profiles.energy.power.PState;
import model.resources.computing.profiles.energy.power.PowerProfile;
import model.resources.computing.profiles.energy.power.PowerState;
import model.resources.computing.profiles.energy.power.PowerStateNameFactory;
import model.resources.computing.profiles.energy.power.Transition;
import model.resources.computing.profiles.energy.power.plugin.EnergyEstimationPlugin;
import model.resources.units.ResourceUnit;
import model.resources.units.ResourceUnitFactory;
import schemas.ComputingResource;
import schemas.PowerUsage;
import schemas.Profile;
import example.energy.DefaultEnergyEstimationPlugin;

public class ComputingResourceDescription extends ExecutingResourceDescription {

	//protected Map<ResourceUnitName, List<AbstractResourceUnit>> resUnits;
	protected PowerProfile powerProfile;
	protected Location location;
	protected String category;
	//protected Parameters parameters;

	public ComputingResourceDescription(ComputingResource computingResource) {

		super(ResourceTypeFactory.createResourceType(computingResource.getClazz()));
		this.category = computingResource.getType();
		
		initId(computingResource);

		if (computingResource.getComputingResourceTypeChoiceSequence() != null) {
			initResourceUnits(computingResource.getComputingResourceTypeChoiceSequence().getResourceUnit());
			try {
				if(System.getProperty("coolemall.resdesc") != null){
					schemas.EnergyEstimationPlugin eep = new schemas.EnergyEstimationPlugin();
					eep.setName(getEEP(createEEPQuery(computingResource), System.getProperty("coolemall.resdesc")));
					if(computingResource.getComputingResourceTypeChoiceSequence().getProfile() != null) {
						if(computingResource.getComputingResourceTypeChoiceSequence().getProfile().getPowerProfile() != null) {
							computingResource.getComputingResourceTypeChoiceSequence().getProfile().getPowerProfile().setEnergyEstimationPlugin(eep);
						}
					} else {
						schemas.Profile p = new schemas.Profile();
						computingResource.getComputingResourceTypeChoiceSequence().setProfile(p);
						schemas.PowerProfile pp = new schemas.PowerProfile();
						computingResource.getComputingResourceTypeChoiceSequence().getProfile().setPowerProfile(pp);
						computingResource.getComputingResourceTypeChoiceSequence().getProfile().getPowerProfile().setEnergyEstimationPlugin(eep);
					}
				}
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
			initProfiles(computingResource.getComputingResourceTypeChoiceSequence().getProfile());
			initLocation(computingResource.getComputingResourceTypeChoiceSequence().getLocation());
			this.parameters = extractParameters(computingResource.getComputingResourceTypeChoiceSequence().getParameter());
		}
	}

	private void initId(ComputingResource computingResource){
		this.id = computingResource.getName() != null ? computingResource.getName() : type.toString();
		if(computingResource.getCount() > 1 || computingResource.getName() == null){
			this.id = id + "_" + String.valueOf(ResourceIdGenerator.getId(type.toString()));
		}
	}
	
	private void initResourceUnits(schemas.ResourceUnit[] resourceUnitCharacteristics) {
		for (int i = 0; i < resourceUnitCharacteristics.length; i++) {
			schemas.ResourceUnit resourceUnitCharacteristic = resourceUnitCharacteristics[i];
			ResourceUnit resourceUnit = ResourceUnitFactory.createUnit(resourceUnitCharacteristic.getClazz(), this.id,
					 Double.valueOf(resourceUnitCharacteristic.getAmount().getContent()).intValue(), 0);
			Parameters params = extractParameters(resourceUnitCharacteristic.getParameter());
			resourceUnit.init(params);
			addResourceUnit(resourceUnit);
		}
	}

	private void initProfiles(Profile profile) {
		if (profile != null) {
			initPowerProfile(profile.getPowerProfile());
		}
	}
	
	private void initPowerProfile(schemas.PowerProfile powerProfileCharacteristic) {
		
		if (powerProfileCharacteristic != null) {
			EnergyEstimationPlugin energyEstimationPlugin = null;
			List<PowerState> powerStates = null;
			List<PState> pStates = null;
			if(powerProfileCharacteristic.getEnergyEstimationPlugin() != null){
				String energyEstimationPluginName = powerProfileCharacteristic.getEnergyEstimationPlugin().getName();
				if(energyEstimationPluginName != null) {
					energyEstimationPlugin = (EnergyEstimationPlugin) InstanceFactory.createInstance(
							energyEstimationPluginName, EnergyEstimationPlugin.class);			
				} else {
					energyEstimationPlugin = new DefaultEnergyEstimationPlugin();
				}
				Parameters params = extractParameters(powerProfileCharacteristic.getEnergyEstimationPlugin().getParameter());
				energyEstimationPlugin.init(params);
			}
			
			if(powerProfileCharacteristic.getPowerStates() != null) {
				powerStates = new ArrayList<PowerState>();
				int powerStateCount = powerProfileCharacteristic.getPowerStates().getPowerStateCount();
				for (int i = 0; i < powerStateCount ; i++) {
					schemas.PowerState ps = powerProfileCharacteristic.getPowerStates().getPowerState(i);
					List<Transition> transitions = new ArrayList<Transition>();
					int transitionCount = ps.getTransitionCount();
					for (int j = 0; j < transitionCount; j++) {
						schemas.Transition t = ps.getTransition(j);
						Transition transition = new Transition(PowerStateNameFactory.createPowerStateName(t.getTo()), t
								.getPowerUsage().getContent(), t.getTime().getContent());
						Parameters params = extractParameters(t.getParameter());
						transition.init(params);
						transitions.add(transition);
					}
					//CoolEmAll DEBB description case
					if(ps.getPowerUsage() == null){
						ps.setPowerUsage(new PowerUsage("0"));
					}
					PowerState powerState = new PowerState(PowerStateNameFactory.createPowerStateName(ps.getName()), ps
							.getPowerUsage().getContent(), transitions);
					Parameters params = extractParameters(ps.getParameter());
					powerState.init(params);
					powerStates.add(powerState);
				}
			}
			
			if(powerProfileCharacteristic.getParameter() != null){
				pStates = new ArrayList<PState>();
				int parameterCount = powerProfileCharacteristic.getParameterCount();
				for(int i = 0; i < parameterCount; i++){
					schemas.Parameter parameter = powerProfileCharacteristic.getParameter(i);
					if(parameter.getName().equals("pState")){
						PState.Builder builder = new PState.Builder();
						int propertyCount = parameter.getParameterTypeSequence().getPropertyCount();
						for(int j = 0; j < propertyCount; j++){
							schemas.Property property = parameter.getParameterTypeSequence().getProperty(j);
							if(property.getName().equals("name")){
								 builder = builder.name(property.getStringValueWithUnit(0).getContent());
							} else if (property.getName().equals("frequency")){
								 builder = builder.frequency(Double.valueOf(property.getStringValueWithUnit(0).getContent()));
							} else if (property.getName().equals("voltage")){
								builder = builder.voltage(Double.valueOf(property.getStringValueWithUnit(0).getContent()));
							} else if (property.getName().equals("powerUsage")){
								builder = builder.powerUsage(Double.valueOf(property.getStringValueWithUnit(0).getContent()));
							}
						}
						PState pState = builder.build();
						pStates.add(pState);
					}
				}	
			}
			this.powerProfile = new PowerProfile(energyEstimationPlugin, powerStates, pStates);
			Parameters params = extractParameters(powerProfileCharacteristic.getParameter());
			this.powerProfile.init(params);
		}
	}

	
	private void initLocation(schemas.Location l) {
		if (location != null) {
			this.location = new Location(l.getHorizontal(), l.getVertical(), l.getDepth());
			Parameters params = extractParameters(l.getParameter());
			this.location.init(params);
		}
	}


	/*private Properties initProperties(schemas.Parameter[] parameters){
		Properties prop = new Properties();
		
		for(int i = 0; i < parameters.length; i++){
			schemas.Parameter parameter = parameters[i];
			List values = new ArrayList();
			if(parameter.getParameterTypeSequence().getProperty() != null)
			{
				Map<String, List<StringValueWithUnit>> properties = new HashMap<String, List<StringValueWithUnit>>();
				List<StringValueWithUnit> propValues = new ArrayList<StringValueWithUnit>();
				for(int j = 0; j < parameter.getParameterTypeSequence().getPropertyCount(); j++){
					schemas.Property property = parameter.getParameterTypeSequence().getProperty(j);
					for(int k = 0; k < property.getStringValueWithUnitCount(); k++){
						propValues.add(property.getStringValueWithUnit(k));
					}
					properties.put(property.getName(), propValues);
				}
				values.add(properties);
			}else {
				for(int j = 0; j < parameter.getStringValueWithUnitCount(); j++){
					values.add(parameter.getStringValueWithUnit(j));
				}
			}
			prop.put(parameter.getName(), values);
		}
		return prop;
	}*/
	
	private Parameters extractParameters(schemas.Parameter[] parameters){
		
		Parameters params = null;
		
		if(parameters.length != 0)
			params = new Parameters();
		
		for(int i = 0; i < parameters.length; i++){
			schemas.Parameter parameter = parameters[i];
			Parameter param = new Parameter(parameter.getName());
			if(parameter.getParameterTypeSequence() != null && parameter.getParameterTypeSequence().getProperty() != null)
			{							
				int propertyCount = parameter.getParameterTypeSequence().getPropertyCount();
				for(int j = 0; j < propertyCount; j++){
					schemas.Property property = parameter.getParameterTypeSequence().getProperty(j);
					Property prop = new Property(property.getName());
					int stringValueWithUnitCount = property.getStringValueWithUnitCount();
					for(int k = 0; k < stringValueWithUnitCount; k++){
						prop.add(property.getStringValueWithUnit(k));
					}
					param.addProperty(prop);
				}
			} else {
				int stringValueWithUnitCount =  parameter.getStringValueWithUnitCount();
				for(int j = 0; j < stringValueWithUnitCount; j++){
					param.add(parameter.getStringValueWithUnit(j));
				}
			}
			params.put(parameter.getName(), param);
		}
		return params;
	}

	public PowerProfile getPowerProfile() {
		return powerProfile;
	}
	
	public Location getLocation() {
		return location;
	}

	public String getCategory() {
		return category;
	}


	/***COOLEMALL CASE RELATED***/

	private static ResourceBundle recsBundle;
	
	private ResourceBundle getRecsBundle(String fileName) throws FileNotFoundException, IOException{
		if(recsBundle == null){
			recsBundle = new PropertyResourceBundle(new FileInputStream(fileName));
		}
		return recsBundle;
	}
	
	protected String getEEP(String query, String fileName) throws FileNotFoundException, IOException{
		ResourceBundle recsBundle = getRecsBundle(fileName);
		return recsBundle.getString(query);
	}
	
	protected String createEEPQuery(ComputingResource compRes) {
		String query = compRes.getClazz() + "EEP";
		return query;
	}
}
