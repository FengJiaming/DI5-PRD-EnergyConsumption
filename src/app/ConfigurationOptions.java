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
	public String resdescFilePath = null;
	
	public String debbPLXMLPath = null;
	
	public String dcwormsOutputFile = null;
	
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
	
	public boolean creatediagrams_gantt = true;
	
	public boolean creatediagrams_respowerusage = true;
	public double  creatediagrams_resources_scale = -1;

	public boolean isDebug = true;
	
	public String [] resForEnergyChart = {"ComputingNode","Processor"};;
	
	protected ConfigurationOptions() {
	}

	public static ConfigurationOptions getConfiguration(String debbPLXMLPath, String workloadPath, String policy, boolean isDebug) {

		ConfigurationOptions co = new ConfigurationOptions();

		co.debbPLXMLPath = debbPLXMLPath;
		
		String[] debbPLXMLPathArray = debbPLXMLPath.split("\\\\");
		int index= debbPLXMLPathArray.length;
		
		/*debug*/
		co.isDebug = isDebug;
		co.dcwormsOutputFile = debbPLXMLPathArray[index-1].replace("PLMXML", "DCWORMS");
		if(isDebug) {
			co.resdescFilePath = debbPLXMLPath;
		}
		else {
			co.resdescFilePath = "example/" + co.dcwormsOutputFile;			
		}
		
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