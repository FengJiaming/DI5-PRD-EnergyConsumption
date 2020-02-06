package model;

import gridsim.ResourceCalendar;
import model.exceptions.ModuleException;
import model.scheduling.plugin.grid.Module;
import model.scheduling.plugin.grid.ModuleType;

import java.util.LinkedList;
import java.util.Properties;


public class StaticResourceLoadCalendar extends ResourceCalendar implements Module{
	
	private static long seed_; 
	private static double timeZone_;
	private static double peakLoad_;
	private static double offPeakLoad_;
	private static double holidayLoad_;
	private static LinkedList<Integer> Weekends_;
	private static LinkedList<Integer> Holidays_;
	
	static{
		seed_ = 11L * 13L * 17L * 19L * 23L + 1L; 
		timeZone_ = 0.0;
		peakLoad_ = 0.0; // the resource load during peak hour
		offPeakLoad_ = 0.0; // the resource load during off-peak hr
		holidayLoad_ = 0.0; // the resource load during holiday
		
		// incorporates weekends so the grid resource is on 7 days a week
		Weekends_ = new LinkedList<Integer>();
		Weekends_.add(java.util.Calendar.SATURDAY);
		Weekends_.add(java.util.Calendar.SUNDAY);

		// incorporates holidays. However, no holidays are set in this example
		Holidays_ = new LinkedList<Integer>();
	}


	public StaticResourceLoadCalendar(double timeZone, double peakLoad,
			double offPeakLoad, double relativeHolidayLoad,
			LinkedList weekendList, LinkedList holidayList, long seed) {
		super(timeZone, peakLoad, offPeakLoad, relativeHolidayLoad, weekendList,
				holidayList, seed);
		// TODO Auto-generated constructor stub
	}

	public StaticResourceLoadCalendar(){
		super(timeZone_, peakLoad_, offPeakLoad_, 
				holidayLoad_, Weekends_, Holidays_, seed_);
	}

	@Override
	public void init(Properties properties) throws ModuleException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() throws ModuleException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ModuleType getType() {
		return ModuleType.RESOURCE_CALENDAR;
	}



}
