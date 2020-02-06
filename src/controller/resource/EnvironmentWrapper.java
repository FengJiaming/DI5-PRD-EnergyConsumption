package controller.resource;

import java.util.ArrayList;

import org.qcg.broker.schemas.exception.UnknownParameter;

import model.Parameter;
import model.Parameters;
import model.Property;
import schemas.ComputingResource;
import schemas.ComputingResourceTemplate;
import schemas.ComputingResourceTypeChoiceSequence;
import schemas.Environment;
import schemas.Resources;
import schemas.Templates;
import schemas.TimeEstimationPlugin;

public class EnvironmentWrapper {

	Environment environment;

	public void wrap(Environment environment) {
		this.environment = environment;
	}

	public Templates getTemplates() throws UnknownParameter{
		if(environment == null)
			throw new UnknownParameter("Environment parameters are not defined.");
		return environment.getTemplates();
	}
	
	public ComputingResourceTemplate getTemplate(String templateId) throws UnknownParameter{
		Templates templates = getTemplates() ;
	
		if(templates == null) 
			return null;
		
		for (int i = 0; i < templates.getComputingResourceTemplateCount(); i++){
			schemas.ComputingResourceTemplate template = templates.getComputingResourceTemplate(i);
			if(template.getName().equals(templateId)){
				return template;
			}
		}
		return null;
	}
	
	public Resources getResources() throws UnknownParameter{
		if(environment == null)
			throw new UnknownParameter("Environment parameters are not defined.");
		return environment.getResources();
	}
	
	public ComputingResource[] getComputingResources() throws UnknownParameter{
		
		ArrayList<ComputingResource>tab = null;
		
		if(environment == null)
			throw new UnknownParameter("Environment parameters are not defined.");
		Resources resources = getResources();
		if(resources == null)
			return null;
		
		tab = new ArrayList<ComputingResource>();
		 
		for(int i = 0; i < resources.getComputingResourceCount(); i++){
			ComputingResource compRes = resources.getComputingResource(i);
			tab.add(compRes);
		}
		if(tab.size() == 0)
			return null;
		else
			return tab.toArray(new ComputingResource[0]);
	}
	
	public TimeEstimationPlugin getTimeEstimationPlugin() throws UnknownParameter{
		if(environment == null)
			throw new UnknownParameter("Environment parameters are not defined.");
		return environment.getTimeEstimationPlugin();
	}
	
	public boolean initWithCompResTemplate(ComputingResource compRes, ComputingResourceTemplate template){

		compRes.setClazz(template.getClazz());

		compRes.setComputingResourceTypeChoiceSequence(new ComputingResourceTypeChoiceSequence());
		compRes.getComputingResourceTypeChoiceSequence().setComputingResource(template.getComputingResource());
		compRes.getComputingResourceTypeChoiceSequence().setResourceUnit(template.getResourceUnit());
		compRes.getComputingResourceTypeChoiceSequence().setProfile(template.getProfile());
		compRes.getComputingResourceTypeChoiceSequence().setParameter(template.getParameter());
		
		return true;
	}
	
	public static Parameters extractParameters(schemas.Parameter[] parameters){
		
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

}
