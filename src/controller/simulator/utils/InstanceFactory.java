package controller.simulator.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class InstanceFactory {

	private static Log log = LogFactory.getLog(InstanceFactory.class);
	
	public static final Object createInstance(String className, Class<?> z){
		
		if(className == null)
			return null;
		
		Object obj = null;
		
		try {
			
			Class<?> c = Class.forName(className);
			if(!z.isAssignableFrom(c)){
				log.error(className + " does not implement desired interface " + z.getName());
				return null;
			}
			
			obj = c.newInstance();
			
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage() + " Class not found.");
		} catch (InstantiationException e) {
			log.error(e.getMessage() + " Can not create class instance.");
		} catch (IllegalAccessException e) {
			log.error(e.getMessage() + " Illegal Access.");
		}
		
		return obj;
		
	}
	
}
