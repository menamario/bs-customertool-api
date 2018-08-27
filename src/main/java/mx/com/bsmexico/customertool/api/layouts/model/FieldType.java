package mx.com.bsmexico.customertool.api.layouts.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for FieldType.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="FieldType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="numeric"/&gt;
 *     &lt;enumeration value="string"/&gt;
 *     &lt;enumeration value="boolean"/&gt;
 *     &lt;enumeration value="decimal"/&gt;
 *     &lt;enumeration value="date"/&gt;
 *     &lt;enumeration value="enum"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "FieldType")
@XmlEnum
public enum FieldType {

	@XmlEnumValue("numeric")
	NUMERIC("numeric"), @XmlEnumValue("string")
	STRING("string"), @XmlEnumValue("boolean")
	BOOLEAN("boolean"), @XmlEnumValue("decimal")
	DECIMAL("decimal"), @XmlEnumValue("date")
	DATE("date"), @XmlEnumValue("enum")
	ENUM("enum");
	private final String value;

	FieldType(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	public static FieldType fromValue(String v) {
		for (FieldType c : FieldType.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}

}
