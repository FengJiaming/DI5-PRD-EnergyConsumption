package controller.workload.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import bsh.EvalError;
import bsh.Interpreter;
import controller.simulator.utils.RandomNumbers;
import controller.workload.SWFFields;
import controller.workload.loader.SWFParser;

import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList;

public class RandomNumbersWrapper {
	
	protected Log log = LogFactory.getLog(RandomNumbersWrapper.class);
	
	protected SWFParser swfParser;
	protected String jobId;
	protected String taskId;
	
	protected RandomNumbers randomNumbers[];
	
	/**
	 * List of all dependency paths detected in workload.
	 */
	protected ArrayList<ArrayList<String>> idDep;
	
	/**
	 * Map, where key is workload node id and value is arithmetic expression.
	 */
	protected HashMap <String, String> idExpMap;
	protected Interpreter interpreter;
	
	public RandomNumbersWrapper(){
		swfParser = null;
		idExpMap = new HashMap<String, String>();
		interpreter = new Interpreter();
		randomNumbers = new RandomNumbers[5];
		/*
		 * 0 - parameterRandomNumbers; 
		 * 1 - hardConstraintsRandomNumbers;
		 * 2 - softConstraintsRandomNumbers;
		 * 3 - timeConstraintsRandomNumbers;
		 * 4 - precedingConstraintsRandomNumbers;
		 */
	}
	
	public void setExternalFileDpendencies(String fileName) throws NullPointerException, IOException{
			
			this.swfParser = new SWFParser(fileName);
	}
	
	public void setCurrentJobId(String id){
		this.jobId = id;
	}
	
	public void setCurrentTaskId(String id){
		this.taskId = id;
	}
	
