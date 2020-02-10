package controller.workload;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import model.exceptions.NoSuchCommentException;


public class SWFParser extends RandomAccessFile {

	private Log log = LogFactory.getLog(SWFParser.class);
	
	private static final long serialVersionUID = 1L;
	private String fileName;
	
	protected String COMMENT = ";";
	protected String FIELD_SEPARATOR = "\\s"; //white spaces

	protected HashMap<String, String> comments;
	protected HashMap<String, String[]> idMapping; // key - swf job id, value - value[0] xml job id, value[1] xml task id
	protected HashMap<String, String> reverseIdMapping; // key - xmlJobId_xmlTaskId, value - swf job id
	protected HashMap<String, Long> jobIndex;
	protected String fields[];
	protected int fieldsNo;
	protected boolean headerLoaded;
	protected boolean buildIndex;
	
	public SWFParser(String fileName) throws IOException, NullPointerException{
		super(fileName, "r");
		this.fileName = fileName;
		this.comments = new HashMap<String, String>();
		this.idMapping = new HashMap<String, String[]>();
		this.reverseIdMapping = new HashMap<String, String>();
		this.jobIndex = new HashMap<String, Long>();
		this.headerLoaded = false;
		this.buildIndex = true;
		this.fieldsNo = 18;
		this.COMMENT = ";";
	}

	public String[] readTask() throws IOException {
		String line = null;
		fields = null;
		
		while((line = readLine()) != null){
			line = line.trim();
			if(line.length() == 0 || line.startsWith(COMMENT)){
				continue;
			} else{
				fields = line.split(FIELD_SEPARATOR+"+");

				if(fields == null || fields.length != fieldsNo)
					throw new IOException("Some data fields are missing.");
				else
					break;
			}
				
		}
		return fields;
	}
	
		
	public String[] readTask(String jobID, String taskId) throws IOException{
		String key = jobID+"_"+taskId;
		String swfJobId;
		
		if(reverseIdMapping.containsKey(key)){
			swfJobId = reverseIdMapping.get(key);
		} else {
			swfJobId = jobID;
		}
		
		return readTask(swfJobId);
	}

	public String getCommentValue(String label) throws NoSuchCommentException {
		if(!comments.containsKey(label))
			throw new NoSuchCommentException("No comment value for "+label);
		
		return comments.get(label);
	}

	public String getDataField(int field) throws NoSuchFieldException {
		if(field >= fields.length)
			throw new NoSuchFieldException("No such field.");
		
		return fields[field];
	}

	/**
	 * Reads all comments that have appropriate format and place them in HashMap.
	 * Builds job index - decision depends on value of buildIndex variable. 
	 */
	public HashMap<String, String> loadHeader() throws IOException {
		String line = null;
		String label = null;
		String value = null;
		int idx = -1;
		
		
		// default speed for processing unit in millions of instructions.
		comments.put(SWFFields.COMMENT_PUSPEED, "1");
		
		long position = 0;
		while((position = super.getFilePointer()) != -1 && (line = readLine()) != null){
			line = line.trim();
			if(!line.startsWith(COMMENT)){
				if(buildIndex){
					buildIdx(line, position);
				}	
				continue;
			}
				
			idx = line.indexOf(":");
			if(idx == -1)
				continue;

			label = line.substring(1, idx);
			label = label.trim();

			if(idx + 1 > line.length())
				throw new IOException("No value for comment: "+label);
			
			value = line.substring(idx + 1, line.length());
			value = value.trim();
			
			if(label.equals(SWFFields.COMMENT_NOTE) && comments.containsKey(label)){
				value = comments.get(label)+"; "+value;
			} else if(label.equals(SWFFields.COMMENT_IDMAPPING)){
				loadIDMapping();
				continue;
			}

			comments.put(label, value);
		}
		reset();
		headerLoaded = true;
		return comments;
	}

