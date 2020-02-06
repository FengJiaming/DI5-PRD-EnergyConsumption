package model.resources.units;



public class Cost extends AbstractResourceUnit {
	
	protected int value;

	public Cost(Cost c) {
		super(c);
		this.value = c.value;
	}
	
	public Cost(int value) {
		super(StandardResourceUnitName.COST);
		this.value = value;
	}
	
	public Cost(String resId, int value) {
		super(StandardResourceUnitName.COST, resId);
		this.value = value;
	}

	public int getFreeAmount() {
		return 0;
	}

	public int getUsedAmount(){
		return 0;
	}
	
	public void setUsedAmount(int amount){
	}
	
	public int getAmount(){
		return this.value;
	}

	public ResourceUnit toDiscrete() throws ClassNotFoundException {
		throw new ClassNotFoundException("There is no distinguish class version for " 
										+ getClass().getName());
	}

	/**
	 * Comparing two Memory objects is equivalent to
	 * comparing the free amount of the resource. See {@link #getAmount()}
	 */
	public int compareTo(ResourceUnit o) {
		if(o instanceof Cost){
			if(this.value < o.getAmount()) return -1;
			if(this.value > o.getAmount()) return 1;
			return 0;
		} else {
			throw new IllegalArgumentException(o + " is not an instance of " +
											"memory resource unit.");
		}
	}

	public boolean equals(Object o){
		if(o instanceof Cost){
			return super.equals(o);
		} else {
			return false;
		}
	}
}
