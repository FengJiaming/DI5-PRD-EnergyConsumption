package model.scheduling.plugin.grid;

import model.StaticResourceLoadCalendar;
import gridsim.EventManager;

public enum ModuleType {

	RESOURCE_DISCOVERY("Resource_discovery", ResourceDiscovery.class),
	RESOURCE_CALENDAR("Resource calendar", StaticResourceLoadCalendar.class),
	EVENT_MANAGER("Event manager", EventManager.class);
	//DATA_MANAGER("Data Manager", DataManager.class),
	//AR_EXTENSION("ARExtension", ARExtension.class);

	
	private String name;
	private Class<?> _interface;
	
	private ModuleType(String name, Class<?> _class){
		this.name = name;
		this._interface = _class;
	}
	
	public String toString(){
		return this.name;
	}
	
	public boolean isValidFor(Module m){
		return (m == null ? false : _interface.isAssignableFrom(m.getClass()));
	}
}
