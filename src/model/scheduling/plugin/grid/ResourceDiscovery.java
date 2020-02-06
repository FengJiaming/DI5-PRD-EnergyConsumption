package model.scheduling.plugin.grid;

import java.util.List;

import model.resources.providers.ResourceProvider;
import model.scheduling.SchedulerDescription;
import model.scheduling.SecurityContext;
import model.scheduling.tasks.requirements.AbstractResourceRequirements;


/**
 * This interface provides access to information about resources
 *
 */
public interface ResourceDiscovery extends Module {

	/**
	 * 
	 * @return List of all administration domains in the system
	 */
	public List<String> getAdministrationDomains(SecurityContext secContext);
	
	/**
	 * 
	 * @return List of resource providers available in the system
	 */
	public List<ResourceProvider> getProviders(SecurityContext secContext);
	
	/**
	 * 
	 * @param admDomain
	 * @return List of resource providers available in particular administration
	 * domain.
	 */
	public List<ResourceProvider> getProviders(String admDomain, SecurityContext secContext);
	

	
	public List<ResourceProvider> getProviders(AbstractResourceRequirements<?> reqDesc,
												SecurityContext secContext);
	
	/**
	 * @return description of all resources. Order of the resources is not determined.
	 */
	public List<SchedulerDescription> getResources(SecurityContext secContext);

	/**
	 * @param reqDesc resource requirements
	 * @return description of all resources that meet resource requirements
	 */
	public List<SchedulerDescription> getResources(AbstractResourceRequirements<?> reqDesc, SecurityContext secContext);

	public List<SchedulerDescription> getResources();
	
}
