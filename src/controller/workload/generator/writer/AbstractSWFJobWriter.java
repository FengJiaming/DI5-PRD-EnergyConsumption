package controller.workload.generator.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controller.simulator.DataCenterWorkloadSimulator;


public abstract class AbstractSWFJobWriter<Job> implements SWFJobWriter<Job>{

	public static String COMMENT = ";";
	public static String FIELD_SEPARATOR = "\t\t";
	public static String IRRELEVANT = "-1";
	
	protected long submitTime;
	protected boolean useTaskMapping;
	protected Map<String, Long> tasksLength;
	protected ArrayList<String> taskMapping;
	protected Writer writer;
	
	protected AbstractSWFJobWriter(String dirName, String fileName) throws IOException{
		submitTime = 0;
		useTaskMapping = true;
		tasksLength = new HashMap<String, Long>();
		taskMapping = new ArrayList<String>();
		
		File dir = new File(dirName);
		if(!dir.exists()) {
			dir.mkdirs();
		} else if(!dir.isDirectory()){
			throw new IOException(dirName + " is not a directory.");
		}
		
		File swfFile = new File(dir, fileName);
		writer = new FileWriter(swfFile);
	}
	
	
	
	public void writeFieldDescriptionHeader() throws IOException {
		writer.write(COMMENT + "Automatically generated workload description by the WorkloadGenerator \n");
		writer.write(COMMENT + FIELD_SEPARATOR + "j|" + FIELD_SEPARATOR + "s|" + FIELD_SEPARATOR + "w|" + FIELD_SEPARATOR + "r|" + FIELD_SEPARATOR + "c|" + FIELD_SEPARATOR + "c|" + FIELD_SEPARATOR + "m|" + FIELD_SEPARATOR + "p|" + FIELD_SEPARATOR + "t|" + FIELD_SEPARATOR + "m|" + FIELD_SEPARATOR + "s|" + FIELD_SEPARATOR + "u|" + FIELD_SEPARATOR + "g|" + FIELD_SEPARATOR + "e|" + FIELD_SEPARATOR + "q|" + FIELD_SEPARATOR + "p|" + FIELD_SEPARATOR + "p|" + FIELD_SEPARATOR + "t|\n"+
					 COMMENT + FIELD_SEPARATOR + "o|" + FIELD_SEPARATOR + "u|" + FIELD_SEPARATOR + "a|" + FIELD_SEPARATOR + "u|" + FIELD_SEPARATOR + "p|" + FIELD_SEPARATOR + "p|" + FIELD_SEPARATOR + "e|" + FIELD_SEPARATOR + "r|" + FIELD_SEPARATOR + "i|" + FIELD_SEPARATOR + "e|" + FIELD_SEPARATOR + "t|" + FIELD_SEPARATOR + "i|" + FIELD_SEPARATOR + "i|" + FIELD_SEPARATOR + "x|" + FIELD_SEPARATOR + "u|" + FIELD_SEPARATOR + "a|" + FIELD_SEPARATOR + "r|" + FIELD_SEPARATOR + "h|\n"+
					 COMMENT + FIELD_SEPARATOR + "b|" + FIELD_SEPARATOR + "b|" + FIELD_SEPARATOR + "i|" + FIELD_SEPARATOR + "n|" + FIELD_SEPARATOR + "u|" + FIELD_SEPARATOR + "u|" + FIELD_SEPARATOR + "m|" + FIELD_SEPARATOR + "o|" + FIELD_SEPARATOR + "m|" + FIELD_SEPARATOR + "m|" + FIELD_SEPARATOR + "a|" + FIELD_SEPARATOR + "d|" + FIELD_SEPARATOR + "d|" + FIELD_SEPARATOR + "e|" + FIELD_SEPARATOR + "e|" + FIELD_SEPARATOR + "r|" + FIELD_SEPARATOR + "e|" + FIELD_SEPARATOR + "i|\n"+
					 COMMENT + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + "m|" + FIELD_SEPARATOR + "t|" + FIELD_SEPARATOR + "t|" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + "c|" + FIELD_SEPARATOR + "e|" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + "t|" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + "c|" + FIELD_SEPARATOR + "u|" + FIELD_SEPARATOR + "t|" + FIELD_SEPARATOR + "c|" + FIELD_SEPARATOR + "n|\n"+
					 COMMENT + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + "i|" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + "i|" + FIELD_SEPARATOR + "a|" + FIELD_SEPARATOR + "t|" + FIELD_SEPARATOR + "u|" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + "r|" + FIELD_SEPARATOR + "u|" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + "u|" + FIELD_SEPARATOR + "e|" + FIELD_SEPARATOR + "i|" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + "k|\n"+
					 COMMENT + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + "t|" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + "m|" + FIELD_SEPARATOR + "l|" + FIELD_SEPARATOR + "i|" + FIELD_SEPARATOR + "s|" + FIELD_SEPARATOR + "r|" + FIELD_SEPARATOR + "r|" + FIELD_SEPARATOR + "e|" + FIELD_SEPARATOR + "s|" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + "t|" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + "t|" + FIELD_SEPARATOR + "j|" + FIELD_SEPARATOR + " |\n"+
					 COMMENT + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + "e|" + FIELD_SEPARATOR + "l|" + FIELD_SEPARATOR + "m|" + FIELD_SEPARATOR + "e|" + FIELD_SEPARATOR + "e|" + FIELD_SEPARATOR + "e|" + FIELD_SEPARATOR + "q|" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + "a|" + FIELD_SEPARATOR + "n|" + FIELD_SEPARATOR + "i|" + FIELD_SEPARATOR + "o|" + FIELD_SEPARATOR + "t|\n"+
					 COMMENT + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + "o|" + FIELD_SEPARATOR + "e|" + FIELD_SEPARATOR + "d|" + FIELD_SEPARATOR + "q|" + FIELD_SEPARATOR + "q|" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + "b|" + FIELD_SEPARATOR + "r|" + FIELD_SEPARATOR + "o|" + FIELD_SEPARATOR + "b|" + FIELD_SEPARATOR + "i|\n"+
					 COMMENT + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + "c|" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + "l|" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + "n|" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + "m|\n"+
					 COMMENT + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + "e|" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + " |" + FIELD_SEPARATOR + "e|\n");
	}
	
	public void writeComment(String key, String value) throws IOException {
		writer.write(COMMENT+key+": "+value+"\n");
	}
	
	public void setJobSubmitTime(long submitTime) {
		this.submitTime = submitTime;
	}

	public void addTaskLength(String taskId, long taskLength) {
		this.tasksLength.put(taskId, taskLength);
	}

	public void setOverwrite(boolean overwrite) {
	}
	
	public void useTaskMapping(boolean use) {
		this.useTaskMapping = use;
	}	
	
	public void writeLine(String array[]) throws IOException{
		for (int i = 0; i < array.length; i++) {
			writer.write(FIELD_SEPARATOR + array[i]);
		}
		writer.write("\n");
	}
	
	public void close() throws IOException {
		if(useTaskMapping){
			writer.write(COMMENT+"IDMapping: swfID:jobID:taskID\n"+COMMENT+" ");
			for(int i = 1; i <= taskMapping.size(); i++){
				writer.write(taskMapping.get(i-1) + ", ");
				if(i %  10 == 0){
					writer.write("\n"+COMMENT + " ");
				}
			}
			writer.write("\n"+COMMENT+"IDMapping: end\n");
		}
		
		this.taskMapping.clear();
		this.tasksLength.clear();
		
		writer.close();
	}

}
