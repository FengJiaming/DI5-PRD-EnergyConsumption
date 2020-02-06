package model.resources.computing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Parameters;
import model.resources.computing.location.Location;
import model.resources.units.ResourceUnit;
import model.resources.units.ResourceUnitName;


public class ResourceCharacteristics /*extends Properties*/{

	private static final long serialVersionUID = 2719535186621622647L;

	protected Map<ResourceUnitName, List<ResourceUnit>> resUnits;
	protected Parameters parameters;
	protected Location location;

	/*public ResourceCharacteristics(Map<ResourceUnitName, List<AbstractResourceUnit>> resUnits){
		this.resUnits = resUnits;
	}

	public ResourceCharacteristics(){
		this.resUnits = null;
	}
	
	public ResourceCharacteristics(Map<ResourceUnitName, List<AbstractResourceUnit>> resUnits, Parameters parameters){
		this(resUnits);
	}*/
	
	public Map<ResourceUnitName, List<ResourceUnit>> getResourceUnits() {
		if(resUnits == null)
			return new HashMap<ResourceUnitName, List<ResourceUnit>>();
		return resUnits;
	}
	
	public ResourceUnit getResourceUnit(ResourceUnitName unitName) throws NoSuchFieldException{
		if(getResourceUnits().containsKey(unitName))
			return getResourceUnits().get(unitName).get(0);
		else throw new NoSuchFieldException("Resource unit " + unitName + 
				" is not available in this resource ");
	}
	
	public void addResourceUnit(ResourceUnit unit){ 
		if(resUnits == null){
			resUnits = new HashMap<ResourceUnitName, List<ResourceUnit>>(2);
		}
		List<ResourceUnit> list = null;
		if(resUnits.containsKey(unit.getName())){
			list = resUnits.get(unit.getName());
		} else {
			list = new ArrayList<ResourceUnit>(1);
			resUnits.put(unit.getName(), list);
		}
		list.add(unit);
	}
	
	
	public static class Builder {
		  
		protected Map<ResourceUnitName, List<ResourceUnit>> resUnits;
		protected Location location;
		protected Parameters parameters;
		
		public Builder location(Location loc){this.location = loc; return this; }
        public Builder parameters(Parameters params){this.parameters = params; return this; }
        public Builder resourceUnits(Map<ResourceUnitName, List<ResourceUnit>> units){this.resUnits = units; return this; }
        public Builder resourceUnits(){this.resUnits = new HashMap<ResourceUnitName, List<ResourceUnit>>(2); return this; }
        
        public ResourceCharacteristics build() {
            return new ResourceCharacteristics(this);
        }
	}
	
	private ResourceCharacteristics(Builder builder) {
		this.location = builder.location;
		this.parameters = builder.parameters;
		this.resUnits = builder.resUnits;
	}

	public Parameters getParameters() {
		if(parameters == null)
			return new Parameters();
		return parameters;
	}

	public Location getLocation() {
		return location;
	}

}
