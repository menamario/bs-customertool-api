package mx.com.bsmexico.customertool.api.layouts.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for Field complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="Field"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}ID"/&gt;
 *         &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="type" type="{http://www.bsmexico.com.mx/generalLayout}FieldType"/&gt;
 *         &lt;element name="group" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="length" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="disable" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="restriction" type="{http://www.bsmexico.com.mx/generalLayout}Restriction"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Field", propOrder = { "id", "title", "type", "group", "length", "disable", "restriction" })
public class Field {

	@XmlElement(required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlID
	@XmlSchemaType(name = "ID")
	protected String id;
	@XmlElement(required = true)
	protected String title;
	@XmlElement(required = true)
	@XmlSchemaType(name = "string")
	protected FieldType type;
	@XmlElement(required = false)
	protected String group;
	@XmlElement(required = false)
	protected int length;
	@XmlElement(defaultValue = "false", required = true)
	protected boolean disable;
	@XmlElement(required = false)
	protected Restriction restriction;

	/**
	 * Gets the value of the id property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setId(String value) {
		this.id = value;
	}

	/**
	 * Gets the value of the title property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the value of the title property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setTitle(String value) {
		this.title = value;
	}

	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link FieldType }
	 * 
	 */
	public FieldType getType() {
		return type;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param value allowed object is {@link FieldType }
	 * 
	 */
	public void setType(FieldType value) {
		this.type = value;
	}

	/**
	 * Gets the value of the group property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * Sets the value of the group property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setGroup(String value) {
		this.group = value;
	}

	/**
	 * Gets the value of the length property.
	 * 
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Sets the value of the length property.
	 * 
	 */
	public void setLength(int value) {
		this.length = value;
	}

	/**
	 * Gets the value of the disable property.
	 * 
	 */
	public boolean isDisable() {
		return disable;
	}

	/**
	 * Sets the value of the disable property.
	 * 
	 */
	public void setDisable(boolean value) {
		this.disable = value;
	}

	/**
	 * Gets the value of the restriction property.
	 * 
	 * @return possible object is {@link Restriction }
	 * 
	 */
	public Restriction getRestriction() {
		return restriction;
	}

	/**
	 * Sets the value of the restriction property.
	 * 
	 * @param value allowed object is {@link Restriction }
	 * 
	 */
	public void setRestriction(Restriction value) {
		this.restriction = value;
	}

}
