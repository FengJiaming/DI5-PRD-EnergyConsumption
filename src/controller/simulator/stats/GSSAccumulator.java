package controller.simulator.stats;

import java.io.PrintStream;

import controller.simulator.DataCenterWorkloadSimulator;
import gridsim.Accumulator;

public class GSSAccumulator extends Accumulator {

	public GSSAccumulator() {
		super();
	}

	public GSSAccumulator(Accumulator acc) {
		n_ = acc.getCount();
		mean_ = acc.getMean();
		sqrMean_ = acc.getStandardDeviation() + (mean_ * mean_);
		min_ = acc.getMin();
		max_ = acc.getMax();
		last_ = acc.getLast();
	}

	/**
	 * This method must be overloaded, since it returns the wrong value.
	 */
	public double getStandardDeviation() {
		return Math.sqrt(getVariance());
	}

	/**
	 * Calculates the variance of accumulated items
	 * 
	 * @return the Standard Deviation of accumulated items
	 * @pre $none
	 * @post $none
	 */
	public double getVariance() {
		return sqrMean_ - (mean_ * mean_);
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof GSSAccumulator))
			return false;

		GSSAccumulator otherAcc = (GSSAccumulator) obj;
		if (Double.compare(this.last_, otherAcc.last_) != 0)
			return false;
		if (Double.compare(this.max_, otherAcc.max_) != 0)
			return false;
		if (Double.compare(this.mean_, otherAcc.mean_) != 0)
			return false;
		if (Double.compare(this.min_, otherAcc.min_) != 0)
			return false;
		if (Double.compare(this.n_, otherAcc.n_) != 0)
			return false;
		if (Double.compare(this.sqrMean_, otherAcc.sqrMean_) != 0)
			return false;

		return true;
	}

	public String toString() {
		String accumulatorSeparator = ";";
		StringBuffer buffer = new StringBuffer(50);

		buffer.append(accumulatorSeparator);
		buffer.append(DataCenterWorkloadSimulator.DFAULT_NUMBER_FORMAT.format(this
				.getMean()));
		buffer.append(accumulatorSeparator);
		buffer.append(DataCenterWorkloadSimulator.DFAULT_NUMBER_FORMAT.format(this
				.getStandardDeviation()));
		buffer.append(accumulatorSeparator);
		buffer.append(DataCenterWorkloadSimulator.DFAULT_NUMBER_FORMAT.format(this
				.getVariance()));
		buffer.append(accumulatorSeparator);
		buffer.append(DataCenterWorkloadSimulator.DFAULT_NUMBER_FORMAT.format(this
				.getMin()));
		buffer.append(accumulatorSeparator);
		buffer.append(DataCenterWorkloadSimulator.DFAULT_NUMBER_FORMAT.format(this
				.getMax()));
		buffer.append(accumulatorSeparator);
		buffer.append(DataCenterWorkloadSimulator.DFAULT_NUMBER_FORMAT.format(this
				.getSum()));
		buffer.append(accumulatorSeparator);
		buffer.append(DataCenterWorkloadSimulator.DFAULT_NUMBER_FORMAT.format(this
				.getCount()));
		buffer.append(accumulatorSeparator);

		return buffer.toString();
	}

	public String toFormatedString() {

		String accumulatorSeparator = "; ";
		StringBuffer buffer = new StringBuffer(50);

		buffer.append(accumulatorSeparator);
		buffer.append(String.format("%16s",
				DataCenterWorkloadSimulator.DFAULT_NUMBER_FORMAT.format(this
						.getMean())));
		buffer.append(accumulatorSeparator);
		buffer.append(String.format("%16s",
				DataCenterWorkloadSimulator.DFAULT_NUMBER_FORMAT.format(this
						.getStandardDeviation())));
		buffer.append(accumulatorSeparator);
		buffer.append(String.format("%16s",
				DataCenterWorkloadSimulator.DFAULT_NUMBER_FORMAT.format(this
						.getVariance())));
		buffer.append(accumulatorSeparator);
		buffer.append(String.format("%16s",
				DataCenterWorkloadSimulator.DFAULT_NUMBER_FORMAT.format(this
						.getMin())));
		buffer.append(accumulatorSeparator);
		buffer.append(String.format("%16s",
				DataCenterWorkloadSimulator.DFAULT_NUMBER_FORMAT.format(this
						.getMax())));
		buffer.append(accumulatorSeparator);
		buffer.append(String.format("%16s",
				DataCenterWorkloadSimulator.DFAULT_NUMBER_FORMAT.format(this
						.getSum())));
		buffer.append(accumulatorSeparator);
		buffer.append(String.format("%16s",
				DataCenterWorkloadSimulator.DFAULT_NUMBER_FORMAT.format(this
						.getCount())));
		buffer.append(accumulatorSeparator);

		return buffer.toString();
	}
}
