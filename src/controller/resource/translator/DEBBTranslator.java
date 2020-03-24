package controller.resource.translator;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


public class DEBBTranslator {

	// TODO:
	// 1) Configuration file needed
	// 2) Where to take information about schedulers and estimators from?
	// 3) Where to take information about sensors? There is something in PLM XML and DEBB Component file(s)

	private static final String DEFAULT_DCWORMS_OUTPUT_FILE_NAME = "example/xml/DCWORMS_TEST.xml";
	// private static final String DEFAULT_INPUT_FILE_NAME =// "xml/PLMXML_PSNCRECS.xml";
	private static final String DEFAULT_PLMXML_INPUT_FILE_NAME = "example/xml/2020-03-15-02-43-44/PLMXML_PolytechPolyTestroom_20.xml";
//	private static final String DEFAULT_DEBB_COMPONENT_INPUT_FILE_NAME = "example/xml/debb_component_hlrs_smallServerRoom.xml";
	private static final String DEFAULT_DEBB_COMPONENT_INPUT_FILE_NAME = null;
	
	private static final String DEFAULT_SCHEMA_NAME = "DCWormsResSchema.xsd";
	
	private static final String DEFAULT_DEBB_COMPONENT_TRANSFORMATION_FILE_NAME = "example/xml/DEBBComponentTranslator.xsl";
	private static final String DEFAULT_NAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";
	
	private static final String DEFAULT_UPDATE_TRANSFORMATION_FILE_NAME = "example/xml/DCWoRMSDEBBUpdater.xsl";
	private static final String DEFAULT_SCHEDULERS_AND_ESTIMATORS_FILE_NAME = "../debb2dcworms/example1.xml";
	
	
	private static DocumentBuilder xmlDocumentBuilder;
	private static DocumentBuilderFactory domFactory;

	private static HashMap<String, String> computingResourceClasses = null;
	private static HashMap<String, String> resourceUnitClasses;
	private static XPath xpath;
	private static Document dcwormsDocument;
	private static Logger logger = Logger
			.getLogger(DEBBTranslator.class);
	private static StreamSource xsltDEBBComponentTransformationSource;
	private static Transformer xsltDEBBComponentTransformationTransformer;