	protected void loadIDMapping() throws IOException{
		String line = null;
		
		while((line = readLine()) != null){
			
			line = line.trim();
		
			if(!line.startsWith(COMMENT))
				continue;
			
			line = line.substring(1).trim(); // to cut beginning ';'
			if(line.startsWith(SWFFields.COMMENT_IDMAPPING+": end"))
				break;
			
			String idsInLine[] = line.split(",");
			String third = null;
			String ids[] = null;
			
			for(int i = 0; i < idsInLine.length; i++){
				third = idsInLine[i];
				ids = third.split(":");
				if(ids.length < 3){
					if(ids.length != 1)
						if(log.isWarnEnabled())
							log.warn(third + " is not complete. Use swfJobId:xmlJobId:xmlTaskId");
					continue;
				}
				String key = ids[0].trim();
				String value[] = new String[2];
				value[0] = ids[1].trim(); // job id
				value[1] = ids[2].trim(); // task id
				idMapping.put(key, value);
				reverseIdMapping.put(value[0]+"_"+value[1], key);
			}
			
		}
	}
	
	/**
	 * Sets comment sign. Default is ';'.
	 */
	public String setCommentSign(String comment) {
		String oldComment = this.COMMENT;
		this.COMMENT = comment;
		return oldComment;
	}

	/**
	 * Sets data fields separator. Default is tab (\t).
	 */
	public String setFieldSeparator(String fieldSeparator) {
		String oldSeparator = this.FIELD_SEPARATOR;
		this.FIELD_SEPARATOR = fieldSeparator;
		return oldSeparator;
	}
	
	/**
	 * Decide if task position index should be build. It is very useful
	 * and significantly speedup 
	 * @return previous value
	 */
	public boolean buildIndex(boolean build){
		boolean old = this.buildIndex;
		this.buildIndex = build;
		return old;
	}

	public void close() throws IOException {
		super.close();
		this.comments.clear();
		this.comments = null;
		this.idMapping.clear();
		this.idMapping = null;
		this.reverseIdMapping.clear();
		this.reverseIdMapping = null;
		this.jobIndex.clear();
		this.jobIndex = null;
	}

	/**
	 * Puts file pointer to the beginning of file.
	 */
	public void reset() throws IOException {
		super.seek(0);
	}
	
	public boolean isHeaderLoaded() {
		return headerLoaded;
	}
	
	public HashMap<String, String[]> getIDMapping(){
		return this.idMapping;
	}


	public String[] getIDMapping(String waID) {
		
		if(idMapping.containsKey(waID)){
			return this.idMapping.get(waID);
		}
		else {	// if mapping for job is not provided, then we assume that xmlJobId, xmlTaskId, swfJobId are equal.
			String tab[] = {waID, waID};
			return tab;
		}
	}
	
	
	public String getFileName(){
		return this.fileName;
	}
	
	public String[] readTask(String id) throws IOException{
		String fields[] = null;
		boolean found = false;
		
		
		Long jobPosition = this.jobIndex.get(id);
		
		// if index for job id is calculated, then use it!
		if(jobPosition != null){
			super.seek(jobPosition);
			fields = readTask();
			if(!id.equals(fields[0])) {
				log.error("Wrong index for task: " + id + ". Found task: " + fields[0]);
				return null;
			}
		} else {
			// do classic search, read file from current position to the end, move to the beginning
			// and continue to current position
			
			long filePointer = getFilePointer();
			// reads from current position to the end of file
			while(!found && (fields = readTask()) != null){
				if(fields[SWFFields.DATA_JOB_NUMBER].equals(id))
					found = true;
			}
			
			if(!found){
				// back to the beginning
				System.out.println("search from begin for " + id);
				super.seek(0);
				// reads from the beginning of file to current position
				while(!found && filePointer != getFilePointer() && (fields = readTask()) != null){
					if(fields[SWFFields.DATA_JOB_NUMBER].equals(id))
						found = true;
				}
			}
		
			if(found == false) return null;
		}
		
		return fields;
	}
	
	protected void buildIdx(String line, long position){
		if(line == null || line.length() == 0)
			return;
		
		String fields[] = line.split(FIELD_SEPARATOR+"+");

		if(fields == null || fields.length != fieldsNo) {
			int i = (fields == null ?  -1 : fields.length);
			log.error("Can not build job index for line: " + line +
					"\nSome data fields are missing. Expected fields length is " + fieldsNo + ", in file is " + i );
			fields = null;
			return;
		} 
		
		String jobId = fields[SWFFields.DATA_JOB_NUMBER];
		this.jobIndex.put(jobId, position);
	}
	
	public int getType(){
		return 0;
	}
	
}
