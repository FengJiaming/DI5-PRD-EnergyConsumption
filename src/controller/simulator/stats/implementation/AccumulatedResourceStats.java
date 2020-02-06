package controller.simulator.stats.implementation;

import controller.simulator.stats.GSSAccumulator;
import controller.simulator.stats.implementation.out.StatsSerializer;

public class AccumulatedResourceStats implements StatsInterface {

	// accumulated resource statistic
	protected GSSAccumulator resourceLoad;
	protected GSSAccumulator resourceReservationLoad;
	protected String resourceName;

	private String[] headers = { "Resource name", "Factor's name", "mean",
			"stdev", "variance", "minimum", "maximum", "sum", "count" };

	public AccumulatedResourceStats(String resourceName) {
		this.resourceName = resourceName;
		this.resourceLoad = new GSSAccumulator();
		this.resourceReservationLoad = new GSSAccumulator();
	}

	public String getResourceName() {
		return this.resourceName;
	}

	public void addResourceLoad(double load) {
		this.resourceLoad.add(load);
	}

	public void addReservationLoad(double load) {
		this.resourceReservationLoad.add(load);
	}

	public GSSAccumulator getResourceLoad() {
		return this.resourceLoad;
	}

	public GSSAccumulator getResourceReservationLoad() {
		return this.resourceReservationLoad;
	}

	public Object serialize(StatsSerializer serializer) {
		return serializer.visit(this);
	}

	public String[] getHeaders() {
		return headers;
	}

}
