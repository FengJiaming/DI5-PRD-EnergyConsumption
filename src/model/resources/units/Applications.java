package model.resources.units;

import java.util.ArrayList;
import java.util.List;



public class Applications extends AbstractResourceUnit {

	
	protected List<String> applicationNames;
	
	public Applications(String resId){
		super(StandardResourceUnitName.APPLICATION, resId);
		
		this.resourceType = ResourceUnitType.DISCRETE_RESOURCE;
		this.applicationNames = new ArrayList<String>(2);
	}
	
	public Applications(String resId, List<String> names){
		super(StandardResourceUnitName.APPLICATION, resId);
		
		this.resourceType = ResourceUnitType.DISCRETE_RESOURCE;
		
		if(names == null)
			this.applicationNames = new ArrayList<String>(2);
		else
			this.applicationNames = names;
	}
	
	public void addApplication(String name){
		this.applicationNames.add(name);
	}
	
	public List<String> getApplicationNames(){
		return this.applicationNames;
	}
	
	public boolean containsApplication(String name){
		return this.applicationNames.contains(name);
	}
	
	public int getAmount() {
		return this.applicationNames.size();
	}

	public int getFreeAmount() {
		return this.getAmount();
	}

	public int getUsedAmount() {
		return 0;
	}

	public void setUsedAmount(int amount) {
	}

	public ResourceUnit toDiscrete() throws ClassNotFoundException {
		return this;
	}

	public int compareTo(ResourceUnit o) {
		return 0;
	}

	public String toString(){
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < this.applicationNames.size(); i++){
			buffer.append(this.applicationNames.get(i) + ", ");
		}
		
		return getName() + ": " + buffer.toString();
	}
}
