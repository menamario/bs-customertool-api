package mx.com.bsmexico.layoutstool.test.api;

import java.util.Set;
import java.util.function.Predicate;

import org.junit.Assert;
import org.junit.Test;

import javafx.scene.control.TableColumn;
import javafx.util.StringConverter;
import mx.com.bsmexico.customertool.api.layouts.control.ColumnTableFactoryAbstract;
import mx.com.bsmexico.customertool.api.layouts.modell.LayoutMetaModel;
import mx.com.bsmexico.customertool.api.layouts.modell.converter.SecureLongStringConverter;
import mx.com.bsmexico.layoutstool.test.api.misc.ModelTest;

public class LayoutModelTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void layoutProcessorTest() {
		LayoutMetaModel<ModelTest> metamodel = new LayoutMetaModel<ModelTest>(ModelTest.class) {
		};

		Assert.assertTrue(metamodel.getTotalFields() == 2);
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
		try {
			Predicate restriction = metamodel.getRestriction(ModelTest.FIELD_PROPERTY_2);
			Assert.assertNotNull(restriction);
			Assert.assertTrue(restriction.test("00"));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void factoryColumnTest() {
		final ColumnTableFactoryAbstract<ModelTest> factory = new ColumnTableFactoryAbstract<ModelTest>(
				ModelTest.class) {
		};
		final Set<String> ids = factory.getFieldIds();
		Assert.assertTrue(ids.size() == 2);
		TableColumn tc = null;
		for (String id : ids) {
			try {
				tc = factory.getInstance(id, 100);
				Assert.assertNotNull(tc);
				Assert.assertNotNull(tc.getOnEditCommit());
				Assert.assertNotNull(tc.getCellFactory());
				Assert.assertNotNull(tc.getCellValueFactory());
				Assert.assertTrue(tc.getId().equals(id));
			} catch (Exception e) {
				Assert.fail(e.getMessage());
			}
		}

	}
}
