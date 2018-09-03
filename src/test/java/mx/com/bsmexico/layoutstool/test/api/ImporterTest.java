package mx.com.bsmexico.layoutstool.test.api;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import mx.com.bsmexico.customertool.api.exporter.CSVImporter;
import mx.com.bsmexico.customertool.api.exporter.ImportTarget;
import mx.com.bsmexico.layoutstool.test.api.misc.Beneficiario;

public class ImporterTest {

	@Test
	public void exporterTest() {
		final ClassLoader classLoader = getClass().getClassLoader();
		final File file = new File(classLoader.getResource("plain/Libro6.csv").getFile());

		final ImportTarget<Beneficiario> target = new ImportTarget<Beneficiario>() {

			@Override
			public void setData(List<Beneficiario> data) {
				Assert.assertTrue(data.size() == 4);
				Assert.assertTrue("083645372655".equals(data.get(0).getCuenta()));
				Assert.assertTrue(StringUtils.isBlank(data.get(0).getNumLinea()));
				Assert.assertTrue(StringUtils.isBlank(data.get(0).getBancoParticipante()));
				Assert.assertTrue("00".equals(data.get(0).getTipoCuenta()));
				Assert.assertTrue("MXN".equals(data.get(0).getMoneda()));
				Assert.assertTrue("55000".equals(data.get(0).getImporteMaximo()));
				Assert.assertTrue("00".equals(data.get(0).getTipoPersona()));
				Assert.assertTrue(StringUtils.isBlank(data.get(0).getRazonSocial()));
				Assert.assertTrue("Mario".equals(data.get(0).getNombre()));
				Assert.assertTrue("Mena".equals(data.get(0).getApellidoMaterno()));
				Assert.assertTrue("Cruz".equals(data.get(0).getApellidoPaterno()));
			}

		};

		final CSVImporter<Beneficiario> importer = new CSVImporter<Beneficiario>(target) {

			@Override
			protected Beneficiario getInstance(List<String> record) {
				final Beneficiario beneficiario = new Beneficiario();
				beneficiario.setCuenta(record.get(0));
				beneficiario.setNumLinea(record.get(1));
				beneficiario.setBancoParticipante(record.get(2));
				beneficiario.setTipoCuenta(record.get(3));
				beneficiario.setMoneda(record.get(4));
				beneficiario.setImporteMaximo(record.get(5));
				beneficiario.setTipoPersona(record.get(6));
				beneficiario.setRazonSocial(record.get(7));
				beneficiario.setNombre(record.get(8));
				beneficiario.setApellidoMaterno(record.get(9));
				beneficiario.setApellidoPaterno(record.get(10));
				return beneficiario;
			}

			@Override
			protected String[] getHeader() {
				return null;
			}
		};

		try {
			importer.importFile(file);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}
