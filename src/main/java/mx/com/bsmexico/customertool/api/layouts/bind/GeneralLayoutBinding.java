package mx.com.bsmexico.customertool.api.layouts.bind;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import mx.com.bsmexico.customertool.api.bind.JaxbBinding;
import mx.com.bsmexico.customertool.api.layouts.model.Layout;

/**
 * XML Binding to Layout type
 * 
 * @author jchr
 *
 */
public class GeneralLayoutBinding extends JaxbBinding<Layout> {
	private Schema schema;
	private SchemaFactory schemaFactory;
	private final static String PACKAGE = "mx.com.bsmexico.customertool.api.layouts.model";

	/**
	 * @param pckg
	 * @throws JAXBException
	 */
	public GeneralLayoutBinding() throws JAXBException {
		super(PACKAGE);
	}

	@Override
	public void setValidating(boolean validating) {
		if (schema == null) {
			schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			final ClassLoader classLoader = getClass().getClassLoader();
			final File schemaFile = new File(classLoader.getResource("generalLayout.xsd").getFile());
			try {
				schema = schemaFactory.newSchema(schemaFile);
			} catch (SAXException e) {
				// do nothig
			}
		}
		this.unmarshaller.setSchema(schema);
	}

}
