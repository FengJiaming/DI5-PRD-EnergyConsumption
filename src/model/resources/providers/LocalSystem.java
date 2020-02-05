package model.resources.providers;


public class LocalSystem extends ResourceProvider {

	public LocalSystem(String id, String administrationDomain, String location) {
		super(id, administrationDomain, location, "LocalSystem");
	}
	
	public LocalSystem(){
		super();
	}

	public void setProviderId(String id) {
		this.id = id;
	}

	public void setProviderLocation(String location) {
		this.location = location;
	}

	public void setAdministrationDomain(String administrationDoamain){
		this.administrationDomain = administrationDoamain;
	}
	
	public void setDescription(Object description){
		this.description = description;
	}

}
