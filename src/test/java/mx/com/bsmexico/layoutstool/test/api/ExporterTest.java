package mx.com.bsmexico.layoutstool.test.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import mx.com.bsmexico.customertool.api.exporter.CSVExporter;
import mx.com.bsmexico.customertool.api.exporter.ExportSource;
import mx.com.bsmexico.layoutstool.test.api.misc.Beneficiario;

public class ExporterTest {

	@Test
	public void exporterTest() {
		final ExportSource<Beneficiario> source = new ExportSource<Beneficiario>() {

			@Override
			public List<Beneficiario> getData() {
				final List<Beneficiario> data = new ArrayList<Beneficiario>();
				final Beneficiario beneficiario = new Beneficiario();
				beneficiario.setCuenta("00000000");
				beneficiario.setNumLinea("111111");
				beneficiario.setBancoParticipante("000");
				beneficiario.setTipoCuenta("00");
				beneficiario.setMoneda("MXN");
				beneficiario.setImporteMaximo("0.00");
				beneficiario.setTipoPersona("00");
				beneficiario.setRazonSocial("Test");
				beneficiario.setNombre("Test");
				beneficiario.setApellidoMaterno("Test");
				beneficiario.setApellidoPaterno("Test");
				data.add(beneficiario);
				return data;
			}
		};

		final CSVExporter<Beneficiario> exporter = new CSVExporter<Beneficiario>(source) {			

			@Override
			protected Object[] getRecord(Beneficiario beneficiario) {
				final List<Object> record = new ArrayList<>();
				record.add(beneficiario.getCuenta());
				record.add(beneficiario.getNumLinea());
				record.add(beneficiario.getBancoParticipante());
				record.add(beneficiario.getTipoCuenta());
				record.add(beneficiario.getMoneda());
				record.add(beneficiario.getImporteMaximo());
				record.add(beneficiario.getTipoPersona());
				record.add(beneficiario.getRazonSocial());
				record.add(beneficiario.getNombre());
				record.add(beneficiario.getApellidoPaterno());
				record.add(beneficiario.getApellidoMaterno());
				return record.toArray();
			}

			@Override
			protected String[] getHeader() {
				return null;
			}
		};
		
		final ClassLoader classLoader = getClass().getClassLoader();
		final File file = new File(classLoader.getResource("out/testExport.csv").getFile());
		try {
			exporter.export(file);
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String st;
				while ((st = br.readLine()) != null) {
					System.out.println(st);
					Assert.assertTrue("00000000,111111,000,00,MXN,0.00,00,Test,Test,Test,Test".equals(st));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}
