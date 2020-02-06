package model.resources.computing.profiles.energy.power;


public class PowerStateNameFactory {
	
	public static PowerStateName createPowerStateName (String powerStateName){
		
		if(powerStateName.equals(StandardPowerStateName.ON.getName()))
			return StandardPowerStateName.ON;
		
		else if(powerStateName.equals(StandardPowerStateName.OFF.getName()))
			return StandardPowerStateName.OFF;
		
		else if(powerStateName.equals(StandardPowerStateName.HIBERNATE.getName()))
			return StandardPowerStateName.HIBERNATE;
		
		else if(powerStateName.equals(StandardPowerStateName.SLEEP.getName()))
			return StandardPowerStateName.SLEEP;
		
		else if(powerStateName.equals(StandardPowerStateName.SUSPEND.getName()))
			return StandardPowerStateName.SUSPEND;
		
		else return new UserPowerStateName(powerStateName);
	}
}
