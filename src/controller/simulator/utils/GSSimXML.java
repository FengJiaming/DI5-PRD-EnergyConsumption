package controller.simulator.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.castor.mapping.BindingType;
import org.exolab.castor.xml.ClassDescriptorResolverFactory;
import org.exolab.castor.xml.Introspector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLClassDescriptorResolver;

/**
 * This class provides static methods for marshalling and unmarshalling xml documents.
 * It uses static cache objects, making the marshalling and unmarshalling process far quicker. 
 * 
 * @see <a href="http://www.castor.org/xml-best-practice.html#Performance-Considerations">Castor - Performance Considerations</a>
 */
public class GSSimXML {

	/**
	 * Hidden constructor
	 */
	protected GSSimXML() {
	}
	
	/** The class descriptor resolver */
	protected static XMLClassDescriptorResolver mcdr = (XMLClassDescriptorResolver) ClassDescriptorResolverFactory.createClassDescriptorResolver(BindingType.XML);
	
	/** The unmarshaller object */
	protected static Unmarshaller unmarshaller = new Unmarshaller();
	
	/** The marshaller object */
	protected static Marshaller marshaller = new Marshaller();
	
	//static initialization
	static {
		mcdr.setIntrospector(new Introspector());
		unmarshaller.setResolver(mcdr);
		marshaller.setResolver(mcdr);
	}
	
	/**
	 * A generic method for marshalling.
	 * @param <T> the type of the object that is to be marshalled
	 * @param object the object that is to be marshalled
	 * @param writer the writer, where the result is to be written to
	 * @return true, if the operation succeeded; false otherwise
	 */
	public static synchronized <T> boolean marshal(T object, Writer writer) {
		try {
			marshaller.setWriter(writer);
			marshaller.marshal(object);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (MarshalException e) {
			e.printStackTrace();
			return false;
		} catch (ValidationException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * A generic method for marshalling.
	 * Performs a marshalling and return the result in form of a {@link String} object.
	 * @param <T> the type of the object that is to be marshalled
	 * @param object the object that is to be marshalled
	 * @return a string containing the marshalled xml or <code>null</code> if any error occurred
	 */
	public static synchronized <T> String marshal(T object) {
		StringWriter sw = new StringWriter();
		if (marshal(object, sw))
			return sw.toString();
		else 
			return null;
	}

	/**
	 * A generic method for unmarshalling.
	 * @param <T> the type of the object that is to be unmarshalled
	 * @param clazz the class of the object that is to be marshalled
	 * @param reader the reader, from which the result is to be read
	 * @return the unmarshalled object; <code>null</code> if error occurs
	 */
	public static synchronized <T> T unmarshal(Class<T> clazz, Reader reader) {
		unmarshaller.setClass(clazz);
		T result = null;
		try {
			result = (T) unmarshaller.unmarshal(reader);
		} catch (MarshalException e) {
			e.printStackTrace();
			return null;
		} catch (ValidationException e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	/**
	 * A generic method for unmarshalling.
	 * @param <T> the type of the object that is to be unmarshalled
	 * @param clazz the class of the object that is to be marshalled
	 * @param xml the string, containing an xml document, from which the result is to be read
	 * @return the unmarshalled object; <code>null</code> if error occurs
	 */
	public static synchronized <T> T unmarshal(Class<T> clazz, String xml) {
		return unmarshal(clazz, new StringReader(xml));
	}
}
