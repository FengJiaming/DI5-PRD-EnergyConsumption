package controller.simulator;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.joda.time.DateTimeUtilsExt;

import app.ConfigurationOptions;
import controller.ResourceController;
import controller.resource.ResourceReader;
import controller.simulator.stats.AccumulatedStatistics;
import controller.simulator.stats.implementation.DCWormsStatistics;
import controller.simulator.utils.LogErrStream;
import controller.workload.loader.QcgSWFJobReader;
import controller.workload.loader.WorkloadLoader;
import gridsim.DCWormsUsers;
import gridsim.GridSimWrapper;
import model.Initializable;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister64;

public class DataCenterWorkloadSimulator {

	protected static AccumulatedStatistics accumulatedStatistics;

	protected static int simulationRunNumber = 0;

	private Log log = LogFactory.getLog(DataCenterWorkloadSimulator.class);

	protected String statsOutputPath;
	
	public static int MAXIMUM_FRACTION_DIGITS = 3;
	
	public static NumberFormat DFAULT_NUMBER_FORMAT = NumberFormat
			.getInstance();
	static {
		DFAULT_NUMBER_FORMAT.setMaximumFractionDigits(MAXIMUM_FRACTION_DIGITS); // 0,001
	}
	public DataCenterWorkloadSimulator() {
	}

	public void run(ConfigurationOptions configurationOptions) {
		System.setErr(new PrintStream(new LogErrStream(log)));
		int noOfSimulations = 1; // default value is one

		if (log.isInfoEnabled())
			log.info(":: Starting simulation ::");
		System.out.println("Starting simulation");
		try {
			prepareDirecotry(configurationOptions);
		} catch (Exception e) {
			if (log.isErrorEnabled())
				log.error("FAILED to create the output path", e);
			return;
		}
		accumulatedStatistics = new AccumulatedStatistics(1);

		try {
			performSimulation("Simulation 1", configurationOptions); // run the simulation

		} catch (Exception e) {
			if (log.isErrorEnabled())
				log.error("ERROR in simulation run \""  + "\":", e);
			System.out.println("ERROR in simulation run :" + e.getMessage());
			e.printStackTrace();
		}

		if (log.isInfoEnabled())
			log.info("Done :: Simulation has finished " + noOfSimulations + " simulation runs ::");
		System.out.println("Simulation has finished");
		System.out.flush();
		System.err.flush();
	}


	private void performSimulation(String simulationIdentifier, ConfigurationOptions options) throws Exception {
		// Startup of the random number generators must be set up with a random
		// seed that is greater than zero
		// if seed is not given then all consecutive simulations are the same
		// if seed < 0 then the random numbers are equal to zero
		Uniform uniform = new Uniform(new MersenneTwister64(new Date()));
		// seed should be > 0 and fits to int size (which is also important)
		long seed = uniform.nextLongFromTo(1, Integer.MAX_VALUE);
		if (log.isDebugEnabled())
			log.debug("Shuffled initial seed: " + seed);

		assert seed > 0 && seed <= Integer.MAX_VALUE : "Initial seed is <= 0, what is improper";

		GridSimWrapper.setSeed(seed);
		GridSimWrapper.setTraceSettings();

		long startSimulation = System.currentTimeMillis();
		if (log.isInfoEnabled()) {
			log.info(":: Starting simulation run: \"" + simulationIdentifier + "\" ::");
			log.info(":: In the the mode of ");
		}

		WorkloadLoader workload = loadWorkload(options);

		// BEGIN: Initializing the GridSim:
		int numUser = 1; // the number of users in the experiment simulation
		// (default 1)

		Date date = workload.getSimulationStartTime();
		Calendar calendar = Calendar.getInstance();
		if (date == null)
			calendar.setTimeInMillis(0L);
		else
			calendar.setTime(date);

		boolean traceFlag = true; // means: trace GridSim events/activities
		String[] excludeFromFile = { "" }, excludeFromProcessing = { "" };

		GridSimWrapper.init(numUser, calendar, traceFlag, excludeFromFile, excludeFromProcessing, null);
		DateTimeUtilsExt.initVirtualTimeAccess(calendar);

		ResourceReader resourceReader = new ResourceReader(options);
		ResourceController rc = resourceReader.read();

		for (Initializable initObj : rc.getToInit()) {
			initObj.initiate();
		}
		rc.setInitList(null);

		DCWormsUsers wl = new DCWormsUsers("Users", rc.getScheduler().get_name(), workload);

		GridSimWrapper.startSimulation();
		long stopSimulation = System.currentTimeMillis();

		DCWormsStatistics stats = new DCWormsStatistics(simulationIdentifier, options, wl, statsOutputPath, rc);
		accumulatedStatistics.add(stats);
		if (log.isInfoEnabled())
			log.info("Generating statistics...");
		stats.generateStatistics();

		long duration = (stopSimulation - startSimulation) / 1000;
		if (log.isInfoEnabled())
			log.info("The simulation run took " + duration + " seconds");
		
		// if necessary generate gifs from sjvg - need to rewrite the SJVG
		// classes for public methods
		if (log.isInfoEnabled())
			log.info(":: Finished simulation run: \"" + simulationIdentifier + "\" ::");

		System.out.flush();
		System.err.flush();
	}

	private WorkloadLoader loadWorkload(ConfigurationOptions options) throws IOException, MarshalException, ValidationException {
		QcgSWFJobReader swfReader = null;

		String wlFileName = options.inputWorkloadFileName.trim().toLowerCase();

		swfReader = new QcgSWFJobReader(wlFileName);

		WorkloadLoader workload = new WorkloadLoader(swfReader);
		workload.load();

		return workload;
	}

	private File prepareDirecotry(ConfigurationOptions options) throws Exception {
		if (log.isInfoEnabled())
			log.info("READING SCENARIO ::");
		String prefix = null;
		if (options.inputWorkloadFileName != null) {
			prefix = new File(options.inputWorkloadFileName).getParent();
		} else {
			prefix = System.getProperty("user.dir");
		}
		statsOutputPath = prefix + File.separator + options.statsOutputSubfolderNameCreate;
		statsOutputPath += File.separator;

		// create the output folder
		File statsOutputPathFile = new File(statsOutputPath);
		if (!statsOutputPathFile.exists()) {
			if (!statsOutputPathFile.mkdirs())
				throw new IOException("Cannot create the output (stats) path: " + statsOutputPath + ". Cause: ");
		}

		File folder = new File(statsOutputPath);
		File fileList[] = folder.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			File tmpFile = fileList[i];
			String name = tmpFile.getName();
			if (name.length() > 4) {
				String subName = name.substring(0, 5);
				if (subName.compareTo("Chart") == 0 || subName.compareTo("Stats") == 0) {
					tmpFile.delete();
				}
			}
		}
		return statsOutputPathFile;
	}


	/**
	 * Returns the accumulated simulation statistics from the last run of the simulator
	 * 
	 * @return the accumulated simulation statistics from the last run of the simulator
	 */
	public static AccumulatedStatistics getAccumulatedStatistics() {
		return accumulatedStatistics;
	}

	public static int getSimulationRunNumber() {
		return simulationRunNumber;
	}

	protected class WorkloadDirectoryFilter implements java.io.FileFilter {

		public boolean accept(File file) {
			if (file.isDirectory() && file.getName().startsWith("workload"))
				return true;
			else
				return false;
		}
	}

	protected class PropertiesFileFilter implements java.io.FileFilter {

		public boolean accept(File file) {
			if (file.isFile() && file.getName().endsWith(".properties"))
				return true;
			else
				return false;
		}
	}

}