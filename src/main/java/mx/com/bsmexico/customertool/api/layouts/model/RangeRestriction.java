package mx.com.bsmexico.customertool.api.layouts.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for RangeRestriction complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="RangeRestriction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="min" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="max" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RangeRestriction", propOrder = { "min", "max" })
public class RangeRestriction {

	protected int min;
	protected int max;

	/**
	 * Gets the value of the min property.
	 * 
	 */
	public int getMin() {
		return min;
	}

	/**
	 * Sets the value of the min property.
	 * 
	 */
	public void setMin(int value) {
		this.min = value;
	}

	/**
	 * Gets the value of the max property.
	 * 
	 */
	public int getMax() {
		return max;
	}

	/**
	 * Sets the value of the max property.
	 * 
	 */
	public void setMax(int value) {
		this.max = value;
	}

}
