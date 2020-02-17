package eduni.simjava.distributions;

import java.util.Date;

import cern.jet.random.AbstractDistribution;

public class PeriodicGenerator extends AbstractSpecificGenerator{
	
	private long beginValidTime;
	private long endValidTime;
	private boolean useTime;
	
	public PeriodicGenerator(AbstractDistribution generator){
		super(generator);
		this.useTime = false;
	}
	
	public PeriodicGenerator(AbstractDistribution generator, Date beginValidTime, Date endValidTime){
		super(generator);
		this.beginValidTime = beginValidTime.getTime();
		this.endValidTime = endValidTime.getTime();
		useTime = true;
	}
	
	
	public boolean isValid(long time){
		if(!useTime) return true;
		
		if(time >= beginValidTime && time <= endValidTime)
			return true;
		else
			return false;
	}
	
	public boolean isValid(Date time){
		if(time == null)
			return false;
		
		return isValid(time.getTime());
	}
	
	public long getBeginValidTime_L(){
		return beginValidTime;
	}
	
	public long getEndValidTime_L(){
		return endValidTime;
	}
	
	public Date getBeginValidTime_D(){
		return new Date(beginValidTime);
	}
	
	public Date getEndValidTime_D(){
		return new Date(endValidTime);
	}
	
}
