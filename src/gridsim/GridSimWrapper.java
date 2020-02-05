package gridsim;

import eduni.simjava.Sim_exception;
import eduni.simjava.Sim_system;
import gridsim.GridSim;
import gridsim.GridSimRandom;
import gridsim.GridSimShutdown;


import java.util.Calendar;

public class GridSimWrapper extends GridSim {

	protected GridSimWrapper(String name) throws Exception {
		super(name);

	}

	protected GridSimWrapper(String name, double baudRate) throws Exception {
		super(name, baudRate);
	}

	protected static void initCommonVariable(Calendar cal, boolean traceFlag, int numUser, String reportWriterName)
			throws Exception {
		// NOTE: the order for the below 3 lines are important
		Sim_system.initialise();
		Sim_system.set_trc_level(1);
		Sim_system.set_auto_trace(traceFlag);

		// Set the current Wall clock time as the starting time of simulation
		calendar_ = cal;
		if (cal == null) {
			calendar_ = Calendar.getInstance();
		}

		SimulationStartDate = calendar_.getTime();
		rand = new GridSimRandom();

		// creates a GridSimShutdown object
		GridSimShutdown shutdown = new GridSimShutdown("GridSimShutdown", numUser, reportWriterName);
		shutdownID_ = shutdown.get_id();
	}

    public static void startSimulation() throws NullPointerException
    {
        System.out.println("Starting GridSim version 4.0");
        try {
            Sim_system.run();
        }
        catch (Sim_exception e)
        {
            throw new NullPointerException("GridSim.startGridSimulation() :" +
                    " Error - you haven't initialized GridSim.");
        }
    }
    
    public static void setSeed(long seed){
    	Sim_system.set_seed(seed);
    }
    
    public static void setTraceSettings() {
		Sim_system.set_trace_detail(true, true, true);
		Sim_system.set_trace_level(Integer.MAX_VALUE);
    }
}
