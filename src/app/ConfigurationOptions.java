package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class ConfigurationOptions {

	public String statsOutputSubfolderNameCreate = null;
	public String resdescFileName = null;

	public String inputWorkloadFileName = null;
	public String workloadDescFileName = "example/WorkloadConfig2.xml";
	public String outputFolder = null;
	public String schedulingplugin = null;
	/**
	 * the name of the .swf simulator.workload file into which the
	 * simulator.workload is to be written (it will be stored in the
	 * outputFolder folder)
	 */
	public String outputWorkloadFileName = "workload.swf";
	
	public int numberOfSimulations = 1; // default value
	
	public boolean createjobsstatistics = true;
	public boolean createsimulationstatistics = true;

	protected ConfigurationOptions() {
	}

	public static ConfigurationOptions getConfiguration(String resdescPath, String workloadPath, String policy) {

		ConfigurationOptions co = new ConfigurationOptions();

		co.resdescFileName = resdescPath;

		co.inputWorkloadFileName = workloadPath;
		
		co.schedulingplugin = policy;
//		co.outputFolder = "../result";
		co.statsOutputSubfolderNameCreate ="/result/stats";
		co.createjobsstatistics = true;
		co.createsimulationstatistics = true;
		co.numberOfSimulations = 1;

		return co;
	}
	
	public static ConfigurationOptions getConfiguration(String workloadfilename, String outputfolder) {

		ConfigurationOptions co = new ConfigurationOptions();

		co.outputWorkloadFileName = workloadfilename;
		
		co.outputFolder = outputfolder;
		
		co.statsOutputSubfolderNameCreate ="/result/stats";
		co.createjobsstatistics = true;
		co.createsimulationstatistics = true;
		co.numberOfSimulations = 1;

		return co;
	}
}