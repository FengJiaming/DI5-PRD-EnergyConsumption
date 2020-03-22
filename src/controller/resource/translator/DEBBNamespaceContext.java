package controller.resource.translator;


import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

		
	
/**
 * Auxiliary class for handling multiple namespace prefixes
 *
 */
public class DEBBNamespaceContext implements NamespaceContext
    {	
		/**
		 * Gets schema url for namespace prefix 
		 */
        public String getNamespaceURI(String prefix)
        {
            if (prefix.equals("plm"))
                return "http://www.plmxml.org/Schemas/PLMXMLSchema";
            else if (prefix.equals("cim"))
            	return "http://www.coolemall.eu/DEBBComponent";
            else
                return XMLConstants.NULL_NS_URI;
        }
        
        /**
         * Unnecessary
         */
        public String getPrefix(String namespace)
        {
        	return null;
        }

        /**
         * Unnecessary
         */
        public Iterator<String> getPrefixes(String namespace)
        {
            return null;
        }
    }  