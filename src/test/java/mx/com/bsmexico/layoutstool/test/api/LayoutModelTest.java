package mx.com.bsmexico.layoutstool.test.api;

import org.junit.Assert;
import org.junit.Test;

import javafx.util.StringConverter;
import mx.com.bsmexico.customertool.api.layouts.model.LayoutMetaModel;
import mx.com.bsmexico.customertool.api.layouts.model.converter.SecureLongStringConverter;
import mx.com.bsmexico.layoutstool.test.api.misc.ModelTest;

public class LayoutModelTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void layoutProcessorTest() {
		LayoutMetaModel<ModelTest> metamodel = new LayoutMetaModel<ModelTest>(ModelTest.class) {
		};

		Assert.assertTrue(metamodel.getTotalFields() == 3);
		// PROPERTY_1
		Assert.assertTrue(metamodel.getClassFieldName(ModelTest.FIELD_PROPERTY_1).equals("property1"));
		Assert.assertTrue(metamodel.getTitle(ModelTest.FIELD_PROPERTY_1).equals("Property_1"));
		Assert.assertTrue(metamodel.getLength(ModelTest.FIELD_PROPERTY_1) == 18);
		Assert.assertFalse(metamodel.isDisabled(ModelTest.FIELD_PROPERTY_1));
		Assert.assertTrue(metamodel.isRequied(ModelTest.FIELD_PROPERTY_1));
		Assert.assertTrue(
				((Class) metamodel.getWrapperClass(ModelTest.FIELD_PROPERTY_1)).getSimpleName().equals("Long"));
		StringConverter<?> converter;
		try {
			converter = metamodel.getConverter(ModelTest.FIELD_PROPERTY_1);
			Assert.assertNotNull(converter);
			Assert.assertTrue(converter instanceof SecureLongStringConverter);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}

		// PROPERTY_2
		Assert.assertTrue(metamodel.getClassFieldName(ModelTest.FIELD_PROPERTY_2).equals("property2"));
		Assert.assertTrue(metamodel.getTitle(ModelTest.FIELD_PROPERTY_2).equals("Property_2"));
		Assert.assertTrue(metamodel.getLength(ModelTest.FIELD_PROPERTY_2) == 18);
		Assert.assertTrue(metamodel.isDisabled(ModelTest.FIELD_PROPERTY_2));
		Assert.assertFalse(metamodel.isRequied(ModelTest.FIELD_PROPERTY_2));
		Assert.assertNull(metamodel.getWrapperClass(ModelTest.FIELD_PROPERTY_2));
		try {
			Assert.assertNull(metamodel.getConverter(ModelTest.FIELD_PROPERTY_2));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}		
	}
}
