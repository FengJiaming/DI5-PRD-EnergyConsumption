package controller.simulator.stats.implementation;

import controller.simulator.stats.implementation.out.StatsSerializer;

public interface StatsInterface {

	public Object serialize(StatsSerializer serializer);
	public String[] getHeaders();
	
}
