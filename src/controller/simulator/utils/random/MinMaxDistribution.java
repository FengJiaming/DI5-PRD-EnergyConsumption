package controller.simulator.utils.random;

import cern.jet.random.AbstractDistribution;

/**
 * Represents a wrapper for a specific random distribution class. The purpose of this class is
 * to provide the random values, with a given distribution, but within a given range [min; max] - inclusive. 
 * Min may be called a lower bound, and max an upper bound.
 * @author Stanislaw Szczepanowski
 *
 */
public class MinMaxDistribution extends AbstractDistribution {

	/**
	 * The lower bound for the allowed random values (<code>null</code> if no bound was set)
	 */
	protected Double min;
	
	/**
	 * The upper bound for the allowed random values (<code>null</code> if no bound was set)
	 */
	protected Double max;
	
	/**
	 * The source distribution for random values
	 */
	protected AbstractDistribution source;
	
	/**
	 * MinMax constructor. 
	 * It constructs the class with a given original distribution and minimal and maximal values.
	 * @param origDistribution the original distribution, which will be the source for random numbers. It must not be <code>null</code>.
	 * @param min the minimal allowed value for the random numbers. If a <code>null</code> value is given, then no lower bound will be set.
	 * @param max the maximal allowed value for the random numbers. If a <code>null</code> value is given, then no upper bound will be set.
	 */
	public MinMaxDistribution(AbstractDistribution origDistribution, Double min, Double max) {
		source = origDistribution;
		
		if (min != null && max != null) {
			if (min > max)
				throw new IllegalArgumentException("ERROR: invalid parameters min > max");
		}
		
		this.min = min;
		this.max = max;
	}
	
	@Override
	public double nextDouble() {
		double value;
		
		for (;;) {
			value = source.nextDouble();
			
			if (min != null && Double.compare(value, min) < 0)
				continue;
			
			if (max != null && Double.compare(value, max) > 0)
				continue;

			break;
		}
		
		return value;
	}
}