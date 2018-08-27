package mx.com.bsmexico.customertool.api.layouts.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for Layout complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="Layout"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="fields" type="{http://www.bsmexico.com.mx/generalLayout}Fields"/&gt;
 *         &lt;element name="groups" type="{http://www.bsmexico.com.mx/generalLayout}Fields"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Layout", propOrder = { "fields", "groups" })
@XmlRootElement
public class Layout {

	@XmlElement(required = true)
	protected Fields fields;
	@XmlElement(required = false)
	protected Fields groups;

	/**
	 * Gets the value of the fields property.
	 * 
	 * @return possible object is {@link Fields }
	 * 
	 */
	public Fields getFields() {
		return fields;
	}

	/**
	 * Sets the value of the fields property.
	 * 
	 * @param value allowed object is {@link Fields }
	 * 
	 */
	public void setFields(Fields value) {
		this.fields = value;
	}

	/**
	 * Gets the value of the groups property.
	 * 
	 * @return possible object is {@link Fields }
	 * 
	 */
	public Fields getGroups() {
		return groups;
	}

	/**
	 * Sets the value of the groups property.
	 * 
	 * @param value allowed object is {@link Fields }
	 * 
	 */
	public void setGroups(Fields value) {
		this.groups = value;
	}

}
