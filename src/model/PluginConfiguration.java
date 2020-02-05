package model;

import java.util.Map;

import model.events.scheduling.SchedulingEventType;

public interface PluginConfiguration {
	
	/**
	 * SchedulingEventType - type of the event served by the plugin.
	 * Object - parameter of the event, if particular event requires one.
	 * @return
	 */
	public Map<SchedulingEventType, Object> getServedEvents();

}
