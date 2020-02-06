package model.resources.computing.profiles.energy;

public class MeasurementHistory {
	
	protected long timestamp;
	protected double value;

	public MeasurementHistory (long timestamp, double value){
		this.timestamp = timestamp;
		this.value = value;
	}
	
	public long getTimestamp() {
		return timestamp;
	}

	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
}
