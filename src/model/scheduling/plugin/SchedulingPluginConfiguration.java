package model.scheduling.plugin;

import java.util.HashMap;
import java.util.Map;

import model.PluginConfiguration;
import model.events.scheduling.SchedulingEventType;

public class SchedulingPluginConfiguration implements PluginConfiguration {
	
	private Map<SchedulingEventType, Object> events;

	public SchedulingPluginConfiguration(){
		this.events = new HashMap<SchedulingEventType, Object>();
	}
	
	public void addServedEvent(SchedulingEventType eventType, Object argument){
		events.put(eventType, argument);
	}
	
	public Map<SchedulingEventType, Object> getServedEvents() {
		return events;
	}
}
