package controller.workload.generator;

import java.util.ArrayList;
import java.util.HashMap;

public class Dependency {
	/**
	 * List of all dependency paths detected in workload.
	 */
	protected ArrayList<ArrayList<String>> idDep;
	
	/**
	 * Map, where key is workload node id and value is arithmetic expression.
	 */
	protected HashMap <String, String> idExpMap;
	
	
}
