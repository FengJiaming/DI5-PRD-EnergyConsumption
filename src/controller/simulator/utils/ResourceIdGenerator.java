package controller.simulator.utils;

import java.util.HashMap;

public class ResourceIdGenerator {
	
	private static HashMap<String, Integer> resourceCounter = new HashMap<String, Integer>();
	
	public static int getId(String key){

		int value = 0;
		if(resourceCounter.containsKey(key)){
			value = resourceCounter.get(key);
			value++;
		}
		resourceCounter.put(key, value);
		return value;
	}
}
