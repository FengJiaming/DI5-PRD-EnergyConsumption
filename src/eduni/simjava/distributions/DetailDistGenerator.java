package eduni.simjava.distributions;

import cern.jet.random.AbstractDistribution;


public class DetailDistGenerator extends AbstractSpecificGenerator {
	
	/**
	 * interval length
	 */ 
	private double prcnt;
	
	public DetailDistGenerator(AbstractDistribution generator){
		super(generator);
		this.prcnt = 0;
	}
	
	public DetailDistGenerator(AbstractDistribution generator, double prcnt){
		super(generator);
		this.prcnt = prcnt;
	}

	/**
	 * Returns percentage interval length.
	 * @return
	 */
	public double getPrcnt(){
		return prcnt;
	}

	public int compareTo(AbstractSpecificGenerator o) {
		if(o instanceof DetailDistGenerator) {
			
			DetailDistGenerator ddg = (DetailDistGenerator) o;
			double anotherPrcnt = ddg.getPrcnt();
			return Double.compare(prcnt, anotherPrcnt);
			
		} else {
			return -1;
		}
		
	}
	
}
