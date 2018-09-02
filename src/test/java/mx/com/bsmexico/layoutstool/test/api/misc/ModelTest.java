package mx.com.bsmexico.layoutstool.test.api.misc;

import java.util.function.Predicate;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import mx.com.bsmexico.customertool.api.layouts.modell.LayoutField;
import mx.com.bsmexico.customertool.api.layouts.modell.LayoutFieldConverter;
import mx.com.bsmexico.customertool.api.layouts.modell.LayoutFieldWrapper;
import mx.com.bsmexico.customertool.api.layouts.modell.LayoutModel;
import mx.com.bsmexico.customertool.api.layouts.modell.LayoutModelType;
import mx.com.bsmexico.customertool.api.layouts.modell.RestrictionLayoutField;
import mx.com.bsmexico.customertool.api.layouts.modell.converter.SecureLongStringConverter;

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
	@LayoutFieldConverter(conversionClass = SecureLongStringConverter.class)
	@LayoutFieldWrapper(wrappedClass = Long.class)
	@LayoutField(name = FIELD_PROPERTY_1, title = "Property_1", length = 18)
	private SimpleLongProperty property1;

	@LayoutField(name = FIELD_PROPERTY_2, title = "Property_2", length = 18, disable = true, required = false)
	private SimpleStringProperty property2;

	@RestrictionLayoutField(description = "RestrictionTest", fields = { FIELD_PROPERTY_2 })
	private static Predicate<String> restrictionTest = t -> (t != null && t.length() == 2);

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