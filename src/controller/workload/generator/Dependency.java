package controller.workload.generator;

import java.util.ArrayList;
import java.util.HashMap;

import bsh.Interpreter;
import controller.simulator.utils.RandomNumbers;

public class Dependency {
	/**
	 * List of all dependency paths detected in workload.
	 */
	protected ArrayList<ArrayList<String>> idDep;
	
	/**
	 * Map, where key is workload node id and value is arithmetic expression.
	 */
	protected HashMap <String, String> idExpMap;
	
	public Dependency(){
		idDep = new ArrayList<ArrayList<String>>();
		idExpMap = new HashMap<String, String>();
	}
	
	public void addIdExpMap(String key, String value) {
			idExpMap.put(key, value);
	}
	
	public void addIdDep(String id, String refElement) {
		ArrayList <String> genOrder = new ArrayList<String>();
		genOrder.add(0, refElement);
		genOrder.add(0, id);
		idDep.add((ArrayList<String>)genOrder.clone());
	}
	
}
