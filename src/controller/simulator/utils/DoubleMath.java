package controller.simulator.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

public class DoubleMath {
	
	/**
	 * The pattern used for converting a <code>double</code> value to a string
	 */
	public static String PATTERN = "###.###";
	
	/**
	 * A protected constructor which will be invoked only 
	 * one time in the lifetime of this class
	 */
	protected DoubleMath() {
	}
	
	/**
	 * Adds two <code>double</code> values and returns the result of addition
	 * @param left the left argument
	 * @param right the right argument
	 * @return the result of addition of the two arguments
	 */
	public static double add(double left, double right) {
		DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.ENGLISH);
		df.applyPattern(PATTERN);
		String leftStr = df.format(left);
		String rightStr = df.format(right);
		BigDecimal resultDec = new BigDecimal(leftStr);
		BigDecimal rightDec = new BigDecimal(rightStr);
		resultDec = resultDec.add(rightDec);
		return resultDec.doubleValue();
	}
	
	/**
	 * Subtracts from the <code>left</code> argument the <code>right</code> argument and returns the result of subtraction
	 * @param left the left argument
	 * @param right the right argument
	 * @return the result of subtraction of the two arguments
	 */
	public static double subtract(double left, double right) {
		DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.ENGLISH);
		df.applyPattern(PATTERN);
		String leftStr = df.format(left);
		String rightStr = df.format(right);
		BigDecimal resultDec = new BigDecimal(leftStr);
		BigDecimal rightDec = new BigDecimal(rightStr);
		resultDec = resultDec.subtract(rightDec);
		return resultDec.doubleValue();
	}

}
