package model.resources.units;

public enum ResourceUnitType {

	UNKNOWN("unknown", 0),
	CONTINUOUS_RESOURCE("continuous resource", 1),
	DISCRETE_RESOURCE("discrete resource", 2)
	;
	
	private final int iValue;
	private final String sValue;
	
	private ResourceUnitType(final String sValue, final int iValue){
		this.sValue = sValue;
		this.iValue = iValue;
	}
	
	public String toString(){
		return this.sValue;
	}
	
	public int toInt(){
		return this.iValue;
	}
}