	public void setParameterRandomNumbers(RandomNumbers parameterRandomNumbers) {
		randomNumbers[0] = parameterRandomNumbers;
	}
	public void setHardConstraintsRandomNumbers(
			RandomNumbers hardConstraintsRandomNumbers) {
		randomNumbers[1] = hardConstraintsRandomNumbers;
	}
	public void setSoftConstraintsRandomNumbers(
			RandomNumbers softConstraintsRandomNumbers) {
		randomNumbers[2] = softConstraintsRandomNumbers;
	}
	public void setTimeConstraintsRandomNumbers(
			RandomNumbers timeConstraintsRandomNumbers) {
		randomNumbers[3] = timeConstraintsRandomNumbers;
	}
	public void setPrecedingConstraintsRandomNumbers(
			RandomNumbers precedingConstraintsRandomNumbers) {
		randomNumbers[4] = precedingConstraintsRandomNumbers;
	}
	
	
	public double evalValueFor(String id, String refElementId){
		ArrayList<String> path = null;
		String idName;
		boolean found = false;
		int idx = 0;
		
		// find path which contains node.id . 
		for(int i = 0; i < idDep.size(); i++){
			path = idDep.get(i);
			// find position of id in path
			for(idx = 0; idx < path.size(); idx++){
				idName = path.get(idx);
				if(idName.equals(id)) {
					found = true;
					break;
				}
			}
			if(found) break;
		}
		
		if(!found)
			throw new RuntimeException("No value for "+id);

		RandomNumbers randomNumber;
		String genName;
		String expr;
		double value = 0;
		Number nValue;
		
		
		try {
			// this is the id of the first node in the path. All others nodes depends on this node. 
			idName = path.get(0);
				
			if(isExternalFileDependent(refElementId)){	// evaluate external file dependency
				value = evalExternalFileDependency(refElementId);
				if(idExpMap.containsKey(idName)){
					expr = idExpMap.get(idName);
					interpreter.set("x", value);
					nValue = (Number) interpreter.eval(expr);
					value = nValue.doubleValue();
				}
			} else {								// if there is no external file dependency, try to use random generator
				for(int i = 0; i < randomNumbers.length; i++){
					randomNumber = randomNumbers[i];
					if(randomNumber.containsElement(idName)){
						genName = randomNumber.getGeneratorName(idName);
				//		value = randomNumber.getRandomValue(genName);
						value = randomNumber.getLastGeneratedRandomValue(genName);
						break;
					}
				}
			}
			
			// execute all arithmetic dependency expressions between first node and current one.
			for(int i = 1; i <= idx; i++){
				idName = path.get(i);
				expr = idExpMap.get(idName);
				interpreter.set("x", value);
				nValue = (Number)interpreter.eval(expr);
				value = nValue.doubleValue();
				if(i < idx) {
					// random factor which was generated last time should be added to current value.
					for(int j = 0; j < randomNumbers.length; j++){
						randomNumber = randomNumbers[j];
						if(randomNumber.containsElement(idName)){
							genName = randomNumber.getGeneratorName(idName);
							value = value + randomNumber.getLastGeneratedRandomValue(genName);
							break;
						}
					}
				}
			}
			
			// if for current node is defined any random generator, it means that to the value 
			// accounted by arithmetic expressions some random factor must be added.
			for(int i = 0; i < randomNumbers.length; i++){
				randomNumber = randomNumbers[i];
				if(randomNumber.containsElement(idName)){
					genName = randomNumber.getGeneratorName(idName);
					value = value + randomNumber.getRandomValue(genName);
					break;
				}
			}
			
		} catch (IllegalAccessException e){
			e.printStackTrace();
		} catch (EvalError e) {
			e.printStackTrace();
		}

		return value;
	}
	
	
	/**
	 * 
	 * @param fileLocation - workload configuration file location
	 * @return list of lists of xml elements id's, organized in dependency order (top-down hierarchy)
	 * or null if no dependencies in workload configuration file were detected.
	 * @throws Exception if cycle in dependency graph was detected
	 */
	public ArrayList<ArrayList<String>> findDependencies(String fileLocation) throws Exception{
		ArrayList <ArrayList<String>> genOrderList = new ArrayList<ArrayList<String>>();
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = docFactory.newDocumentBuilder();
			Document doc = builder.parse(fileLocation);
			
			
			//XPathFactory factory = XPathFactory.newInstance();
			XPathFactory factory = new com.sun.org.apache.xpath.internal.jaxp.XPathFactoryImpl();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile("//*[@id]");
			
			Object wynik = expr.evaluate(doc, XPathConstants.NODESET);
			DTMNodeList list = (DTMNodeList) wynik;
			Node node = null;
			Node refAttr = null;
			Node idAttr = null;
			
			HashMap<String, Integer> nodeMap = new HashMap<String, Integer>();
			HashMap<Integer, String> nodeMapInv = new HashMap<Integer, String>();
			String key;
			for(int i = 0; i < list.getLength(); i++){
				node = list.item(i);
				key = node.getAttributes().getNamedItem("id").getNodeValue();
				nodeMap.put(key, i);
				nodeMapInv.put(i, key);
				node = node.getAttributes().getNamedItem("expr");
				if(node != null && node.getNodeValue() != null)
					idExpMap.put(key, node.getNodeValue());
				else
					idExpMap.put(key, "x");
			}
			
			int size = nodeMap.size();
			if(size == 0)
				return null;
			
			int refGraph[] = new int[size];
			for(int i = 0; i < refGraph.length; i++)
				refGraph[i] = -1;
			
			expr = xpath.compile("//*[@refElementId]");
			wynik = expr.evaluate(doc, XPathConstants.NODESET);
			int x = 0;
			int y = 0;
			String idValue = null;
			for(int i = 0; i < list.getLength(); i++){
				node = list.item(i);
				refAttr = node.getAttributes().getNamedItem("refElementId");
				idAttr = node.getAttributes().getNamedItem("id");
				if(refAttr != null){
					try {
						idValue = idAttr.getNodeValue();
						y = nodeMap.get(idValue);
						x = nodeMap.get(refAttr.getNodeValue());
						refGraph[y] = nodeMap.get(nodeMapInv.get(x));
						
					} catch (NullPointerException e){
						log.warn("Element referenced by " + idValue + " doeas not exist in configuration file.");
					}
				}
			}

			// find start points
			/*
			 * ostatni w grafie jest ten, ktory nie poprzedza innego wierzcholka (nie ma nastepnikow).
			 * W celu lokalizacji:
			 * jezeli wierzcholek 'i' posiada poprzednika 'p', to poprzednik 'p' oznaczany jest jako uzyty
			 * (posiadajacy nastepnika) - pod indeksem reprezentujacym 'p' w helpTab wpisywne jest -1
			 */
			
			int helpTab[] = new int[refGraph.length];
			for(int i = 0; i < refGraph.length; i++){
				if(refGraph[i] != -1)			// bo -1 nie moze byc indeksem tablicy
					helpTab[refGraph[i]] = -1;
			}
			
			// path end detection
			ArrayList <Integer> pathEnd = new ArrayList<Integer>();
			for(int i = 0; i < helpTab.length; i++){
				if(helpTab[i] == 0) {
					pathEnd.add(i);
				}
			}
			
			int loopDetector[] = new int[refGraph.length];
			int usedNodes[] = new int[refGraph.length]; // 1 - used, 0 - not used
			boolean process = true;
			boolean loopNotFound = true;
			int idx = 0;
			int pathId = 0;
			int val = 0;
			String path = new String();
			ArrayList <String> genOrder = new ArrayList<String>();
			
			if(pathEnd.size() == 0)
				throw new Exception("ERROR: Loop detected in attributes dependency.");
			
			// for each path (path end)
			for(int i = 0; i < pathEnd.size(); i++){
				System.arraycopy(refGraph, 0, loopDetector, 0, refGraph.length);
				idx = pathEnd.get(i);
				usedNodes[idx] = 1;
				pathId = refGraph.length + idx;
				path = nodeMapInv.get(idx);
				genOrder.clear();
				genOrder.add(0, path);
				process = true;
				loopNotFound = true;
				
				// found ancestor of current nod
				// path is build from the end to the beginning
				while(process && loopNotFound){
					val = loopDetector[idx];
					
					if(val == -1)
						process = false;
					else if(val == -pathId)
 						loopNotFound = false;
 					else {
 						loopDetector[idx] = -pathId;
 						path = nodeMapInv.get(val) + " -> " + path;
 						usedNodes[val] = 1;
 						genOrder.add(0, nodeMapInv.get(val));
 					}
					idx = val;
				}
				if(!loopNotFound) {
					throw new Exception("ERROR: Loop detected in atributes dependency, path: "+path);
				}
				
				if(genOrder.size() > 0){
					genOrderList.add((ArrayList<String>)genOrder.clone());
				}
				
			}
			
			// the same as above loop, but for all unused nodes.
			for(int i = 0; i < usedNodes.length; i++){
				if(usedNodes[i] == 0){
					System.arraycopy(refGraph, 0, loopDetector, 0, refGraph.length);
					idx = i;
					pathId = refGraph.length + idx;
					path = nodeMapInv.get(idx);
					genOrder.clear();
					genOrder.add(0, path);
					process = true;
					loopNotFound = true;
					
					while(process && loopNotFound){
						val = loopDetector[idx];
						
						if(val == -1)
							process = false;
						else if(val == -pathId)
	 						loopNotFound = false;
	 					else {
	 						loopDetector[idx] = -pathId;
	 						path = nodeMapInv.get(val) + " -> " + path;
	 						genOrder.add(0, nodeMapInv.get(val));
	 					}
						idx = val;
					}
					if(!loopNotFound){
						throw new Exception("ERROR: Loop detected in atributes dependency, path: "+path);
					}
					
					if(genOrder.size() > 0){
						genOrderList.add((ArrayList<String>)genOrder.clone());
					}
				}
			}
			
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		idDep = genOrderList;
		return genOrderList;
	}
	
