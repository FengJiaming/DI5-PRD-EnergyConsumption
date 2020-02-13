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
	
	public String outputFolder = null;

	public int numberOfSimulations = 1; // default value
	
	public boolean createjobsstatistics = true;
	public boolean createsimulationstatistics = true;

	protected ConfigurationOptions() {
	}

	public static ConfigurationOptions getConfiguration(String resdescPath, String workloadPath) {

		ConfigurationOptions co = new ConfigurationOptions();

		co.resdescFileName = resdescPath;

		co.inputWorkloadFileName = workloadPath;
		
//		co.outputFolder = "../result";
		co.statsOutputSubfolderNameCreate ="/result/stats";
		co.createjobsstatistics = true;
		co.createsimulationstatistics = true;
		co.numberOfSimulations = 1;

		return co;
	}

}