package model.resources.providers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class ResourceProvider {

	protected String administrationDomain;
	protected String id;
	protected String location;
	protected String type;
	protected Object description;
	protected String commonName;
	
	protected Map<String, String> properties;
	
	public ResourceProvider(String id, 
							String administrationDomain, 
							String location, 
							String type) {
		this.id = id;
		this.administrationDomain = administrationDomain;
	    this.location = location;
	    this.type = type;
	    this.description = null;
	    this.properties = null;
	}
	
	public ResourceProvider(){
		this.id = null;
		this.administrationDomain = null;
		this.location = null;
		this.description = null;
		this.type = null;
		this.properties = null;
	}
	
	public String getAdministrationDomain() {
		return this.administrationDomain;
	}

	public String getProviderId() {
		return this.id;
	}

	public String getProviderLocation() {
		return this.location;
	}
	
	public Object getDescription(){
		return this.description;
	}
	
	public String getType(){
		return this.type;
	}
	
	public String getCommonName(){
		return this.commonName;
	}
	
	public void setCommonName(String name){
		this.commonName = name;
	}
	
	public void addProperty(String key, String value){
		if(this.properties == null){
			this.properties = new HashMap<String, String>();
		}
		
		this.properties.put(key, value);
	}
	
	public String getProperty(String key){
		if(this.properties == null)
			return null;
		String value = this.properties.get(key);
		return value;
	}
	
	public Set<String> getPropertyKeys(){
		if(this.properties == null)
			return null;
		
		Set<String> keys = this.properties.keySet();
		
		return keys;
	}
	
	public boolean equals(Object r){
		if (r instanceof ResourceProvider == false) 
			return false;
		
		ResourceProvider resProvider = (ResourceProvider) r;
		
		if(type != null && !type.equals(resProvider.getType()))
			return false;
		
		if(administrationDomain != null && !administrationDomain.equals(resProvider.getAdministrationDomain()))
			return false;
		
		if(location != null && !location.equals(resProvider.getProviderLocation()))
			return false;
		
		if(id != null && !id.equals(resProvider.getProviderId()))
			return false;
		
		return true;
		
	}
	
	public int hashCode() {
		// 4 is for 4 letters in word null.
		int cnt = (this.id == null ? 4 : this.id.length());
		cnt += (this.administrationDomain == null ? 4 : this.administrationDomain.length());
		cnt += (this.location == null ? 4 : this.location.length());
		cnt += (this.type == null ? 4 : this.type.length());
		cnt += (this.commonName == null ? 4 : this.commonName.length());
		
		StringBuffer buffer = new StringBuffer(cnt);
		buffer.append(this.id);
		buffer.append(this.administrationDomain);
		buffer.append(this.location);
		buffer.append(this.type);
		buffer.append(this.commonName);
		String s = buffer.toString();
		return s.hashCode();
	}

	public String toString(){
		return id + " " + administrationDomain + " " + location + " " + commonName;
	}

	
}