	protected boolean isExternalFileDependent(String idName){
		if(idName.startsWith("swf."))
			return true;
		
		return false;
	}
	
	protected double evalExternalFileDependency(String refElementId){
		String filedName = refElementId.substring(4).toUpperCase();
		
		try {
			String value = null;
			
			if("JOB_NUMBER".equals(filedName)){
				value = swfParser.readTask(jobId, taskId)[SWFFields.DATA_JOB_NUMBER];
			} else if("SUBMIT_TIME".equals(filedName)){
				value = swfParser.readTask(jobId, taskId)[SWFFields.DATA_SUBMIT_TIME];
			} else if("WAIT_TIME".equals(filedName)){
				value = swfParser.readTask(jobId, taskId)[SWFFields.DATA_WAIT_TIME];
			} else if("RUN_TIME".equals(filedName)){
				value = swfParser.readTask(jobId, taskId)[SWFFields.DATA_RUN_TIME];
			} else if("NUMBER_OF_ALLOCATED_PROCESSORS".equals(filedName)){
				value = swfParser.readTask(jobId, taskId)[SWFFields.DATA_NUMBER_OF_ALLOCATED_PROCESSORS];
			} else if("AVERAGE_CPU_TIME_USED".equals(filedName)){
				value = swfParser.readTask(jobId, taskId)[SWFFields.DATA_AVERAGE_CPU_TIME_USED];
			} else if("USED_MEMORY".equals(filedName)){
				value = swfParser.readTask(jobId, taskId)[SWFFields.DATA_USED_MEMORY];
			} else if("REQUESTED_NUMBER_OF_PROCESSORS".equals(filedName)){
				value = swfParser.readTask(jobId, taskId)[SWFFields.DATA_REQUESTED_NUMBER_OF_PROCESSORS];
			} else if("REQUESTED_TIME".equals(filedName)){
				value = swfParser.readTask(jobId, taskId)[SWFFields.DATA_REQUESTED_TIME];
			} else if("REQUESTED_MEMORY".equals(filedName)){
				value = swfParser.readTask(jobId, taskId)[SWFFields.DATA_REQUESTED_MEMORY];
			} else if("STATUS".equals(filedName)){
				value = swfParser.readTask(jobId, taskId)[SWFFields.DATA_STATUS];
			} else if("USER_ID".equals(filedName)){
				value = swfParser.readTask(jobId, taskId)[SWFFields.DATA_USER_ID];
			} else if("GROUP_ID".equals(filedName)){
				value = swfParser.readTask(jobId, taskId)[SWFFields.DATA_GROUP_ID];
			} else if("EXECUTABLE_NUMBER".equals(filedName)){
				value = swfParser.readTask(jobId, taskId)[SWFFields.DATA_EXECUTABLE_NUMBER];
			} else if("QUEUE_NUMBER".equals(filedName)){
				value = swfParser.readTask(jobId, taskId)[SWFFields.DATA_QUEUE_NUMBER];
			} else if("PARTITION_NUMBER".equals(filedName)){
				value = swfParser.readTask(jobId, taskId)[SWFFields.DATA_PARTITION_NUMBER];
			} else if("PRECEDING_JOB_NUMBER".equals(filedName)){
				value = swfParser.readTask(jobId, taskId)[SWFFields.DATA_PRECEDING_JOB_NUMBER];
			} else if("THINK_TIME_FROM_PRECEDING_JOB".equals(filedName)){
				value = swfParser.readTask(jobId, taskId)[SWFFields.DATA_THINK_TIME_FROM_PRECEDING_JOB];
			} else {
				log.error("No external value for element reference "+refElementId + ". Assign default value 0.");
				return 0;
			}
			
			return Double.valueOf(value);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public void close(){
		try {
			swfParser.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
