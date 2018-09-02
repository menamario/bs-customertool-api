package mx.com.bsmexico.layoutstool.test.api;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import mx.com.bsmexico.customertool.api.bind.JaxbBinding;
import primer.po.PurchaseOrderType;
import primer.po.USAddress;

public class BindingTest {

	@Test
	public void JaxbBindingTest() {
		try {
			final ClassLoader classLoader = getClass().getClassLoader();
			final File file = new File(classLoader.getResource("xml/po.xml").getFile());
			final JaxbBinding<PurchaseOrderType> binding = new JaxbBinding<PurchaseOrderType>("primer.po") {
				@Override
				public void setValidating(boolean validating) {
					// do nothing
				}
			};
			PurchaseOrderType purchase = binding.unmarshall(new FileInputStream(file));
			Assert.assertNotNull(purchase);
			Assert.assertNotNull(purchase.getItems());
			Assert.assertTrue(purchase.getItems().getItem().size() == 3);
			Assert.assertNotNull(purchase.getBillTo());
			Assert.assertNotNull(purchase.getShipTo());
			USAddress address = purchase.getBillTo();
			address.setName("John Bob");
			address.setStreet("242 Main Street");
			address.setCity("Beverly Hills");
			address.setState("CA");
			address.setZip(new BigDecimal("90210"));
			binding.marshall(purchase, System.out);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}
