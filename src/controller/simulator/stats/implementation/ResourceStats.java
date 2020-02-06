package controller.simulator.stats.implementation;

import java.util.HashMap;
import java.util.Map;

import controller.simulator.stats.implementation.out.StatsSerializer;
import model.resources.computing.ComputingResource;


public class ResourceStats implements StatsInterface {

	// raw resource statistic
	protected String resourceName;
	protected int cpucnt;
	protected int cpuspeed;
	protected int memory;
	protected double queueLength;
	protected Map<String, Double> processorsLoad;
	protected Map<String, Double> processorsReservationLoad;

	private String[] headers = { "resourceName", "memory", "cpu", "cpu_speed",
			"queue_length", "cpu_load", "reservation_load" };

	public ResourceStats(ComputingResource compResource) {
		this.resourceName = compResource.getName();
		init();

	}

	public void init() {
		this.cpucnt = 0;
		this.cpuspeed = 0;
		this.memory = 0;
		this.queueLength = 0;
		this.processorsLoad = new HashMap<String, Double>();
		this.processorsReservationLoad = new HashMap<String, Double>();
	}

	public double getQueueLength() {
		return queueLength;
	}

	public void setQueueLength(double queueLength) {
		this.queueLength = queueLength;
	}

	public Map<String, Double> getPELoad() {
		return processorsLoad;
	}

	public Map<String, Double> getProcessorsReservationLoad() {
		return processorsReservationLoad;
	}

	public String getResourceName() {
		return resourceName;
	}

	public int getCpucnt() {
		return cpucnt;
	}

	public int getCpuspeed() {
		return cpuspeed;
	}

	public int getMemory() {
		return memory;
	}

	public Object serialize(StatsSerializer serializer) {
		return serializer.visit(this);
	}

	public String[] getHeaders() {
		return headers;
	}

}
