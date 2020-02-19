package eduni.simjava.distributions;

import cern.jet.random.AbstractDistribution;


public abstract class AbstractSpecificGenerator implements Comparable<AbstractSpecificGenerator>{
	protected AbstractDistribution generator;
	
	protected double lastGeneraterdValue;
	
	protected boolean lastGenerated;
	
	public AbstractSpecificGenerator(AbstractDistribution generator){
		this.generator = generator;
		this.lastGeneraterdValue = generator.nextDouble();
	}
	
	public double getLastGeneratedValue(){
		return lastGeneraterdValue;
	}
	
	public void setLastGeneratedValue(double value){
		lastGeneraterdValue = value;
	}
	
	public AbstractDistribution getGenerator(){
		return generator;
	}
	
	public int compareTo(AbstractSpecificGenerator o){
		return -1;
	}

	public boolean getLastGenerated( ) {
		return lastGenerated;
	}
	
	public void setLastGenerated(boolean flag) {
		this.lastGenerated = flag;
	}
}
