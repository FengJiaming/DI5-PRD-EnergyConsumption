package model.resources.computing.extensions;

import model.resources.computing.profiles.energy.EnergyExtension;

public enum ExtensionType {

	ENERGY_EXTENSION("EnergyExtension", EnergyExtension.class);
	
	private String name;
	private Class<?> _interface;
	
	private ExtensionType(String name, Class<?> _class){
		this.name = name;
		this._interface = _class;
	}
	
	public String toString(){
		return this.name;
	}
	
	public boolean isValidFor(Extension m){
		return (m == null ? false : _interface.isAssignableFrom(m.getClass()));
	}
}