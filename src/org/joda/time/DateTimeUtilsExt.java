package org.joda.time;

import java.lang.reflect.Field;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTimeUtils;


public class DateTimeUtilsExt extends DateTimeUtils {
	
	private static Log log = LogFactory.getLog(DateTimeUtilsExt.class);
	private static Calendar offsetTime;
	
	public static void initVirtualTimeAccess(Calendar startTime){
		offsetTime = startTime;
		try {
			Field privateStringField = DateTimeUtils.class.
										getDeclaredField("cMillisProvider");
			privateStringField.setAccessible(true);
			privateStringField.set(null, new VCMilisProvider(startTime));
			privateStringField.setAccessible(false);
			
			log.info("org.joda.time PACKAGE IS USING SIMJAVA VIRTUAL CLOCK " +
									"TO DETERMINE CURRENT TIME.");
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public static DateTime getDateTimeAt(double vtime){
		DateTime dt = new DateTime(offsetTime.getTimeInMillis());
		return dt.plusSeconds((int) vtime);
	}
	
	public static Calendar getOffsetTime(){
		return offsetTime;
	}
}
