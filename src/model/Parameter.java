package model;

import java.util.ArrayList;
import java.util.List;

import schemas.StringValueWithUnit;

public class Parameter extends ArrayList<StringValueWithUnit> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String name;
	protected List<Property> properties;

	public Parameter(String name) {
		this.name = name;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public boolean addProperty(Property prop){
		if(properties == null){
			properties = new ArrayList<Property>();
		}
		return properties.add(prop);
	}
	
	public Property getProperty(String propName){
		for(Property property: properties){
			if(property.getName().equals(propName)){
				return property;
			}
		}
		return null;
	}
}
