package model.resources.units;



public class CpuSpeed extends AbstractResourceUnit{

	protected int total;
	protected int used;
	
	public CpuSpeed(CpuSpeed cs, int total, int used) {
		super(cs);
		this.total = total;
		this.used = used;
	}
	
	public CpuSpeed(CpuSpeed cs) {
		super(cs);
		this.total = cs.total;
		this.used = cs.used;
	}
	
	public CpuSpeed(int total, int used) {
		super(StandardResourceUnitName.CPUSPEED);
		this.total = total;
		this.used = used;
	}
	
	public CpuSpeed(String resId, int total, int used) {
		super(StandardResourceUnitName.CPUSPEED, resId);
		this.total = total;
		this.used = used;
	}
	
	public int getFreeAmount() {
		return this.total - this.used;
	}

	public int getUsedAmount(){
		return this.used;
	}
	
	public void setUsedAmount(int amount){
		this.used = amount;
	}
	
	public void setAmount(int amount){
		this.total = amount;
	}

	public int getAmount(){
		return this.total;
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
		if(o instanceof CpuSpeed){
			int free = total - used;
			if(free < o.getFreeAmount()) return -1;
			if(free > o.getFreeAmount()) return 1;
			return 0;
		} else {
			throw new IllegalArgumentException(o + " is not an instance of " +
											"memory resource unit.");
		}
	}

	public boolean equals(Object o){
		if(o instanceof CpuSpeed){
			return super.equals(o);
		} else {
			return false;
		}
	}

}
