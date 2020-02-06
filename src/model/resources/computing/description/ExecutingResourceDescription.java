package model.resources.computing.description;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import model.Parameters;
import model.resources.ResourceType;
import model.resources.units.ResourceUnit;
import model.resources.units.ResourceUnitName;

public class ExecutingResourceDescription extends AbstractResourceDescription {

	protected Map<ResourceUnitName, List<ResourceUnit>> resUnits;
	protected Parameters parameters;
	
	public ExecutingResourceDescription(ResourceType type) {
		super(type);
	}

	public Parameters getParameters() {
		return parameters;
	}
	
	public String getCompResourceParameterValue(String name){
		return getParameters().get(name).get(0).getContent();
	}
	
	public void addResourceUnit(ResourceUnit unit) {
		if (this.resUnits == null)
			this.resUnits = new HashMap<ResourceUnitName, List<ResourceUnit>>(1);
		List<ResourceUnit> list = null;
		if (this.resUnits.containsKey(unit.getName())) {
			list = this.resUnits.get(unit.getName());
		} else {
			list = new ArrayList<ResourceUnit>(1);
			this.resUnits.put(unit.getName(), list);
		}
		list.add(unit);
	}
	
	public ResourceUnit getResourceUnit(ResourceUnitName unitName) throws NoSuchFieldException {
		return getResourceUnitList(unitName).get(0);
	}

	public List<ResourceUnit> getResourceUnitList(ResourceUnitName unitName) throws NoSuchFieldException {
		if (resUnits.containsKey(unitName))
			return resUnits.get(unitName);
		else
			throw new NoSuchFieldException("Resource unit " + unitName + " is not available in resource " + this.id);
	}

	public Collection<ResourceUnit> getResourceUnit() {
		if (resUnits == null)
			return null;
		List<ResourceUnit> values = new ArrayList<ResourceUnit>();
		Collection<List<ResourceUnit>> lists = resUnits.values();
		Iterator<List<ResourceUnit>> itr = lists.iterator();

		while (itr.hasNext()) {
			List<ResourceUnit> list = itr.next();
			values.addAll(list);
		}

		return values;
	}

	public Map<ResourceUnitName, List<ResourceUnit>> getResourceUnits() {
		return resUnits;
	}
}
