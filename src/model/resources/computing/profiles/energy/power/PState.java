package model.resources.computing.profiles.energy.power;

public class PState {

	protected String name;
	protected double frequency;
	protected double voltage;
	protected double powerUsage;

	/*public PState(PStateType name, double frequency, double voltage, double power) {
		super();
		this.name = name;
		this.frequency = frequency;
		this.voltage = voltage;
		this.power = power;
	}*/
	
	public String getName() {
		return name;
	}
	public double getFrequency() {
		return frequency;
	}
	public double getVoltage() {
		return voltage;
	}
	public double getPower() {
		return powerUsage;
	}
	
	public static class Builder {
	  
		protected String name;
		protected double frequency;
		protected double voltage;
		protected double powerUsage;
		
		public Builder name(String name){this.name = name; return this; }
        public Builder frequency(double value){this.frequency = value; return this; }
        public Builder voltage(double value){this.voltage = value; return this; }
        public Builder powerUsage(double rate){this.powerUsage = rate; return this; }
        
        public PState build() {
            return new PState(this);
        }
	}
	
	private PState(Builder builder) {
		this.name = builder.name;
		this.frequency = builder.frequency;
		this.voltage = builder.voltage;
		this.powerUsage = builder.powerUsage;
	}
}
