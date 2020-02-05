package org.joda.time;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eduni.simjava.Sim_system;

/**
 * 
 * @author Marcin Krystek
 *
 */

public class VCMilisProvider extends DateTimeUtils.MillisProvider {

	private Log log = LogFactory.getLog(VCMilisProvider.class);
	
	// start time offset
	protected long startTime; 
	
	public VCMilisProvider(Calendar initTime){

		// cut milliseconds
		String msg = "Cut start time milliseconds " + initTime.getTime() + 
					" (" + initTime.getTimeInMillis() + ") to ";
		
		initTime.set(Calendar.MILLISECOND, 0);
		
		log.debug(msg + initTime.getTime() + 
				" (" + initTime.getTimeInMillis() + ")");
		 
		this.startTime = initTime.getTimeInMillis();
	}
	
	long getMillis() {
		
		long vc = Math.round(Sim_system.clock());
		//System.out.println("SIMJAVA TIME: "+Sim_system.clock());
		
		// change virtual seconds to milliseconds
		vc = vc * 1000;
		
		return this.startTime + vc;
	}

}
