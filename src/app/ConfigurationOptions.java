package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * This aim of this class is to store all the configuration options passed to the program to steer its functionality. <br>
 * As a general contract of this class, all fields, that store a path name to a folder will always be terminated with {@link File#separator} string.
 * 
 * @author Stanislaw Szczepanowski
 * 
 */
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
		
		co.outputFolder = "../../result";

		co.statsOutputSubfolderNameCreate = "../../result/stats";
		co.createjobsstatistics = true;
		co.createsimulationstatistics = true;
		co.numberOfSimulations = 1;

		return co;
	}

}