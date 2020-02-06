package model.resources.units;



public class Memory extends AbstractResourceUnit {

	protected int total;
	protected int used;

	public Memory(Memory m, int total, int used) {
		super(m);
		this.total = total;
		this.used = used;
	}
	
	public Memory(Memory m) {
		super(m);
		this.total = m.total;
		this.used = m.used;
	}
	
	public Memory(int total, int used) {
		super(StandardResourceUnitName.MEMORY);
		this.total = total;
		this.used = used;
	}
	
	public Memory(String resId, int total, int used) {
		super(StandardResourceUnitName.MEMORY, resId);
		this.total = total;
		this.used = used;
	}

	public int getFreeAmount() {
		return this.total - this.used - this.getProvisioner().getPending();
	}

	public int getUsedAmount(){
		return this.used;
	}
	
	public void setUsedAmount(int amount){
		if(amount > this.total){
			throw new IllegalArgumentException(
					"Used amount can not be grather then total amount.");
		}
		this.used = amount;
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
		if(o instanceof Memory){
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
		if(o instanceof Memory){
			return super.equals(o);
		} else {
			return false;
		}
	}


}


