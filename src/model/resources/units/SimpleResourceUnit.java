package model.resources.units;


public class SimpleResourceUnit extends AbstractResourceUnit {

	protected int total;
	protected int used;
	
	public SimpleResourceUnit(SimpleResourceUnit sru, int total, int used) {
		super(sru);
		this.total = total;
		this.used = used;
	}
	
	public SimpleResourceUnit(SimpleResourceUnit sru) {
		super(sru);
		this.total = sru.total;
		this.used = sru.used;
	}
	
	public SimpleResourceUnit(ResourceUnitName name, int total, int used) {
		super(name);
		this.total = total;
		this.used = used;
	}
	
	public SimpleResourceUnit(ResourceUnitName name, String resId, int total, int used) {
		super(name, resId);
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


}
