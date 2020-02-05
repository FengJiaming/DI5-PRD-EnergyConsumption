package model.resources.units;



public class PEUnit extends AbstractResourceUnit {
	
	protected int total;
	protected int used;
	protected int peSpeed;
	
	public PEUnit(PEUnit pe, int total, int used) {
		super(pe);
		this.total = total;
		this.used = used;
		this.peSpeed = 1;
	}
	
	protected PEUnit(){
		super(StandardResourceUnitName.PE);
		this.peSpeed = 1;
	}
	
	public PEUnit(int total, int used, int avgSpeed){
		this();
		this.total = total;
		this.used = used;
		this.peSpeed = avgSpeed;
	}
	
	public PEUnit(String resId, int total, int used) {
		super(StandardResourceUnitName.PE, resId);
		this.total = total;
		this.used = used;
		this.peSpeed = 1;
	}
	
	public void setSpeed(int speed) {
		this.peSpeed = speed;
	}
	
	public int getSpeed(){
		return this.peSpeed;
	}
	
	public int getFreeAmount() {
		return this.total - this.used;
	}
	
	public int getUsedAmount(){
		return this.used;
	}

	public int getAmount(){
		return this.total;
	}
	
	public void setUsedAmount(int amount) {
		this.used = amount;
	}
	
	public void reset(){
		this.used = 0;
	}
	
	public ResourceUnit toDiscrete(){
		throw new UnsupportedOperationException();
	}
	
	public int compareTo(ResourceUnit o) {
		if(o instanceof PEUnit){
			PEUnit pe = (PEUnit) o;
			int p_power = pe.getAmount() * pe.getSpeed();
			int power = this.getAmount() * this.getSpeed();
			if(power < p_power) return -1;
			if(power > p_power) return 1;
			return 0;
		} else {
			throw new ClassCastException();
		}
	}
	
	public boolean equals(Object o){
		if(o instanceof PEUnit){
			if(!super.equals(o))
				return false;
			PEUnit pe = (PEUnit) o;
			if(pe.getSpeed() != this.getSpeed()) return false;
			return true;
		} else {
			return false;
		}
	}

	public PEUnit replicate (int amount){
		amount = Math.min(getAmount(), amount);
		PEUnit peUnit = new PEUnit(this, amount, amount);
		return peUnit;
	}
}