	/**
	 * 
	 */
	public DEBBTranslator() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void translate(String[] args) {
		String inputFileName = DEFAULT_PLMXML_INPUT_FILE_NAME;
		String outputFileName = DEFAULT_DCWORMS_OUTPUT_FILE_NAME;

		if (args != null && args.length > 0) {
			inputFileName = args[0];
			if (inputFileName == null
					|| (inputFileName != null && inputFileName.isEmpty() == true)) {
				inputFileName = DEFAULT_PLMXML_INPUT_FILE_NAME;
			}

			if (args.length > 1) {
				outputFileName = args[1];
				if (outputFileName == null
						|| (outputFileName != null && outputFileName.isEmpty() == true)) {
					outputFileName = DEFAULT_DCWORMS_OUTPUT_FILE_NAME;
				}
			}
		}
//		BasicConfigurator.configure();

		logger.debug("inputFileName=" + inputFileName);
		logger.debug("outputFileName=" + outputFileName);

		try {
			initialize();

			// Open and read content of the input file (i.e. PLM XML document)
			Document plmxmlDocument = readPLMXMLInputFile(inputFileName);

			XPathFactory xpathFactory = XPathFactory.newInstance();
			xpath = xpathFactory.newXPath();
			NamespaceContext nsContext = new DEBBNamespaceContext();
			xpath.setNamespaceContext(nsContext);

			// Find root component (top level DEBB) id
			XPathExpression expr = xpath
					.compile("//plm:ProductDef/plm:InstanceGraph/@rootRefs");
			Object result = expr.evaluate(plmxmlDocument, XPathConstants.NODE);
			Node rootIdNode = (Node) result;
			logger.debug(rootIdNode.getNodeName());
			logger.debug(rootIdNode.getNodeValue());

			// Creating destination, dcworms document
			dcwormsDocument = xmlDocumentBuilder.newDocument();

			// Creating root, environment, element
			Element rootElement = dcwormsDocument.createElement("environment");

			// Information about namespaces and schema validating output xml
			// Should be:
			// <environment
			// xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			// xsi:noNamespaceSchemaLocation="DCWormsResSchema.xsd">

			Attr schemaAttribute = dcwormsDocument
					.createAttribute("xsi:noNamespaceSchemaLocation");
			schemaAttribute.setValue(DEFAULT_SCHEMA_NAME);
			Attr namespaceAttribute = dcwormsDocument
					.createAttribute("xmlns:xsi");
			namespaceAttribute.setValue(DEFAULT_NAMESPACE);
			rootElement.setAttributeNode(schemaAttribute);
			rootElement.setAttributeNode(namespaceAttribute);

			dcwormsDocument.appendChild(rootElement);
			Comment comment = dcwormsDocument
					.createComment("CoolEmAll testbed description");
			rootElement.appendChild(comment);

			// Creating resources element
			Element resourcesElement = dcwormsDocument
					.createElement("resources");
			rootElement.appendChild(resourcesElement);

			// *****************************************************************
			// Creating (main) scheduler element
			// TODO: Fill scheduler with reasonable data!
			Element schedulerElement = dcwormsDocument
					.createElement("scheduler");
			comment = dcwormsDocument.createComment("Default scheduler");
			schedulerElement.appendChild(comment);
			resourcesElement.appendChild(schedulerElement);

			// *****************************************************************
			// Transform top level DEBB and all nested DEBBs
			transformDEBB(plmxmlDocument, dcwormsDocument, resourcesElement,
					rootIdNode.getNodeValue());

			// *****************************************************************
			// TODO: Extract information about sensors from PLM XML

			// write the content into xml file
			saveDCWoRMSOutputFile(outputFileName, dcwormsDocument);
			
			//Add scheduler plugin	
			String debbUpdateTransformationFileName = DEFAULT_UPDATE_TRANSFORMATION_FILE_NAME;
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer updateTransformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(
							debbUpdateTransformationFileName ));
			updateTransformer.setParameter("additionalInformationFileName", DEFAULT_SCHEDULERS_AND_ESTIMATORS_FILE_NAME);
			updateTransformer.transform(new javax.xml.transform.stream.StreamSource(outputFileName), 
					new javax.xml.transform.stream.StreamResult(outputFileName));
			
		} catch (ParserConfigurationException e) {
			logger.error("Parser configuration exception", e);
		} catch (IOException e) {
			logger.error("I/O exception", e);
		} catch (SAXException e) {
			logger.error("SAX exception", e);
		} catch (XPathExpressionException e) {
			logger.error("XPATH expression exception", e);
		} catch (Exception e) {
			logger.error("Non specific exception", e);
		}

	}

	private static void initialize() throws ParserConfigurationException,
			TransformerConfigurationException {
		domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true); // never forget this!
		domFactory.setValidating(true);
		domFactory.setAttribute(
				"http://java.sun.com/xml/jaxp/properties/schemaLanguage",
				"http://www.w3.org/2001/XMLSchema");

		xmlDocumentBuilder = domFactory.newDocumentBuilder();

		// XSLT transformation for translating information stored DEBB
		// Components file(s)
		xsltDEBBComponentTransformationSource = new StreamSource(
				DEFAULT_DEBB_COMPONENT_TRANSFORMATION_FILE_NAME);
		// create an instance of TransformerFactory
		TransformerFactory transFact = TransformerFactory.newInstance();
		xsltDEBBComponentTransformationTransformer = transFact
				.newTransformer(xsltDEBBComponentTransformationSource);

		// TODO: Check it this is correct!
		// TODO: Move it to configuration file
		computingResourceClasses = new HashMap<String, String>();
		computingResourceClasses.put("ComputeBox2", "ComputeBox2");
		computingResourceClasses.put("ComputeBox1", "ComputeBox1");
		computingResourceClasses.put("NodeGroup", "NodeGroup");
		computingResourceClasses.put("Node", "ComputingNode");
		computingResourceClasses.put("Processor", "Processor");

		resourceUnitClasses = new HashMap<String, String>();
		resourceUnitClasses.put("Sensor", "Sensor");
		resourceUnitClasses.put("Memory", "Memory");
		resourceUnitClasses.put("Baseboard", "Baseboard");
		resourceUnitClasses.put("CoolingDevice", "CoolingDevice");
		resourceUnitClasses.put("Storage", "Storage");

	}

	private static Document readPLMXMLInputFile(String inputFileName) {
		Document plmxmlDocument;
		try {
			plmxmlDocument = xmlDocumentBuilder.parse(inputFileName);
			return plmxmlDocument;
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static Document readDEBBComponentInputFile(String inputFileName) {
		Document debbComponentDocument;
		try {
			debbComponentDocument = xmlDocumentBuilder.parse(inputFileName);
			return debbComponentDocument;
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static void saveDCWoRMSOutputFile(String outputFileName,
			Document dcwormsDocument) {

		try {
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer;

			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			DOMSource source = new DOMSource(dcwormsDocument);
			StreamResult transformResult = new StreamResult(new File(
					outputFileName));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, transformResult);

			logger.debug("File " + outputFileName + " saved!");

		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Translates specified DEBB and all its nested sub DEBBS
	 * 
	 * @param plmxmlDocument
	 * @param dcwormsDocument
	 *            TODO
	 * @param instanceId
	 * @return
	 * @throws Exception
	 */
	public static void transformDEBB(Document plmxmlDocument,
			Document dcwormsDocument, Element debbParentElement,
			String instanceId) throws Exception {

		if (instanceId == null) {
			logger.error("DEBB identifier is null");
			throw new Exception("Null DEBB identifier");
		}
		if (plmxmlDocument == null) {
			logger.error("PLM XML document is null");
			throw new Exception("Null PLM XML document");
		}

		if (debbParentElement == null) {
			logger.error("Parent node in output DCWoRMS document is null");
			throw new Exception("Null parent element in dcworms document");
		}

		// Find DEBB with given id and transform it
		// *****************************************************************
		// 1) ProductInstance -
		XPathExpression expr = xpath
				.compile("//plm:ProductDef/plm:InstanceGraph/plm:ProductInstance[@id='"
						+ instanceId + "']");
		Object result = expr.evaluate(plmxmlDocument, XPathConstants.NODE);
		Node productInstanceNode = (Node) result;
		Node debbNameNode = productInstanceNode.getAttributes().getNamedItem(
				"name");
		logger.debug("name=" + debbNameNode.getNodeValue());
		Node partRefNode = productInstanceNode.getAttributes().getNamedItem(
				"partRef");
		logger.debug("partRef=" + partRefNode.getNodeValue());
//		String partId = partRefNode.getNodeValue().substring(1);
		String partId = partRefNode.getNodeValue(); // Get rid of #
		// at
		// the
		// beginning
		// UserData - hostname
		expr = xpath
				.compile("plm:UserData/plm:UserValue[@title='hostname']/@value");
		result = expr.evaluate(productInstanceNode, XPathConstants.NODE);
		Node debbHostNameNode = (Node) result;

		// UserData - sequence
		expr = xpath
				.compile("plm:UserData/plm:UserValue[@title='sequence']/@value");
		result = expr.evaluate(productInstanceNode, XPathConstants.NODE);
		Node debbSequenceNode = (Node) result;

		// UserData - label
		expr = xpath
				.compile("plm:UserData/plm:UserValue[@title='label']/@value");
		result = expr.evaluate(productInstanceNode, XPathConstants.NODE);
		Node debbLabelNode = (Node) result;

		// UserData - location
		expr = xpath
				.compile("plm:UserData/plm:UserValue[@title='location']/@value");
		result = expr.evaluate(productInstanceNode, XPathConstants.NODE);
		Node debbLocationNode = (Node) result;

		// *****************************************************************
		// 2) ProductRevisionView
		expr = xpath
				.compile("//plm:ProductDef/plm:InstanceGraph/plm:ProductRevisionView[@id='"
						+ partId + "']");
		result = expr.evaluate(plmxmlDocument, XPathConstants.NODE);
		Node productRevisionViewNode = (Node) result;
		// debbName = productRevisionView.getAttributes().getNamedItem("name");
		// logger.debug("name=" + debbName.getNodeValue());

		// UserValue (to extract DEBB name, level or DEBBComponent file)
		// DEBBComponentId
		expr = xpath
				.compile("plm:UserData/plm:UserValue[@title='DEBBComponentId']/@value");
		result = expr.evaluate(productRevisionViewNode, XPathConstants.NODE);
		Node debbComponentIdNode = (Node) result;
		if (debbComponentIdNode != null) {
			logger.debug("debbComponentId="
					+ debbComponentIdNode.getNodeValue());
		}

		// DEBBLevel
		expr = xpath
				.compile("plm:UserData/plm:UserValue[@title='DEBBLevel']/@value");
		result = expr.evaluate(productRevisionViewNode, XPathConstants.NODE);
		Node debbLevelNode = (Node) result;
		if (debbLevelNode != null) {
			logger.debug("debbLevel=" + debbLevelNode.getNodeValue());
		}

		// DEBBComponentFile
		expr = xpath
				.compile("plm:UserData/plm:UserValue[@title='DEBBComponentFile']/@value");
		result = expr.evaluate(productRevisionViewNode, XPathConstants.NODE);
		Node debbComponentFileNameNode = (Node) result;
		String debbComponentFileName = null;
		if (debbComponentFileNameNode != null) {
			// Default DEBBComponent file is assumed
			String prefix = new File(DEFAULT_PLMXML_INPUT_FILE_NAME).getParent();
			debbComponentFileName = prefix + File.separator + debbComponentFileNameNode.getNodeValue();
		} else {
			debbComponentFileName = DEFAULT_DEBB_COMPONENT_INPUT_FILE_NAME;
		}
		logger.debug("debbComponentFileToOpen=" + debbComponentFileName);

		// *****************************************************************
		// 3) Add computingResource element for the DEBB
		if (debbLevelNode != null) {
			Element computingResourceElement = dcwormsDocument
					.createElement("computingResource");
			Attr nameAttribute = dcwormsDocument.createAttribute("name");
			nameAttribute.setValue(debbNameNode.getNodeValue());
			computingResourceElement.setAttributeNode(nameAttribute);

			Attr classAttribute = dcwormsDocument.createAttribute("class");
			String dcwormsClass = computingResourceClasses.get(debbLevelNode
					.getNodeValue());
			classAttribute.setValue(dcwormsClass);
			computingResourceElement.setAttributeNode(classAttribute);

			Document debbComponentDocument = readDEBBComponentInputFile(debbComponentFileName);
			logger.debug("debbComponentNode="
					+ debbComponentIdNode.getNodeValue());
			expr = xpath.compile("//*[ComponentId='"
					+ debbComponentIdNode.getNodeValue() + "']");
			result = expr.evaluate(debbComponentDocument, XPathConstants.NODE);
			Node debbComponentNode = (Node) result;
			if (debbComponentNode != null) {
				logger.debug("debbComponentNode="
						+ debbComponentNode.getNodeName());
			}

			// *****************************************************************
			// 4) Translation of information about DEBB taken from corresponding
			// DEBB components file
			try {
				// Each DEBB can be described in separate document
				// DOMSource -> DOMResult
				// Starting parsing DEBB Components document from specified node
				// works fine with Saxon, but doesn't with Xalan
				DOMSource domSource = new DOMSource(debbComponentNode);
				DOMResult domResult = new DOMResult(computingResourceElement);

				xsltDEBBComponentTransformationTransformer.transform(domSource,
						domResult);
				logger.debug("result node=" + domResult.getNode().getNodeName());
			} catch (NullPointerException npEx) {
				logger.info("Cannot find debb "
						+ debbComponentIdNode.getNodeValue()
						+ " in corresponding DEBB Components "
						+ debbComponentFileName + " file");
			}

			// 5) Adding several minor details about DEBB stored in PLM XML
			// document:
			// Name of the host
			if (debbHostNameNode != null) {
				Element parameterElement = dcwormsDocument
						.createElement("parameter");
				Attr parameterNameAttribute = dcwormsDocument
						.createAttribute("name");
				parameterNameAttribute.setValue("hostname");
				parameterElement.setAttributeNode(parameterNameAttribute);
				Element valueElement = dcwormsDocument.createElement("value");
				valueElement.appendChild(dcwormsDocument
						.createTextNode(debbHostNameNode.getNodeValue()));
				parameterElement.appendChild(valueElement);
				computingResourceElement.appendChild(parameterElement);
			}

			// Sequence
			if (debbSequenceNode != null) {
				// TODO: Sequence is described unclearly in the deliverable, not
				// used in any example. Not sure what to do with it.
				Element parameterElement = dcwormsDocument
						.createElement("parameter");
				Attr parameterNameAttribute = dcwormsDocument
						.createAttribute("name");
				parameterNameAttribute.setValue("sequence");
				parameterElement.setAttributeNode(parameterNameAttribute);
				Element valueElement = dcwormsDocument.createElement("value");
				valueElement.appendChild(dcwormsDocument
						.createTextNode(debbSequenceNode.getNodeValue()));
				parameterElement.appendChild(valueElement);
				computingResourceElement.appendChild(parameterElement);
			}
			debbParentElement.appendChild(computingResourceElement);

			// Label
			if (debbLabelNode != null) {
				Element parameterElement = dcwormsDocument
						.createElement("parameter");
				Attr parameterNameAttribute = dcwormsDocument
						.createAttribute("name");
				parameterNameAttribute.setValue("label");
				parameterElement.setAttributeNode(parameterNameAttribute);
				Element valueElement = dcwormsDocument.createElement("value");
				valueElement.appendChild(dcwormsDocument
						.createTextNode(debbLabelNode.getNodeValue()));
				parameterElement.appendChild(valueElement);
				computingResourceElement.appendChild(parameterElement);

			}

			// Location
			if (debbLocationNode != null) {
				Element parameterElement = dcwormsDocument
						.createElement("parameter");
				Attr parameterNameAttribute = dcwormsDocument
						.createAttribute("name");
				parameterNameAttribute.setValue("location");
				parameterElement.setAttributeNode(parameterNameAttribute);
				Element valueElement = dcwormsDocument.createElement("value");
				valueElement.appendChild(dcwormsDocument
						.createTextNode(debbLocationNode.getNodeValue()));
				parameterElement.appendChild(valueElement);
				computingResourceElement.appendChild(parameterElement);
			}

			// *****************************************************************
			// 6) Get nested instances - nested DEBBs and ask them to transform
			// themselves
			Node instanceRefsNode = productRevisionViewNode.getAttributes()
					.getNamedItem("instanceRefs");
			// Check if anything is nested at all
			if (instanceRefsNode != null) {
				String instances = instanceRefsNode.getNodeValue();
				logger.debug("instances=" + instances);
				// Separate instance ids
				StringTokenizer tokenizer = new StringTokenizer(instances, " ");
				while (tokenizer.hasMoreTokens()) {
					String instance = tokenizer.nextToken();
					logger.debug(instance);
					// Go down in the hierarchy
					transformDEBB(plmxmlDocument, dcwormsDocument,
							computingResourceElement, instance);
					logger.debug("\n");
				}
			}
		}
	}
}
