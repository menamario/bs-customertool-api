package mx.com.bsmexico.customertool.api.layouts.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the mx.com.bsmexico.generallayout package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _GeneralLayout_QNAME = new QName("http://www.bsmexico.com.mx/generalLayout",
			"generalLayout");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of schema
	 * derived classes for package: mx.com.bsmexico.generallayout
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link Layout }
	 * 
	 */
	public Layout createLayout() {
		return new Layout();
	}

	/**
	 * Create an instance of {@link Fields }
	 * 
	 */
	public Fields createFields() {
		return new Fields();
	}

	/**
	 * Create an instance of {@link Groups }
	 * 
	 */
	public Groups createGroups() {
		return new Groups();
	}

	/**
	 * Create an instance of {@link FieldGroup }
	 * 
	 */
	public FieldGroup createFieldGroup() {
		return new FieldGroup();
	}

	/**
	 * Create an instance of {@link Field }
	 * 
	 */
	public Field createField() {
		return new Field();
	}

	/**
	 * Create an instance of {@link Restriction }
	 * 
	 */
	public Restriction createRestriction() {
		return new Restriction();
	}

	/**
	 * Create an instance of {@link RangeRestriction }
	 * 
	 */
	public RangeRestriction createRangeRestriction() {
		return new RangeRestriction();
	}

	/**
	 * Create an instance of {@link EnumRestriction }
	 * 
	 */
	public EnumRestriction createEnumRestriction() {
		return new EnumRestriction();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Layout }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement }{@code <}{@link Layout
	 *         }{@code >}
	 */
	@XmlElementDecl(namespace = "http://www.bsmexico.com.mx/generalLayout", name = "generalLayout")
	public JAXBElement<Layout> createGeneralLayout(Layout value) {
		return new JAXBElement<Layout>(_GeneralLayout_QNAME, Layout.class, null, value);
	}

}
