package model.resources.units;


public class GPU extends AbstractResourceUnit {

	protected int total;
	protected int used;

	public GPU(GPU m) {
		super(m);
		this.total = m.total;
		this.used = m.used;
	}
	
	public GPU(int total, int used) {
		super(StandardResourceUnitName.GPU);
		this.total = total;
		this.used = used;
	}
	
	public GPU(String resId, int total, int used) {
		super(StandardResourceUnitName.GPU, resId);
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

	@Override
	public ResourceUnit toDiscrete() throws ClassNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	public int compareTo(ResourceUnit o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
