package mx.com.bsmexico.customertool.api.layouts.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for Restriction complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="Restriction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="pattern" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="range" type="{http://www.bsmexico.com.mx/generalLayout}RangeRestriction"/&gt;
 *         &lt;element name="dependence" type="{http://www.w3.org/2001/XMLSchema}IDREF"/&gt;
 *         &lt;element name="enum" type="{http://www.bsmexico.com.mx/generalLayout}EnumRestriction"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Restriction", propOrder = { "pattern", "range", "dependence", "_enum" })
public class Restriction {

	protected String pattern;
	protected RangeRestriction range;
	@XmlIDREF
	@XmlSchemaType(name = "IDREF")
	protected Object dependence;
	@XmlElement(name = "enum")
	protected EnumRestriction _enum;

	/**
	 * Gets the value of the pattern property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * Sets the value of the pattern property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setPattern(String value) {
		this.pattern = value;
	}

	/**
	 * Gets the value of the range property.
	 * 
	 * @return possible object is {@link RangeRestriction }
	 * 
	 */
	public RangeRestriction getRange() {
		return range;
	}

	/**
	 * Sets the value of the range property.
	 * 
	 * @param value allowed object is {@link RangeRestriction }
	 * 
	 */
	public void setRange(RangeRestriction value) {
		this.range = value;
	}

	/**
	 * Gets the value of the dependence property.
	 * 
	 * @return possible object is {@link Object }
	 * 
	 */
	public Object getDependence() {
		return dependence;
	}

	/**
	 * Sets the value of the dependence property.
	 * 
	 * @param value allowed object is {@link Object }
	 * 
	 */
	public void setDependence(Object value) {
		this.dependence = value;
	}

	/**
	 * Gets the value of the enum property.
	 * 
	 * @return possible object is {@link EnumRestriction }
	 * 
	 */
	public EnumRestriction getEnum() {
		return _enum;
	}

	/**
	 * Sets the value of the enum property.
	 * 
	 * @param value allowed object is {@link EnumRestriction }
	 * 
	 */
	public void setEnum(EnumRestriction value) {
		this._enum = value;
	}

}
