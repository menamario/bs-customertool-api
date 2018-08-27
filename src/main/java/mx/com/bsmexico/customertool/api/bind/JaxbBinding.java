package mx.com.bsmexico.customertool.api.bind;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * JAXB Binding
 * 
 * @author jchr
 *
 * @param <T>
 */
public abstract class JaxbBinding<T> implements XMLBinding<T> {

	protected Unmarshaller unmarshaller;
	protected Marshaller marshaller;
	protected JAXBContext jc;

	/**
	 * Constructor
	 * 
	 * @param pckg Package name (convention name Java) where are the classes to be
	 *             bound
	 */
	public JaxbBinding(String pckg) throws JAXBException {
		jc = JAXBContext.newInstance(pckg);
		init();
	}

	/**
	 * Contructor
	 * 
	 * @param classesToBeBound Classes set to be bound
	 */
	public JaxbBinding(Class<?>... classesToBeBound) throws JAXBException {
		jc = JAXBContext.newInstance(classesToBeBound);
		init();
	}

	/**
	 * @throws JAXBException
	 */
	private void init() throws JAXBException {
		unmarshaller = jc.createUnmarshaller();
		marshaller = jc.createMarshaller();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mx.com.bsmexico.layoutstool.core.api.bind.XMLBinding#unmarshall(java.io.File,
	 * java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public T unmarshall(final InputStream file) throws JAXBException {
		return (T) ((JAXBElement<T>) this.unmarshaller.unmarshal(file)).getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mx.com.bsmexico.layoutstool.core.api.bind.XMLBinding#marshall(java.lang.
	 * Object)
	 */
	@Override
	public void marshall(final T obj, final OutputStream output) throws JAXBException {
		this.marshaller.marshal(obj, output);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mx.com.bsmexico.layoutstool.core.api.bind.XMLBinding#setValidating(boolean)
	 */
	@Override
	public abstract void setValidating(final boolean validating);
}