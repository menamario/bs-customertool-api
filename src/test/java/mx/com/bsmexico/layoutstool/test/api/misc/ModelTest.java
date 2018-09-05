package mx.com.bsmexico.layoutstool.test.api.misc;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import mx.com.bsmexico.customertool.api.layouts.model.LayoutField;
import mx.com.bsmexico.customertool.api.layouts.model.LayoutFieldConverter;
import mx.com.bsmexico.customertool.api.layouts.model.LayoutFieldWrapper;
import mx.com.bsmexico.customertool.api.layouts.model.LayoutModel;
import mx.com.bsmexico.customertool.api.layouts.model.LayoutModelType;
import mx.com.bsmexico.customertool.api.layouts.model.converter.SecureLongStringConverter;

/**
 * 
 * Beneficiario Model
 * 
 * @author jchr
 *
 */
@LayoutModel(type = LayoutModelType.PROPERTY_JAVABEANS)
public class ModelTest {
	public static final String FIELD_PROPERTY_1 = "PROPERTY_1";
	public static final String FIELD_PROPERTY_2 = "PROPERTY_2";
	@LayoutFieldConverter(converterClass = SecureLongStringConverter.class)
	@LayoutFieldWrapper(wrappedClass = Long.class)
	@LayoutField(name = FIELD_PROPERTY_1, title = "Property_1", length = 18)
	private SimpleLongProperty property1;

	@LayoutField(name = FIELD_PROPERTY_2, title = "Property_2", length = 18, disable = true, required = false)
	private SimpleStringProperty property2;

	public ModelTest() {
		property1 = new SimpleLongProperty();
	}

	/**
	 * @return
	 */
	public Long getProperty1() {
		return property1.get();
	}

	/**
	 * @param cuenta
	 */
	public void setProperty1(Long cuenta) {
		this.property1.set(cuenta);
	}

	/**
	 * @return
	 */
	public String getProperty2() {
		return property2.get();
	}

	/**
	 * @param cuenta
	 */
	public void setProperty2(String cuenta) {
		this.property2.set(cuenta);
	}

}
